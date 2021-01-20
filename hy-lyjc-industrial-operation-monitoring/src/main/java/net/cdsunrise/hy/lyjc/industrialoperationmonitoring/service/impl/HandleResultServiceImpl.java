package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.ComplaintManage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.HandleResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.ComplaintImportanceEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.IndustryMerchantTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.MessageMenuEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.OperationEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.HandleResultMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ComplaintManageService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.HandleResultService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.mapper.RoleMenuMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.HandleResultVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.LogHandleResultVO;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.ComplainIntegralFeignClient;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.vo.req.ComplainIntegralReq;
import net.cdsunrise.hy.message.center.autoconfigure.feign.MessageCenterFeignClient;
import net.cdsunrise.hy.message.center.autoconfigure.feign.enums.AppEnum;
import net.cdsunrise.hy.message.center.autoconfigure.feign.enums.CategoryEnum;
import net.cdsunrise.hy.message.center.autoconfigure.feign.req.MessageRequest;
import net.cdsunrise.hy.record.starter.service.RecordService;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import net.cdsunrise.hy.sso.starter.domain.TokenInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author LHY
 */
@Service
@Slf4j
public class HandleResultServiceImpl implements HandleResultService {

    private HandleResultMapper resultMapper;
    private ComplaintManageService manageService;
    private RecordService recordService;
    private DataDictionaryFeignClient feignClient;
    private MessageCenterFeignClient messageCenterFeignClient;
    private RoleMenuMapper roleMenuMapper;
    private final ComplainIntegralFeignClient complainIntegralFeignClient;

