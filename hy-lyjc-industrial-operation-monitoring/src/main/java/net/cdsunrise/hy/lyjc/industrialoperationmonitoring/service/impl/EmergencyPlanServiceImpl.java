package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyPlan;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyPlanAttachment;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.EmergencyPlanMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyPlanAttachmentService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyPlanKeywordService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyPlanService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.UserService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyPlanCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyPlanVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import net.cdsunrise.hy.sso.starter.domain.UserResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2019/11/25 16:50
 */
@Service
public class EmergencyPlanServiceImpl extends ServiceImpl<EmergencyPlanMapper, EmergencyPlan> implements IEmergencyPlanService {

    private static final String EVENT_TYPE_PARENT_CODE = "EMERGENCY_EVENT_TYPE";
    private static final String EVENT_LEVEL_PARENT_CODE = "EMERGENCY_EVENT_LEVEL";
    private static final List<String> ORDER_COLUMNS = Arrays.asList("event_type", "event_level", "gmt_modified");


    private final IEmergencyPlanAttachmentService emergencyPlanAttachmentService;
    private final IEmergencyPlanKeywordService emergencyPlanKeywordService;
    private final DataDictionaryFeignClient dataDictionaryFeignClient;
    private final UserService userService;

    public EmergencyPlanServiceImpl(IEmergencyPlanAttachmentService emergencyPlanAttachmentService, IEmergencyPlanKeywordService emergencyPlanKeywordService, DataDictionaryFeignClient dataDictionaryFeignClient, UserService userService) {
        this.emergencyPlanAttachmentService = emergencyPlanAttachmentService;
        this.emergencyPlanKeywordService = emergencyPlanKeywordService;
        this.dataDictionaryFeignClient = dataDictionaryFeignClient;
        this.userService = userService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveEmergencyPlan(EmergencyPlanVO emergencyPlanVO) {
        // 基础信息
        Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel = getAllEmergencyEventTypeAndLevel();
        checkEventTypeAndLevel(allEmergencyEventTypeAndLevel, emergencyPlanVO);
        EmergencyPlan emergencyPlan = new EmergencyPlan();
        BeanUtils.copyProperties(emergencyPlanVO, emergencyPlan, "id", "gmtModified", "modifierId");
        // 当前用户
        emergencyPlan.setModifierId(CustomContext.getTokenInfo().getUser().getId());
        // 存储
        super.save(emergencyPlan);
        // 关键词
        emergencyPlanKeywordService.save(emergencyPlan.getId(), emergencyPlanVO.getKeywords());
        // 附件
        List<EmergencyPlanAttachment> emergencyPlanAttachmentList = getAttachments(emergencyPlan.getId(), emergencyPlanVO);
        emergencyPlanAttachmentService.saveBatch(emergencyPlanAttachmentList);

        return emergencyPlan.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEmergencyPlan(Long id) {
        if (id == null) {
            throw new ParamErrorException("预案ID不能为空");
        }
        // 删除基础信息
        boolean success = super.removeById(id);
        // 删除关键词
        emergencyPlanKeywordService.delete(id);
        // 删除附件
        if (success) {
            QueryWrapper<EmergencyPlanAttachment> queryWrapper = Wrappers.query();
            queryWrapper.lambda().eq(EmergencyPlanAttachment::getPlanId, id);
            emergencyPlanAttachmentService.remove(queryWrapper);
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEmergencyPlan(EmergencyPlanVO emergencyPlanVO) {
        // 基础信息
        Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel = getAllEmergencyEventTypeAndLevel();
        checkEventTypeAndLevel(allEmergencyEventTypeAndLevel, emergencyPlanVO);
        EmergencyPlan emergencyPlan = new EmergencyPlan();
        BeanUtils.copyProperties(emergencyPlanVO, emergencyPlan, "gmtModified", "modifierId");
        // 当前用户
        emergencyPlan.setModifierId(CustomContext.getTokenInfo().getUser().getId());
        // 附件
        List<EmergencyPlanAttachment> emergencyPlanAttachmentList = getAttachments(emergencyPlan.getId(), emergencyPlanVO);
        // 更新
        boolean success = super.updateById(emergencyPlan);
        if (success) {
            // 删除旧关键词
            emergencyPlanKeywordService.delete(emergencyPlan.getId());
            // 重新存入关键词
            emergencyPlanKeywordService.save(emergencyPlan.getId(), emergencyPlanVO.getKeywords());
            // 删除旧附件
            QueryWrapper<EmergencyPlanAttachment> queryWrapper = Wrappers.query();
            queryWrapper.lambda().eq(EmergencyPlanAttachment::getPlanId, emergencyPlanVO.getId());
            emergencyPlanAttachmentService.remove(queryWrapper);
            // 重新存入附件
            emergencyPlanAttachmentService.saveBatch(emergencyPlanAttachmentList);
        }

        return success;
    }

    @Override
    public EmergencyPlanVO getEmergencyPlan(Long id) {
        if (id == null) {
            throw new ParamErrorException("预案ID不能为空");
        }
        // 基础信息
        Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel = getAllEmergencyEventTypeAndLevel();
        EmergencyPlan emergencyPlan = super.getById(id);
        if (emergencyPlan == null) {
            throw new ParamErrorException("预案不存在");
        }
        // 关键词
        Set<String> keywords = emergencyPlanKeywordService.get(id);

        return convertToVO(allEmergencyEventTypeAndLevel, emergencyPlan, keywords);
    }

    @Override
    public IPage<EmergencyPlanVO> listEmergencyPlan(PageRequest<EmergencyPlanCondition> pageRequest) {
        // 分页
        Page<EmergencyPlan> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<EmergencyPlan> queryWrapper = Wrappers.query();
        EmergencyPlanCondition condition = pageRequest.getCondition();
        //筛选
        if (condition != null) {
            queryWrapper.lambda()
                    .eq(StringUtils.hasText(condition.getEventType()), EmergencyPlan::getEventType, condition.getEventType())
                    .eq(StringUtils.hasText(condition.getEventLevel()), EmergencyPlan::getEventLevel, condition.getEventLevel());
        }
        // 排序
        PageUtil.setOrders(queryWrapper, ORDER_COLUMNS, pageRequest.getOrderItemList());
        super.page(page, queryWrapper);
        // 数据字典
        Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel = getAllEmergencyEventTypeAndLevel();
        // 关键词
        Set<Long> ids = page.getRecords().stream().map(EmergencyPlan::getId).collect(Collectors.toSet());
        Map<Long, Set<String>> keywordMap = emergencyPlanKeywordService.getMap(ids);
        return page.convert(emergencyPlan -> convertToVO(allEmergencyEventTypeAndLevel, emergencyPlan, keywordMap.getOrDefault(emergencyPlan.getId(), new HashSet<>(0))));
    }

    @Override
    public List<EmergencyPlan> listForEvaluate(String eventType) {
        if (!StringUtils.hasText(eventType)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<EmergencyPlan> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(EmergencyPlan::getId, EmergencyPlan::getPlanName, EmergencyPlan::getEventLevel)
                .eq(EmergencyPlan::getEventType, eventType);
        return list(queryWrapper);
    }

    @Override
    public Map<Long, Set<String>> getKeywordMap(Set<Long> emergencyPlanIds) {
        return emergencyPlanKeywordService.getMap(emergencyPlanIds);
    }

    private EmergencyPlanVO convertToVO(Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel, EmergencyPlan emergencyPlan, Set<String> keywords) {
        EmergencyPlanVO emergencyPlanVO = new EmergencyPlanVO();
        BeanUtils.copyProperties(emergencyPlan, emergencyPlanVO);
        DataDictionaryVO eventType = getEventType(allEmergencyEventTypeAndLevel, emergencyPlan.getEventType());
        if (eventType != null) {
            emergencyPlanVO.setEventTypeDesc(eventType.getName());
        }
        DataDictionaryVO eventLevel = getEventLevel(allEmergencyEventTypeAndLevel, emergencyPlan.getEventLevel());
        if (eventLevel != null) {
            emergencyPlanVO.setEventLevelDesc(eventLevel.getName());
        }
        // 获取修改人名称
        UserResp userResp = userService.getUserById(emergencyPlan.getModifierId());
        if (userResp != null) {
            emergencyPlanVO.setModifierName(userResp.getUserName());
        }
        // 关键词
        emergencyPlanVO.setKeywords(keywords);
        // 附件
        List<EmergencyPlanVO.Attachment> attachmentVos = getAttachmentVos(emergencyPlan);
        emergencyPlanVO.setAttachments(attachmentVos);

        return emergencyPlanVO;
    }

    private Map<String, Map<String, DataDictionaryVO>> getAllEmergencyEventTypeAndLevel() {
        Result<Map<String, DataDictionaryVO>> result = dataDictionaryFeignClient.getByCodes(new String[]{EVENT_TYPE_PARENT_CODE, EVENT_LEVEL_PARENT_CODE});
        Map<String, DataDictionaryVO> data = result.getData();
        if (data == null || data.isEmpty()) {
            throw new RuntimeException("事件类型及事件等级未在数据字典定义");
        }
        Map<String, Map<String, DataDictionaryVO>> map = new HashMap<>(2);
        DataDictionaryVO eventTypeDataDictionaryVO = data.get(EVENT_TYPE_PARENT_CODE);
        if (eventTypeDataDictionaryVO == null) {
            throw new RuntimeException("事件类型未在数据字典定义");
        }
        DataDictionaryVO eventLevelDataDictionaryVO = data.get(EVENT_LEVEL_PARENT_CODE);
        if (eventLevelDataDictionaryVO == null) {
            throw new RuntimeException("事件等级未在数据字典定义");
        }
        map.put(EVENT_LEVEL_PARENT_CODE, eventLevelDataDictionaryVO.getChildren().stream().collect(Collectors.toMap(DataDictionaryVO::getCode, item -> item)));
        map.put(EVENT_TYPE_PARENT_CODE, eventTypeDataDictionaryVO.getChildren().stream().collect(Collectors.toMap(DataDictionaryVO::getCode, item -> item)));
        return map;
    }

    private DataDictionaryVO getDictionary(Map<String, DataDictionaryVO> allTypes, String type) {
        if (allTypes == null || allTypes.isEmpty()) {
            return null;
        }
        return allTypes.get(type);
    }

    private DataDictionaryVO getEventType(Map<String, Map<String, DataDictionaryVO>> map, String type) {
        Map<String, DataDictionaryVO> allTypes = map.get(EVENT_TYPE_PARENT_CODE);
        return getDictionary(allTypes, type);
    }

    private DataDictionaryVO getEventLevel(Map<String, Map<String, DataDictionaryVO>> map, String type) {
        Map<String, DataDictionaryVO> allTypes = map.get(EVENT_LEVEL_PARENT_CODE);
        return getDictionary(allTypes, type);
    }

    private void checkEventType(Map<String, Map<String, DataDictionaryVO>> map, String type) {
        DataDictionaryVO dataDictionaryVO = getEventType(map, type);
        if (dataDictionaryVO == null) {
            throw new ParamErrorException("未知的事件类型编码: " + type);
        }
    }

    private void checkEventLevel(Map<String, Map<String, DataDictionaryVO>> map, String type) {
        DataDictionaryVO dataDictionaryVO = getEventLevel(map, type);
        if (dataDictionaryVO == null) {
            throw new ParamErrorException("未知的事件等级编码: " + type);
        }
    }

    private void checkEventTypeAndLevel(Map<String, Map<String, DataDictionaryVO>> map, EmergencyPlanVO emergencyPlanVO) {
        String eventType = emergencyPlanVO.getEventType();
        checkEventType(map, eventType);
        String eventLevel = emergencyPlanVO.getEventLevel();
        checkEventLevel(map, eventLevel);
    }

    private List<EmergencyPlanAttachment> getAttachments(Long planId, EmergencyPlanVO emergencyPlanVO) {
        List<EmergencyPlanVO.Attachment> attachments = emergencyPlanVO.getAttachments();
        return attachments.stream().map(attachment -> {
            EmergencyPlanAttachment emergencyPlanAttachment = new EmergencyPlanAttachment();
            BeanUtils.copyProperties(attachment, emergencyPlanAttachment, "id");
            emergencyPlanAttachment.setPlanId(planId);
            return emergencyPlanAttachment;
        }).collect(Collectors.toList());
    }

    private List<EmergencyPlanVO.Attachment> getAttachmentVos(EmergencyPlan emergencyPlan) {
        QueryWrapper<EmergencyPlanAttachment> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(EmergencyPlanAttachment::getPlanId, emergencyPlan.getId());
        List<EmergencyPlanAttachment> emergencyPlanAttachments = emergencyPlanAttachmentService.list(queryWrapper);
        if (CollectionUtils.isEmpty(emergencyPlanAttachments)) {
            return null;
        }
        return emergencyPlanAttachments.stream().map(emergencyPlanAttachment -> {
            EmergencyPlanVO.Attachment attachment = new EmergencyPlanVO.Attachment();
            BeanUtils.copyProperties(emergencyPlanAttachment, attachment);
            return attachment;
        }).collect(Collectors.toList());
    }
}
