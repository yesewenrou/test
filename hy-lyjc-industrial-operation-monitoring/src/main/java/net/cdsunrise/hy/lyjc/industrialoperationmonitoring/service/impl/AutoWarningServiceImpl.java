package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.AutoWarningMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.JsonUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.AutoWarningCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import net.cdsunrise.hy.lyxxfb.iip.autoconfigure.feign.IMFeignClient;
import net.cdsunrise.hy.lyxxfb.iip.autoconfigure.feign.SmsFeignClient;
import net.cdsunrise.hy.lyxxfb.iip.autoconfigure.feign.req.PublishRequest;
import net.cdsunrise.hy.lyxxfb.iip.autoconfigure.feign.req.SmsFeignRequest;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author funnylog
 */
@Slf4j
@Service
public class AutoWarningServiceImpl extends ServiceImpl<AutoWarningMapper, AutoWarning> implements IAutoWarningService {

    private final IAutoWarningRecordService iAutoWarningRecordService;

    private final IAutoWarningRecordReceiverService iAutoWarningRecordReceiverService;
    private final IAutoWarningRecordAttachService iAutoWarningRecordAttachService;

    private final IAutoWarningScenicService iAutoWarningScenicService;

    private final ISmsContactService iSmsContactService;
    private final IMFeignClient imFeignClient;
    private final SmsFeignClient smsFeignClient;

    private final IAutoWarningTrafficService iAutoWarningTrafficService;
    private final IEmergencyEventService emergencyEventService;

    public AutoWarningServiceImpl(IAutoWarningRecordService iAutoWarningRecordService,
                                  IAutoWarningRecordReceiverService iAutoWarningRecordReceiverService,
                                  IAutoWarningRecordAttachService iAutoWarningRecordAttachService,
                                  IAutoWarningScenicService iAutoWarningScenicService,
                                  ISmsContactService iSmsContactService,
                                  IMFeignClient imFeignClient,
                                  SmsFeignClient smsFeignClient,
                                  IAutoWarningTrafficService iAutoWarningTrafficService,
                                  IEmergencyEventService emergencyEventService) {
        this.iAutoWarningRecordService = iAutoWarningRecordService;
        this.iAutoWarningRecordReceiverService = iAutoWarningRecordReceiverService;
        this.iAutoWarningRecordAttachService = iAutoWarningRecordAttachService;
        this.iAutoWarningScenicService = iAutoWarningScenicService;
        this.iSmsContactService = iSmsContactService;
        this.imFeignClient = imFeignClient;
        this.smsFeignClient = smsFeignClient;
        this.iAutoWarningTrafficService = iAutoWarningTrafficService;
        this.emergencyEventService = emergencyEventService;
    }

    /**
     * 待确认列表丶待信息发布 分页查询列表
     *
     * @param query queryCondition
     * @return 分页查询结果
     */
    @Override
    public IPage<AutoWarning> pageList(AutoWarningCondition.Query query) {
        log.info("待确认预警列表查询:{}", query.toString());
        QueryWrapper<AutoWarning> wrapper = new QueryWrapper<>();
        wrapper.select()
                .eq(StringUtils.isNotBlank(query.getType()), "type", query.getType())
                .like(StringUtils.isNotBlank(query.getObject()), "object", query.getObject())
                .in(!CollectionUtils.isEmpty(query.getStatus()), "status", query.getStatus())
                .between("warning_time", query.getBegin(), query.getEnd());
        String[] orderBy = query.getOrderBy();
        if (ArrayUtils.isEmpty(orderBy)) {
            wrapper.orderByDesc("warning_time");
        } else {
            wrapper.orderBy(true, query.getAsc(), query.getOrderBy());
        }
        long size = query.getSize() == null ? -1L : query.getSize();
        Page<AutoWarning> pageRequest = new Page<>(query.getPage(), size);
        return baseMapper.selectPage(pageRequest, wrapper);
    }