    public HandleResultServiceImpl(HandleResultMapper resultMapper, ComplaintManageService manageService, RecordService recordService, DataDictionaryFeignClient feignClient, MessageCenterFeignClient messageCenterFeignClient, RoleMenuMapper roleMenuMapper, ComplainIntegralFeignClient complainIntegralFeignClient) {
        this.resultMapper = resultMapper;
        this.manageService = manageService;
        this.recordService = recordService;
        this.feignClient = feignClient;
        this.messageCenterFeignClient = messageCenterFeignClient;
        this.roleMenuMapper = roleMenuMapper;
        this.complainIntegralFeignClient = complainIntegralFeignClient;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long agreeHandle(Long complaintId) {
        HandleResult handleResult = new HandleResult();
        handleResult.setComplaintId(complaintId);
        handleResult.setAssignee(CustomContext.getTokenInfo().getUser().getUserName());
        handleResult.setAssigneeTime(new Timestamp(System.currentTimeMillis()));
        resultMapper.insert(handleResult);
        manageService.updateStatus(complaintId, ParamConst.BEING_HANDLE_STATUS);
        // 记录操作日志
        ComplaintManage complaintManage = manageService.findById(complaintId);
        recordService.operation(OperationEnum.COMPLAINT_MANAGE, complaintManage, CustomContext.getTokenInfo().getUser().getId(), "受理");
        try {
            // 推送消息
            MessageRequest.AddReq addReq = new MessageRequest.AddReq();
            addReq.setApp(AppEnum.OPERATION);
            addReq.setCategory(CategoryEnum.TODO);
            addReq.setType("投诉管理 - 待处理");
            addReq.setMenuUri(MessageMenuEnum.OPERATION_COMPLAINT_NOT_FINISH.getUri());
            addReq.setRedirect(true);
            addReq.setAggs(complaintManage.getComplaintObject());
            addReq.setBrief(complaintManage.getType());
            addReq.setBtime(complaintManage.getComplaintTime().getTime());
            addReq.setDetail("投诉内容：" + complaintManage.getContent() + "，来源：" + complaintManage.getChannel());
            List<Long> users = roleMenuMapper.getUserIdByMenuCode(MessageMenuEnum.OPERATION_COMPLAINT_NOT_FINISH.getCode());
            addReq.setUserIds(users);
            messageCenterFeignClient.add(addReq);
        } catch (Exception e) {
            log.error("推送消息失败", e);
        }
        return handleResult.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long disagreeHandle(HandleResult handleResult) {
        handleResult.setAssignee(CustomContext.getTokenInfo().getUser().getUserName());
        handleResult.setAssigneeTime(new Timestamp(System.currentTimeMillis()));
        resultMapper.insert(handleResult);
        manageService.updateStatus(handleResult.getComplaintId(), ParamConst.REJECT_HANDLE_STATUS);
        // 记录操作日志
        ComplaintManage complaintManage = manageService.findById(handleResult.getComplaintId());
        recordService.operation(OperationEnum.COMPLAINT_MANAGE, complaintManage, CustomContext.getTokenInfo().getUser().getId(), "不受理");
        return handleResult.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishHandle(HandleResultVO handleResultVO) {
        UpdateWrapper<HandleResult> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("complaint_id", handleResultVO.getComplaintId());
        HandleResult handleResult = new HandleResult();
        handleResult.setComplaintObjectFullname(handleResultVO.getComplaintObjectFullname());
        handleResult.setIndustryType(handleResultVO.getIndustryType());
        handleResult.setImportance(handleResultVO.getImportance());
        handleResult.setHandlerResult(handleResultVO.getHandlerResult());
        handleResult.setHandler(CustomContext.getTokenInfo().getUser().getUserName());
        handleResult.setHandleTime(new Timestamp(System.currentTimeMillis()));
        resultMapper.update(handleResult, updateWrapper);
        manageService.updateStatus(handleResultVO.getComplaintId(), ParamConst.FINISH_HANDLE_STATUS);

        // 记录操作日志
        LogHandleResultVO logHandleResultVO = new LogHandleResultVO();
        logHandleResultVO.setComplaintNumber(manageService.findById(handleResultVO.getComplaintId()).getComplaintNumber());
        logHandleResultVO.setComplaintObjectFullname(handleResultVO.getComplaintObjectFullname());
        String[] codeParam = {handleResultVO.getIndustryType()};
        Map<String, DataDictionaryVO> map = feignClient.getByCodes(codeParam).getData();
        logHandleResultVO.setIndustryType(map.get(handleResultVO.getIndustryType()).getName());
        recordService.operation(OperationEnum.HANDLE_RESULT, logHandleResultVO, CustomContext.getTokenInfo().getUser().getId(), "处理");
    }

    @Override
    public HandleResultVO findById(Long complaintId) {
        QueryWrapper<HandleResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(HandleResult::getComplaintId, complaintId);
        HandleResult handleResult = resultMapper.selectOne(queryWrapper);
        if (Objects.isNull(handleResult)) {
            return null;
        }
        return convertToVO(handleResult);
    }

    /**
     * 投诉结案后，记录被投诉方全称、投诉行业分类，支持商户管理平台能同步投诉记录。
     */
    @Async
    @Override
    public void notifyAfterHandleCompleted(long complaintId, long userId, String userName, String token) {
        log.info("诉结案后，记录被投诉方全称、投诉行业分类，支持商户管理平台能同步投诉记录。");
        ComplaintManage complaintManage = manageService.getById(complaintId);
        if (complaintManage == null) {
            return;
        }
        // 将旅游监测的行业类型转为 商户平台对应的行业类型code  ,如果不为空 则推送.
        String shTypeCode = getShCodeByLyjcCode(complaintManage.getType());
        if (StringUtils.isBlank(shTypeCode)) {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>该行业类型不需要推送到商户:{}", shTypeCode);
            return;
        }

        ComplainIntegralReq.AddReq addReq = new ComplainIntegralReq.AddReq();
        addReq.setMerchantTypeCode(shTypeCode);
        addReq.setMerchantName(complaintManage.getComplaintObject());
        addReq.setContent(complaintManage.getContent());
        addReq.setComplainTime(complaintManage.getComplaintTime());
        addReq.setComplainant(complaintManage.getComplainant());
        addReq.setHandleBy(userId);
        addReq.setHandleName(userName);
        addReq.setHandleTime(new Date());
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(token);
        CustomContext.setTokenInfo(tokenInfo);
        Result result = complainIntegralFeignClient.saveComplainIntegral(addReq);
        log.info("商户管理平台同步投诉记录结果:{}", result.toString());
    }

    /**
     * 由于 行业类型在 商户平台和 旅游运行监测平台的编码不一样所以需要手动转换
     *
     * @param lyjcCode 行业类型在旅游监测平台的编码
     * @return string
     */
    private String getShCodeByLyjcCode(String lyjcCode) {
        return IndustryMerchantTypeEnum.getShCodeByLyjcCode(lyjcCode);
    }

    private HandleResultVO convertToVO(HandleResult src){
        HandleResultVO dest = new HandleResultVO();
        BeanUtils.copyProperties(src,dest);
        ComplaintImportanceEnum complaintImportanceEnum = ComplaintImportanceEnum.getByImportance(src.getImportance());
        if(Objects.nonNull(complaintImportanceEnum)){
            dest.setImportanceDesc(complaintImportanceEnum.getImportanceDesc());
        }
        return dest;
    }
}