    /**
     * 待确认预警 - 申请发布
     *
     * @param requestPublic requestPublic
     * @return result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result handleRequestPublic(AutoWarningCondition.RequestPublic requestPublic) {
        log.info("预警管理-待确认预警-申请发布:{}", requestPublic.toString());
        // 判断warnId是否有效
        Long warningId = requestPublic.getId();
        AutoWarning autoWarning = super.baseMapper.selectById(warningId);
        if (autoWarning == null) {
            return ResultUtil.paramError("预警id不存在");
        }
        // 判断状态 是否可以发布信息屏或者发短信
        Integer handleType = requestPublic.getHandleType();
        AutoWarningOperateEnum currentOperate;
        boolean isApplyPublish = ("" + handleType).equalsIgnoreCase(AutoWarningEnum.HANDLE_TYPE_1.getCode());
        boolean isSMS = ("" + handleType).equalsIgnoreCase(AutoWarningEnum.HANDLE_TYPE_2.getCode());
        if (isApplyPublish) {
            currentOperate = AutoWarningOperateEnum.PUBLISH;
        } else if (isSMS) {
            currentOperate = AutoWarningOperateEnum.SMS;
        } else {
            return ResultUtil.paramError("处理类型不正确");
        }
        if (!checkNextOperateIsLegal(autoWarning.getStatus(), currentOperate)) {
            return ResultUtil.buildResult(false, "当前状态无法" + AutoWarningEnum.getByCode("" + handleType).getName());
        }
        // 4 后续处理
        boolean sendStatus = true;
        if (isApplyPublish) {
            // 如果是 申请发布 则修改预警状态为 已申请丶待发布
            autoWarning.setStatus(AutoWarningStatusEnum.WARN_CODE_033003.getCode());
            autoWarning.setUpdateTime(new Date());
            super.baseMapper.updateById(autoWarning);
        } else if (isSMS) {
            // 如果是 短信 则发送短信
            log.info("开始发送短信, 接收人:{}, 短信内容:{}", JsonUtil.toJsonString(requestPublic.getReceivers()), requestPublic.getContent());
            sendStatus = sendSms(requestPublic.getReceivers(), requestPublic.getContent());
        }

        // 1 保存操作记录 内容、处理人等信息
        AutoWarningRecord record = readyAutoWarningRecord(warningId, handleType, requestPublic.getContent(), "", "", "");
        iAutoWarningRecordService.save(record);
        Long recordId = record.getId();
        // 2 插入receiver
        readyAutoWarningRecordReceivers(recordId, handleType, requestPublic.getReceivers());
        // 3 保存附件
        readyAutoWarningRecordAttaches(recordId, requestPublic.getAttaches());
        return ResultUtil.buildSuccessResultWithData("success");
    }


    /**
     * 发送短信
     *
     * @param receivers 接收人ID
     */
    private boolean sendSms(Set<AutoWarningCondition.Receiver> receivers, String content) {
        if (CollectionUtils.isEmpty(receivers)) {
            return false;
        }
        List<Long> receiverIds = receivers.stream().map(AutoWarningCondition.Receiver::getId).collect(Collectors.toList());
        QueryWrapper<AutoWarningContactConfig> wrapper = new QueryWrapper<>();
        wrapper.select()
                .in("id", receiverIds);
        List<AutoWarningContactConfig> list = iSmsContactService.list(wrapper);
        if (!CollectionUtils.isEmpty(list)) {
            SmsFeignRequest req = new SmsFeignRequest();
            req.setContent(content);
            req.setMobiles(list.stream().map(AutoWarningContactConfig::getPhone).collect(Collectors.toList()));
            Result result = smsFeignClient.send(req);
            return result.getSuccess();
        }
        return false;
    }

    @Override
    public Result ignoreWarning(Long id, String statusCode) {
        log.info("预警管理-待确认预警-忽略预警:{}", id);
        // 判断statusCode是否有效
        if (!AutoWarningStatusEnum.WARN_CODE_033006.getCode().equals(statusCode) && !AutoWarningStatusEnum.WARN_CODE_033002.getCode().equals(statusCode)) {
            return ResultUtil.buildResult(false, "状态码无效");
        }
        // 判断warnId是否有效
        AutoWarning autoWarning = super.baseMapper.selectById(id);
        if (autoWarning != null) {
            // 判断是否可被忽略
            AutoWarningOperateEnum operateEnum = null;
            // 日志是否需要记录  忽略申请才需要记录, 普通忽略不需要记录
            boolean isOperateNeedToRecord = false;
            String checkResult = "忽略";
            if (AutoWarningStatusEnum.WARN_CODE_033002.getCode().equals(statusCode)) {
                operateEnum = AutoWarningOperateEnum.IGNORE;
            } else if (AutoWarningStatusEnum.WARN_CODE_033006.getCode().equals(statusCode)) {
                operateEnum = AutoWarningOperateEnum.REQUEST_PUBLISH_IGNORE;
                checkResult = "忽略申请";
                isOperateNeedToRecord = true;
            }
            boolean isLegal = checkNextOperateIsLegal(autoWarning.getStatus(), operateEnum);
            if (!isLegal) {
                return ResultUtil.buildResult(false, "当前状态无法" + checkResult);
            }
            // 修改状态
            autoWarning.setStatus(statusCode);
            autoWarning.setUpdateTime(new Date());
            boolean bool = super.updateById(autoWarning);
            log.info("预警管理-待确认预警-忽略结果:{}", bool);
            // 记录日志
            if (isOperateNeedToRecord) {
                AutoWarningRecord record = readyAutoWarningRecord(id, Integer.valueOf(AutoWarningEnum.HANDLE_TYPE_4.getCode()), "", "", "", "");
                iAutoWarningRecordService.save(record);
            }
            return ResultUtil.buildResult(bool, bool ? "成功" : "失败");
        }
        return ResultUtil.success();
    }

    @Override
    public WarningRecordDetailVO queryRecordDetail(Long id) {
        log.info("查询预警详情: {}", id);
        WarningRecordDetailVO warningRecordDetailVO = new WarningRecordDetailVO();
        AutoWarning autoWarning = super.baseMapper.selectById(id);
        if (autoWarning == null) {
            return warningRecordDetailVO;
        }
        if (autoWarning.getType().equals(AutoWarningTypeEnum.AutoWarningType_1.getCode())) {
            // 景区
            AutoWarningScenic scenic = iAutoWarningScenicService.queryByWarningId(id);
            WarningRecordDetailVO.ScenicDetail scenicDetail = new WarningRecordDetailVO.ScenicDetail();
            if (scenic != null) {
                scenicDetail.setScenicDetailId(scenic.getId());
                scenicDetail.setPeopleNum(scenic.getPeopleNum());
                scenicDetail.setScenicName(autoWarning.getObject());
                scenicDetail.setWarningTime(autoWarning.getWarningTime().getTime());
            }
            warningRecordDetailVO.setScenicDetail(scenicDetail);
        } else if (autoWarning.getType().equals(AutoWarningTypeEnum.AutoWarningType_2.getCode())) {
            // TODO 停车位
        } else if (autoWarning.getType().equals(AutoWarningTypeEnum.AutoWarningType_3.getCode())) {
            // TODO 交通
            AutoWarningTraffic autoWarningTraffic = iAutoWarningTrafficService.selectByWarningId(id);
            WarningRecordDetailVO.TrafficDetail trafficDetail = new WarningRecordDetailVO.TrafficDetail();
            if (autoWarningTraffic != null) {
                BeanUtils.copyProperties(autoWarningTraffic, trafficDetail);
                trafficDetail.setObject(autoWarningTraffic.getRoadName());
                trafficDetail.setWarningTime(autoWarning.getWarningTime());
            }
            warningRecordDetailVO.setTrafficDetail(trafficDetail);
        }
        // 查询操作记录
        queryOperateRecord(id, warningRecordDetailVO);
        // 设置预警类型
        warningRecordDetailVO.setType(autoWarning.getType());
        return warningRecordDetailVO;
    }

    /**
     * 根据应急事件id, 查询预警详情
     *
     * @param eventId 应急事件id
     * @return 预警详情
     */
    @Override
    public WarningRecordDetailVO queryRecordDetailByEventId(Long eventId) {
        log.info("根据应急事件id查询自动预警信息 eventId:{}", eventId);
        AutoWarning autoWarning = super.baseMapper.selectByEmergencyEventId(eventId);
        if (autoWarning == null) {
            log.info("根据应急事件id:{}, 没有查询到自动预警信息", eventId);
            return null;
        }
        return this.queryRecordDetail(autoWarning.getId());
    }

    /**
     * 信息发布
     *
     * @param addReq addReq
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result publish(AutoWarningCondition.PublishReq addReq) {
        log.info("信息发布:{}", addReq.toString());
        Long warningId = addReq.getId();
        AutoWarning autoWarning = super.baseMapper.selectById(warningId);
        if (autoWarning == null) {
            return ResultUtil.paramError("预警ID不存在");
        }
        // 发布内容
        addReq.setId(null);
        addReq.setPublishBy(CustomContext.getTokenInfo().getUser().getId());
        addReq.setPublishName(CustomContext.getTokenInfo().getUser().getUserName());
        addReq.setPublishTime(new Date());
        try {

            Result result = imFeignClient.add(CustomContext.getTokenInfo().getToken(), addReq);
            log.info("信息屏发布返回结果：{}", result.toString());
            if (!result.getSuccess()) {
                log.info("信息屏发布失败");
                return result;
            }
        } catch (Exception e) {
            log.info("信息屏发布异常", e);
            return ResultUtil.buildResult(false, "信息屏发布异常");
        }

        // 记录操作日志
        String messageChannel = "";
        int handleType = Integer.valueOf(AutoWarningEnum.HANDLE_TYPE_3.getCode());
        if (!CollectionUtils.isEmpty(addReq.getPushChannels())) {
            messageChannel = addReq.getPushChannels().stream().map(PublishRequest.Channel::getChannel).collect(Collectors.joining(","));
        }

        AutoWarningRecord record = readyAutoWarningRecord(warningId, handleType, addReq.getContent(), "", messageChannel, addReq.getProgramContent());
        iAutoWarningRecordService.save(record);
        Long recordId = record.getId();
        // 2 插入receiver receiver代表信息屏或者短信接收人

        Set<AutoWarningCondition.Receiver> receivers = null;
        if (addReq.getInductionScreens() != null) {
            receivers = addReq.getInductionScreens().stream().map(inductionScreen -> {
                AutoWarningCondition.Receiver receiver = new AutoWarningCondition.Receiver();
                receiver.setId(inductionScreen.getId());
                receiver.setName(inductionScreen.getGuidanceDisplayName());
                return receiver;
            }).collect(Collectors.toSet());
        }
        readyAutoWarningRecordReceivers(recordId, handleType, receivers);
        // 3 保存附件 - 正式发布没有附件
        // 4 正式发布后需要发布到 应急事件
        long emergencyEventId = emergencyEventAdd(autoWarning);
        // 修改状态为已发布 并关联应急事件ID
        autoWarning.setUpdateTime(new Date());
        autoWarning.setStatus(AutoWarningStatusEnum.WARN_CODE_033005.getCode());
        autoWarning.setEmergencyEventId(emergencyEventId);
        super.baseMapper.updateById(autoWarning);
        return ResultUtil.success();
    }

    /**
     * 节目信息发布
     *
     * @param addReq addReq
     * @return Result
     */
    @Override
    public Result publishProgram(AutoWarningCondition.PublishProgramReq addReq) {
        log.info("节目信息发布请求:{}", addReq.toString());

        // 发布内容
        addReq.setId(null);
        addReq.setPublishBy(CustomContext.getTokenInfo().getUser().getId());
        addReq.setPublishName(CustomContext.getTokenInfo().getUser().getUserName());
        addReq.setPublishTime(new Date());
        try {
            Result result = imFeignClient.add(CustomContext.getTokenInfo().getToken(), addReq);
            log.info(">>> 信息屏发布返回结果：{}", result.toString());
            if (!result.getSuccess()) {
                log.info("信息屏发布失败");
            }
            return result;
        } catch (Exception e) {
            log.info("信息屏发布异常", e);
            return ResultUtil.buildResult(false, "信息屏发布异常");
        }
    }


    /**
     * 正式发布后 生成应急事件
     *
     * @param autoWarning autoWarning
     * @return
     */
    private Long emergencyEventAdd(AutoWarning autoWarning) {
        String typeNickName = "";
        String content = "";
        String warningObject = autoWarning.getObject();
        String warningType = autoWarning.getType();
        Date warningDate = autoWarning.getWarningTime();
        Long warningId = autoWarning.getId();
        String warningStringDate = DateUtil.format(warningDate, "yyyy年M月d日 HH时mm分");
        String eventType = "";
        if (warningType.equals(AutoWarningTypeEnum.AutoWarningType_1.getCode())) {
            // 客流量: {告警对象}，{告警时间}，景区游客数达到 {当前客流量} 人，请及时协调处理！
            typeNickName = "客流预警";
            AutoWarningScenic autoWarningScenic = iAutoWarningScenicService.queryByWarningId(warningId);
            Integer peopleNum = 0;
            if (autoWarningScenic != null) {
                peopleNum = autoWarningScenic.getPeopleNum();
            }
            String detail = "景区游客数达到" + peopleNum + "人, 请及时协调处理!";
            content = String.join(", ", warningObject, warningStringDate, detail);
            eventType = "014007";
        } else if (warningType.equals(AutoWarningTypeEnum.AutoWarningType_2.getCode())) {
            // 停车
            typeNickName = "";
        } else if (warningType.equals(AutoWarningTypeEnum.AutoWarningType_3.getCode())) {
            // 车流量: {告警对象}，{告警时间}，{告警原因}，均速达到{平均速度}，请及时协调处理！
            typeNickName = "交通拥堵";
            AutoWarningTraffic autoWarningTraffic = iAutoWarningTrafficService.selectByWarningId(warningId);
            String warningReason = "";
            String avgSpeed = "";
            if (autoWarningTraffic != null) {
                warningReason = autoWarningTraffic.getWarningReason();
                avgSpeed = autoWarningTraffic.getAverageSpeed();
            }
            avgSpeed = "均速达到" + avgSpeed + ", 请及时协调处理!";
            content = String.join(", ", warningObject, warningStringDate, warningReason, avgSpeed, avgSpeed);
            eventType = "014006";
        }
        // 事件等级默认为一般, 事件类型默认分别为 交通拥堵和 客流预警
        String eventLevel = "015001";
        EmergencyEventAddVO emergencyEventAddVO = new EmergencyEventAddVO();
        emergencyEventAddVO.setEventName(warningObject + typeNickName);
        emergencyEventAddVO.setEventType(eventType);
        emergencyEventAddVO.setEventLevel(eventLevel);
        emergencyEventAddVO.setEventAddress(warningObject);
        emergencyEventAddVO.setEventContent(content);
        emergencyEventAddVO.setContact("");
        emergencyEventAddVO.setEventTime(warningDate);
        return emergencyEventService.saveSystemEmergencyEvent(emergencyEventAddVO);
    }


    /**
     * 查询该预警已经通过短信发送给了哪些人
     *
     * @param warningId warningId
     * @return String
     */
    @Override
    public Result querySentContact(Long warningId) {
        AutoWarning autoWarning = super.baseMapper.selectById(warningId);
        if (autoWarning == null) {
            return ResultUtil.paramError("预警ID不存在");
        }

        List<AutoWarningRecord> records = iAutoWarningRecordService.queryByWarningId(warningId, Integer.valueOf(AutoWarningEnum.HANDLE_TYPE_2.getCode()));
        if (CollectionUtils.isEmpty(records)) {
            return ResultUtil.success();
        }
        Set<String> receiverSets = new HashSet<>();
        for (AutoWarningRecord record : records) {
            List<AutoWarningRecordReceiver> recordReceivers = iAutoWarningRecordReceiverService.selectByRecordId(record.getId());
            if (!CollectionUtils.isEmpty(recordReceivers)) {
                receiverSets.addAll(recordReceivers.stream().map(AutoWarningRecordReceiver::getReceiverName).collect(Collectors.toSet()));
            }
        }
        return ResultUtil.buildSuccessResultWithData(String.join(", ", receiverSets));
    }

    /**
     * 按状态统计
     *
     * @return result
     */
    @Override
    public Result<List<Map<String, Integer>>> statistics() {
        List<Map<String, Integer>> list = baseMapper.countGroupByStatus();
        return ResultUtil.buildSuccessResultWithData(list);
    }

    /**
     * 景区客流量预警信息保存
     *
     * @param scenicWarning info
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveScenicAutoWarning(AutoWarningDTO.ScenicWarning scenicWarning) {
        log.info("保存景区预警信息:{}", scenicWarning.toString());
        // 保存基础信息
        AutoWarning autoWarning = new AutoWarning();
        BeanUtils.copyProperties(scenicWarning, autoWarning);
        autoWarning.setStatus(AutoWarningStatusEnum.WARN_CODE_033001.getCode());
        super.baseMapper.insert(autoWarning);

        // 保存景区独有信息
        AutoWarningScenic autoWarningScenic = new AutoWarningScenic();
        autoWarningScenic.setWarningId(autoWarning.getId());
        autoWarningScenic.setPeopleNum(scenicWarning.getPeopleNum());
        iAutoWarningScenicService.save(autoWarningScenic);

        return ResultUtil.success();
    }

    /**
     * 根据预警类型统计待确认状态的预警
     *
     * @return list
     */
    @Override
    @CachePut(cacheNames = "LYJC", key = "'WARNING_UNCONFIRMED'")
    public List<WarningUnconfirmedVO> countUnconfirmedWarningByType() {
        List<WarningUnconfirmedVO> list = new ArrayList<>();
        // 查询景区待确认数量
        Arrays.asList(AutoWarningTypeEnum.values()).forEach(autoWarningTypeEnum -> {
            String type = autoWarningTypeEnum.getCode();
            Integer count = super.baseMapper.selectCountByTypeAndStatus(type, AutoWarningStatusEnum.WARN_CODE_033001.getCode());
            WarningUnconfirmedVO warningUnconfirmedVO = new WarningUnconfirmedVO();
            warningUnconfirmedVO.setCode(type);
            warningUnconfirmedVO.setCount(count);
            list.add(warningUnconfirmedVO);
        });
        return list;
    }

    /**
     * 交通预警
     *
     * @param autoWarningTrafficDTO 预警信息
     * @return result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result trafficWarning(AutoWarningTrafficDTO autoWarningTrafficDTO) {
        log.info("交通预警信息:{}", autoWarningTrafficDTO.toString());
        // 1 保存预警信息
        AutoWarning autoWarning = new AutoWarning();
        autoWarning.setType(AutoWarningTypeEnum.AutoWarningType_3.getCode());
        autoWarning.setObject(autoWarningTrafficDTO.getRoadName());
        autoWarning.setStatus(AutoWarningStatusEnum.WARN_CODE_033001.getCode());
        autoWarning.setWarningTime(new Date(autoWarningTrafficDTO.getWarningTime()));
        long id = this.baseMapper.insert(autoWarning);
        // 2. 保存交通信息
        AutoWarningTraffic autoWarningTraffic = new AutoWarningTraffic();
        BeanUtils.copyProperties(autoWarningTrafficDTO, autoWarningTraffic);
        autoWarningTraffic.setWarningId(autoWarning.getId());
        boolean saveStatus = iAutoWarningTrafficService.save(autoWarningTraffic);

        // 3. 自动发送短信
        if (id > 0 && saveStatus) {
            List<AutoWarningContactConfig> autoSMSList = iSmsContactService.getTrafficAuto(true);
            if (!CollectionUtils.isEmpty(autoSMSList)) {
                List<String> phones = autoSMSList.stream().map(AutoWarningContactConfig::getPhone).collect(Collectors.toList());
                String content = "当前[" + autoWarningTrafficDTO.getRoadName() + "]车流量较大，请关注交通拥堵状况。";
                SmsFeignRequest req = new SmsFeignRequest();
                req.setContent(content);
                req.setMobiles(phones);
                Result autoSMSResult = smsFeignClient.send(req);
                log.info("自动发送短信返回结果:{}", autoSMSResult.toString());
            }
        }
        return ResultUtil.success();
    }

    /**
     * 定时任务 - 预警自动过期
     */
    @Override
    public void warningAutoExpire() {
        log.info(">>>>>>>>>>>>>>>>>>>> 预警自动过期");
        List<AutoWarning> list = super.baseMapper.selectByStatus(AutoWarningStatusEnum.WARN_CODE_033001.getCode());
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        // 判断时间  客流 2小时过期,  交通30分钟过期
        String expireString = "";
        for (AutoWarning autoWarning : list) {
            int autoExpire = 0;
            if (AutoWarningTypeEnum.AutoWarningType_1.getCode().equals(autoWarning.getType())) {
                // 客流120分钟
                autoExpire = 120;
                expireString = "2小时";
            } else if (AutoWarningTypeEnum.AutoWarningType_3.getCode().equals(autoWarning.getType())) {
                autoExpire = 30;
                expireString = "30分钟";
            } else if (AutoWarningTypeEnum.AutoWarningType_2.getCode().equals(autoWarning.getType())) {
                // TODO
            } else {
                continue;
            }

            LocalDateTime warningTime = DateUtil.longToLocalDateTime(autoWarning.getWarningTime().getTime());
            if (warningTime.plusMinutes(autoExpire).isBefore(now)) {
                warningAutoExpireUpdateStatus(autoWarning, expireString);
            }
        }

    }

    /**
     * 修改过期状态并记录日志
     *
     * @param autoWarning  autoWarning
     * @param expireString expireString
     */
    private void warningAutoExpireUpdateStatus(AutoWarning autoWarning, String expireString) {
        autoWarning.setStatus(AutoWarningStatusEnum.WARN_CODE_033004.getCode());
        autoWarning.setUpdateTime(new Date());
        baseMapper.updateById(autoWarning);
        // 记录操作日志
        AutoWarningRecord record = new AutoWarningRecord();
        String Content = "预警已超过" + expireString + "未处理, 已自动过期";
        record.setWarningId(autoWarning.getId());
        record.setHandler("admin");
        record.setHandleType(Integer.valueOf(AutoWarningEnum.HANDLE_TYPE_5.getCode()));
        record.setContent(Content);
        iAutoWarningRecordService.save(record);
    }

    private void queryOperateRecord(Long warningId, WarningRecordDetailVO warningRecordDetailVO) {

        List<AutoWarningRecord> list = iAutoWarningRecordService.queryByWarningId(warningId, null);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<WarningRecordDetailVO.RecordVO> records = list.stream().map(record -> {
            WarningRecordDetailVO.RecordVO recordVO = new WarningRecordDetailVO.RecordVO();
            BeanUtils.copyProperties(record, recordVO);
            // receiver
            handleReceiver(record.getId(), recordVO);
            // attaches
            handleAttaches(record.getId(), recordVO);
            return recordVO;

        }).collect(Collectors.toList());
        warningRecordDetailVO.setRecords(records);
    }

    private void handleAttaches(Long recordId, WarningRecordDetailVO.RecordVO recordVO) {
        List<AutoWarningRecordAttach> list = iAutoWarningRecordAttachService.selectByRecordId(recordId);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<AutoWarningCondition.Attach> attaches = list.stream().map(autoWarningRecordAttach -> {
            AutoWarningCondition.Attach attachVO = new AutoWarningCondition.Attach();
            attachVO.setName(autoWarningRecordAttach.getAttachName());
            attachVO.setUrl(autoWarningRecordAttach.getAttachUrl());
            return attachVO;
        }).collect(Collectors.toList());
        recordVO.setAttaches(attaches);
    }

    private void handleReceiver(Long recordId, WarningRecordDetailVO.RecordVO recordVO) {
        List<AutoWarningRecordReceiver> list = iAutoWarningRecordReceiverService.selectByRecordId(recordId);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        String receiverNames = list.stream().map(AutoWarningRecordReceiver::getReceiverName).collect(Collectors.joining(","));
        recordVO.setReceiver(receiverNames);
    }


    /**
     * 判断当前状态是否可以进行当前操作
     *
     * @param currentStatus  当前预警状态
     * @param currentOperate 当前操作
     * @return 是否可以继续执行 true 可以 信息屏发布或者短信发布
     */
    private boolean checkNextOperateIsLegal(String currentStatus, AutoWarningOperateEnum currentOperate) {
        for (AutoWarningStatusCheckEnum statusCheckEnum : AutoWarningStatusCheckEnum.values()) {
            if (currentStatus.equals(statusCheckEnum.getCurrentStatus())) {
                AutoWarningOperateEnum[] ableOperates = statusCheckEnum.getAbleOperates();
                if (Arrays.asList(ableOperates).contains(currentOperate)) {
                    return true;
                }
            }
        }
        return false;
    }

    private AutoWarningRecord readyAutoWarningRecord(Long warningId, Integer handleType, String content, String programName, String messageChannel, String programContent) {
        AutoWarningRecord record = new AutoWarningRecord();
        record.setContent(content);
        record.setHandleType(handleType);
        record.setWarningId(warningId);
        record.setProgramName(programName == null ? "" : programName);
        record.setMessageChannel(messageChannel == null ? "" : messageChannel);
        record.setHandler(CustomContext.getTokenInfo().getUser().getUserName());
        record.setProgramContent(programContent);
        return record;
    }

    private void readyAutoWarningRecordReceivers(Long recordId, Integer handleType, Set<AutoWarningCondition.Receiver> receivers) {
        if (CollectionUtils.isEmpty(receivers)) {
            return;
        }
        Set<AutoWarningRecordReceiver> autoWarningRecordReceivers = receivers.stream().map(receiver -> {
            AutoWarningRecordReceiver recordReceiver = new AutoWarningRecordReceiver();
            recordReceiver.setRecordId(recordId);
            recordReceiver.setType(handleType);
            recordReceiver.setReceiverId(receiver.getId());
            recordReceiver.setReceiverName(receiver.getName());
            return recordReceiver;
        }).collect(Collectors.toSet());
        boolean bool = iAutoWarningRecordReceiverService.saveBatch(autoWarningRecordReceivers);
        log.info("批量保存接受者信息 >>>>> :{}", bool);
    }

    private void readyAutoWarningRecordAttaches(Long recordId, Set<AutoWarningCondition.Attach> attaches) {
        if (CollectionUtils.isEmpty(attaches)) {
            return;
        }
        Set<AutoWarningRecordAttach> attachSet = attaches.stream().map(attach -> {
            AutoWarningRecordAttach autoWarningRecordAttach = new AutoWarningRecordAttach();
            autoWarningRecordAttach.setRecordId(recordId);
            autoWarningRecordAttach.setAttachName(attach.getName());
            autoWarningRecordAttach.setAttachUrl(attach.getUrl());
            return autoWarningRecordAttach;
        }).collect(Collectors.toSet());
        boolean bool = iAutoWarningRecordAttachService.saveBatch(attachSet);
        log.info("批量保存附件信息 >>>>> :{}", bool);
    }

}
