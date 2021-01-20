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
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyEvent;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyEventAttachment;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyPlan;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum.EmergencyEventAttachmentTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum.EmergencyEventCheckStatusEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum.EmergencyEventStatusEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.EmergencyEventMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyEventAttachmentService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyEventKeywordService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyEventService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyPlanService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.UserVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.MenuService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.UserService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.feign.SsoFeignService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import net.cdsunrise.hy.sso.starter.domain.UserResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2019/11/27 17:22
 */
@Service
public class EmergencyEventServiceImpl extends ServiceImpl<EmergencyEventMapper, EmergencyEvent> implements IEmergencyEventService {

    /**
     * 反馈事件情况的接口的权限标识
     */
    private static final String PERMISSION_EMERGENCY_EVENT_FEEDBACK = "emergency-event:feedback";
    private static final String EVENT_TYPE_PARENT_CODE = "EMERGENCY_EVENT_TYPE";
    private static final String EVENT_LEVEL_PARENT_CODE = "EMERGENCY_EVENT_LEVEL";
    private static final List<String> DISALLOW_EVENT_TYPE = Arrays.asList("014006", "014007");
    private static final List<String> ORDER_COLUMNS = Arrays.asList("event_type", "event_level", "event_time", "event_status", "gmt_create");

    private final IEmergencyEventAttachmentService emergencyEventAttachmentService;
    private final IEmergencyEventKeywordService emergencyEventKeywordService;
    private final IEmergencyPlanService emergencyPlanService;
    private final DataDictionaryFeignClient dataDictionaryFeignClient;
    private final UserService userService;
    private final MenuService menuService;
    private final SsoFeignService ssoFeignService;

    public EmergencyEventServiceImpl(IEmergencyEventAttachmentService emergencyEventAttachmentService, IEmergencyEventKeywordService emergencyEventKeywordService, IEmergencyPlanService emergencyPlanService, DataDictionaryFeignClient dataDictionaryFeignClient, UserService userService, MenuService menuService, SsoFeignService ssoFeignService) {
        this.emergencyEventAttachmentService = emergencyEventAttachmentService;
        this.emergencyEventKeywordService = emergencyEventKeywordService;
        this.emergencyPlanService = emergencyPlanService;
        this.dataDictionaryFeignClient = dataDictionaryFeignClient;
        this.userService = userService;
        this.menuService = menuService;
        this.ssoFeignService = ssoFeignService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveEmergencyEvent(EmergencyEventAddVO emergencyEventAddVO) {
        return saveEmergencyEvent(Boolean.FALSE, EmergencyEventStatusEnum.WAIT_CHECK, emergencyEventAddVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveSystemEmergencyEvent(EmergencyEventAddVO emergencyEventAddVO) {
        if (!DISALLOW_EVENT_TYPE.contains(emergencyEventAddVO.getEventType())) {
            throw new ParamErrorException("只能新增交通拥堵事件或客流预警事件");
        }
        return saveEmergencyEvent(Boolean.TRUE, EmergencyEventStatusEnum.CLOSED, emergencyEventAddVO);
    }

    private Long saveEmergencyEvent(Boolean isAutoCreated, EmergencyEventStatusEnum emergencyEventStatusEnum, EmergencyEventAddVO emergencyEventAddVO) {
        // 基础信息
        Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel = getAllEmergencyEventTypeAndLevel();
        checkEventTypeAndLevel(allEmergencyEventTypeAndLevel, emergencyEventAddVO.getEventType(), emergencyEventAddVO.getEventLevel());
        EmergencyEvent emergencyEvent = new EmergencyEvent();
        BeanUtils.copyProperties(emergencyEventAddVO, emergencyEvent);
        // 事件状态
        emergencyEvent.setEventStatus(emergencyEventStatusEnum);
        emergencyEvent.setAutoCreated(isAutoCreated);
        // 存储
        super.save(emergencyEvent);
        // 关键词
        emergencyEventKeywordService.save(emergencyEvent.getId(), emergencyEventAddVO.getKeywords());
        // 附件
        List<EmergencyEventAttachment> scenePhotos = getAttachments(emergencyEvent.getId(), emergencyEventAddVO.getScenePhotos(), EmergencyEventAttachmentTypeEnum.EVENT_IMAGE);
        List<EmergencyEventAttachment> attachments = getAttachments(emergencyEvent.getId(), emergencyEventAddVO.getAttachments(), EmergencyEventAttachmentTypeEnum.EVENT_FILE);
        if (!CollectionUtils.isEmpty(scenePhotos)) {
            emergencyEventAttachmentService.saveBatch(scenePhotos);
        }
        if (!CollectionUtils.isEmpty(attachments)) {
            emergencyEventAttachmentService.saveBatch(attachments);
        }

        return emergencyEvent.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editEmergencyEvent(EmergencyEventEditVO emergencyEventEditVO) {
        EmergencyEvent emergencyEvent = super.getById(emergencyEventEditVO.getId());
        if (Objects.isNull(emergencyEvent)) {
            throw new ParamErrorException("指定ID事件不存在");
        }
        // 只能编辑状态为待审核的事件
        if (!EmergencyEventStatusEnum.WAIT_CHECK.equals(emergencyEvent.getEventStatus())) {
            throw new ParamErrorException("只能编辑待审批事件");
        }
        // 基础信息
        Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel = getAllEmergencyEventTypeAndLevel();
        checkEventTypeAndLevel(allEmergencyEventTypeAndLevel, emergencyEventEditVO.getEventType(), emergencyEventEditVO.getEventLevel());
        BeanUtils.copyProperties(emergencyEventEditVO, emergencyEvent);

        // 更新
        super.updateById(emergencyEvent);
        // 删除旧关键词
        emergencyEventKeywordService.delete(emergencyEvent.getId());
        // 重新存入关键词
        emergencyEventKeywordService.save(emergencyEvent.getId(), emergencyEventEditVO.getKeywords());
        // 附件
        // 删除所有旧附件
        QueryWrapper<EmergencyEventAttachment> queryWrapper = Wrappers.query();
        queryWrapper.lambda()
                .eq(EmergencyEventAttachment::getEventId, emergencyEvent.getId());
        emergencyEventAttachmentService.remove(queryWrapper);
        // 插入新附件
        List<EmergencyEventAttachment> scenePhotos = getAttachments(emergencyEvent.getId(), emergencyEventEditVO.getScenePhotos(), EmergencyEventAttachmentTypeEnum.EVENT_IMAGE);
        List<EmergencyEventAttachment> attachments = getAttachments(emergencyEvent.getId(), emergencyEventEditVO.getAttachments(), EmergencyEventAttachmentTypeEnum.EVENT_FILE);
        if (!CollectionUtils.isEmpty(scenePhotos)) {
            emergencyEventAttachmentService.saveBatch(scenePhotos);
        }
        if (!CollectionUtils.isEmpty(attachments)) {
            emergencyEventAttachmentService.saveBatch(attachments);
        }
    }

    @Override
    public boolean checkEmergencyEvent(EmergencyEventCheckVO emergencyEventCheckVO) {
        EmergencyEvent emergencyEvent = super.getById(emergencyEventCheckVO.getId());
        if (emergencyEvent == null) {
            throw new ParamErrorException("指定ID事件不存在");
        }
        EmergencyEventStatusEnum eventStatus = emergencyEvent.getEventStatus();
        if (eventStatus != EmergencyEventStatusEnum.WAIT_CHECK) {
            throw new ParamErrorException("事件不是待审核状态");
        }
        Boolean checkFlag = emergencyEventCheckVO.getCheckFlag();
        emergencyEvent.setCheckUserId(CustomContext.getTokenInfo().getUser().getId());
        emergencyEvent.setCheckTime(new Date());
        // 待处理或未通过
        emergencyEvent.setEventStatus(checkFlag ? EmergencyEventStatusEnum.WAIT_DEAL : EmergencyEventStatusEnum.NO_PASS);
        emergencyEvent.setCheckStatus(checkFlag ? EmergencyEventCheckStatusEnum.PASS : EmergencyEventCheckStatusEnum.NO_PASS);
        emergencyEvent.setCheckContent(emergencyEventCheckVO.getCheckContent());

        return super.updateById(emergencyEvent);
    }

    @Override
    public List<UserVO> listAllCanAssignUser() {
        return userService.findByPermission(PERMISSION_EMERGENCY_EVENT_FEEDBACK);
    }

    @Override
    public boolean assignEmergencyEvent(EmergencyEventAssignVO emergencyEventAssignVO) {
        EmergencyEvent emergencyEvent = super.getById(emergencyEventAssignVO.getId());
        if (emergencyEvent == null) {
            throw new ParamErrorException("指定ID事件不存在");
        }
        EmergencyEventStatusEnum eventStatus = emergencyEvent.getEventStatus();
        if (eventStatus != EmergencyEventStatusEnum.WAIT_DEAL) {
            throw new ParamErrorException("事件不是待处理状态");
        }
        Long assignedUserId = emergencyEventAssignVO.getAssignedUserId();
        if (!menuService.userHasPermission(assignedUserId, PERMISSION_EMERGENCY_EVENT_FEEDBACK)) {
            throw new ParamErrorException("被指派人员无对应处理权限");
        }
        // 待结案
        emergencyEvent.setEventStatus(EmergencyEventStatusEnum.WAIT_CLOSE);
        emergencyEvent.setAssignedUserId(assignedUserId);
        emergencyEvent.setAssignerUserId(CustomContext.getTokenInfo().getUser().getId());
        emergencyEvent.setAssignTime(new Date());

        return super.updateById(emergencyEvent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean feedbackEmergencyEvent(EmergencyEventFeedbackVO emergencyEventFeedbackVO) {
        EmergencyEvent emergencyEvent = super.getById(emergencyEventFeedbackVO.getId());
        if (emergencyEvent == null) {
            throw new ParamErrorException("指定ID事件不存在");
        }
        EmergencyEventStatusEnum eventStatus = emergencyEvent.getEventStatus();
        if (eventStatus != EmergencyEventStatusEnum.WAIT_CLOSE) {
            throw new ParamErrorException("事件不是待结案状态");
        }
        Long currentUserId = CustomContext.getTokenInfo().getUser().getId();
        if (!emergencyEvent.getAssignedUserId().equals(currentUserId)) {
            throw new ParamErrorException("只能由被指派人员反馈");
        }
        emergencyEvent.setFeedbackContent(emergencyEventFeedbackVO.getFeedbackContent());
        emergencyEvent.setFeedbackTime(new Date());
        boolean success = super.updateById(emergencyEvent);
        // 附件
        List<EmergencyEventAttachment> feedbackPhotos = getAttachments(emergencyEvent.getId(), emergencyEventFeedbackVO.getFeedbackPhotos(), EmergencyEventAttachmentTypeEnum.FEEDBACK_IMAGE);
        // 删除旧附件，多次反馈视为编辑
        QueryWrapper<EmergencyEventAttachment> queryWrapper = Wrappers.query();
        queryWrapper.lambda()
                .eq(EmergencyEventAttachment::getEventId, emergencyEventFeedbackVO.getId())
                .eq(EmergencyEventAttachment::getType, EmergencyEventAttachmentTypeEnum.FEEDBACK_IMAGE);
        emergencyEventAttachmentService.remove(queryWrapper);
        if (!CollectionUtils.isEmpty(feedbackPhotos)) {
            emergencyEventAttachmentService.saveBatch(feedbackPhotos);
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeEmergencyEvent(EmergencyEventCloseVO emergencyEventCloseVO) {
        EmergencyEvent emergencyEvent = super.getById(emergencyEventCloseVO.getId());
        if (emergencyEvent == null) {
            throw new ParamErrorException("指定ID事件不存在");
        }
        EmergencyEventStatusEnum eventStatus = emergencyEvent.getEventStatus();
        if (eventStatus != EmergencyEventStatusEnum.WAIT_DEAL && eventStatus != EmergencyEventStatusEnum.WAIT_CLOSE) {
            throw new ParamErrorException("事件不是待处理或待结案状态");
        }
        // 已结案
        emergencyEvent.setEventStatus(EmergencyEventStatusEnum.CLOSED);
        emergencyEvent.setCloseContent(emergencyEventCloseVO.getCloseContent());
        emergencyEvent.setCloseTime(new Date());
        emergencyEvent.setCloseUserId(CustomContext.getTokenInfo().getUser().getId());
        boolean success = super.updateById(emergencyEvent);
        // 附件
        List<EmergencyEventAttachment> closeAttachment = getAttachments(emergencyEvent.getId(), emergencyEventCloseVO.getCloseAttachments(), EmergencyEventAttachmentTypeEnum.CLOSE_FILE);
        if (!CollectionUtils.isEmpty(closeAttachment)) {
            emergencyEventAttachmentService.saveBatch(closeAttachment);

        }
        // 结案图片
        List<EmergencyEventAttachment> closeImages = getAttachments(emergencyEvent.getId(), emergencyEventCloseVO.getCloseImages(), EmergencyEventAttachmentTypeEnum.CLOSE_IMAGE);
        if (!CollectionUtils.isEmpty(closeImages)) {
            emergencyEventAttachmentService.saveBatch(closeImages);
        }

        return success;
    }

    @Override
    public EmergencyEventVO getEmergencyEvent(Long id) {
        if (id == null) {
            throw new ParamErrorException("事件ID不能为空");
        }
        // 基础信息
        EmergencyEvent emergencyEvent = super.getById(id);
        if (emergencyEvent == null) {
            throw new ParamErrorException("事件不存在");
        }
        // 关键词
        Set<String> keywords = emergencyEventKeywordService.get(emergencyEvent.getId());
        // 数据字典
        Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel = getAllEmergencyEventTypeAndLevel();
        return convertToVO(allEmergencyEventTypeAndLevel, emergencyEvent, keywords);
    }

    @Override
    public IPage<EmergencyEventVO> listEmergencyEvent(PageRequest<EmergencyEventCondition> pageRequest) {
        // 分页
        Page<EmergencyEvent> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<EmergencyEvent> queryWrapper = Wrappers.query();
        EmergencyEventCondition condition = pageRequest.getCondition();
        //筛选
        if (condition != null) {
            queryWrapper.lambda()
                    .like(StringUtils.hasText(condition.getEventName()), EmergencyEvent::getEventName, condition.getEventName())
                    .eq(StringUtils.hasText(condition.getEventType()), EmergencyEvent::getEventType, condition.getEventType())
                    .eq(StringUtils.hasText(condition.getEventLevel()), EmergencyEvent::getEventLevel, condition.getEventLevel())
                    .eq(condition.getEventStatus() != null, EmergencyEvent::getEventStatus, condition.getEventStatus())
                    .eq(condition.getAssignedUserId() != null, EmergencyEvent::getAssignedUserId, condition.getAssignedUserId());
        }
        //排序
        List<PageRequest.OrderItem> orderItemList = pageRequest.getOrderItemList();
        if (!CollectionUtils.isEmpty(orderItemList)) {
            PageUtil.setOrders(queryWrapper, ORDER_COLUMNS, orderItemList);
        } else {
            // 默认事件状态排序
            queryWrapper.lambda().orderByAsc(EmergencyEvent::getEventStatus);
        }

        super.page(page, queryWrapper);
        // 关键词
        Set<Long> ids = page.getRecords().stream().map(EmergencyEvent::getId).collect(Collectors.toSet());
        Map<Long, Set<String>> keywordMap = emergencyEventKeywordService.getMap(ids);
        // 数据字典
        Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel = getAllEmergencyEventTypeAndLevel();
        return page.convert(emergencyEvent -> convertToVO(allEmergencyEventTypeAndLevel, emergencyEvent, keywordMap.getOrDefault(emergencyEvent.getId(), new HashSet<>(0))));
    }

    @Override
    public void deleteEmergencyEvent(Long id) {
        // 删除基础信息
        super.removeById(id);
        // 删除附件
        QueryWrapper<EmergencyEventAttachment> queryWrapper = Wrappers.query();
        queryWrapper.lambda()
                .eq(EmergencyEventAttachment::getEventId, id);
        emergencyEventAttachmentService.remove(queryWrapper);
    }

    @Override
    public List<EmergencyEventEvaluationVO> evaluate(Long id) {
        // 查询事件
        LambdaQueryWrapper<EmergencyEvent> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(EmergencyEvent::getEventType, EmergencyEvent::getEventLevel)
                .eq(EmergencyEvent::getId, id);
        EmergencyEvent emergencyEvent = getOne(queryWrapper);
        if (Objects.isNull(emergencyEvent)) {
            throw new ParamErrorException("指定ID的应急事件不存在");
        }
        // 查询对应事件类型的应急预案
        List<EmergencyPlan> emergencyPlans = emergencyPlanService.listForEvaluate(emergencyEvent.getEventType());
        if (CollectionUtils.isEmpty(emergencyPlans)) {
            return new ArrayList<>();
        }
        // 查询事件关键词
        Set<String> eventKeywords = emergencyEventKeywordService.get(id);
        // 查询预案关键词
        Set<Long> emergencyPlanIds = emergencyPlans.stream().map(EmergencyPlan::getId).collect(Collectors.toSet());
        Map<Long, Set<String>> planKeywordMap = emergencyPlanService.getKeywordMap(emergencyPlanIds);
        // 开始评估
        return emergencyPlans.stream().map(emergencyPlan -> {
            // 事件类型匹配，则占比20%
            BigDecimal fitRate = new BigDecimal("0.2").setScale(4, RoundingMode.HALF_UP);
            // 事件等级匹配，则占比20%
            if (emergencyEvent.getEventLevel().equals(emergencyPlan.getEventLevel())) {
                fitRate = fitRate.add(new BigDecimal("0.2"));
            }
            // 计算关键词定义占比=预案匹配的关键词/事件关键词总数*60%
            // 关键词匹配是指文本内容一致；若均不一致，则为0
            Set<String> planKeywords = planKeywordMap.get(emergencyPlan.getId());
            if (!CollectionUtils.isEmpty(eventKeywords) && !CollectionUtils.isEmpty(planKeywords)) {
                long keywordsFitCount = planKeywords.stream().filter(eventKeywords::contains).count();
                BigDecimal keywordsFitRate = BigDecimal.valueOf(keywordsFitCount).divide(BigDecimal.valueOf(eventKeywords.size()), 4, RoundingMode.HALF_UP);
                fitRate = fitRate.add(keywordsFitRate.multiply(new BigDecimal("0.6")));
            }

            EmergencyEventEvaluationVO emergencyEventEvaluationVO = new EmergencyEventEvaluationVO();
            emergencyEventEvaluationVO.setEmergencyPlanId(emergencyPlan.getId());
            emergencyEventEvaluationVO.setEmergencyPlanName(emergencyPlan.getPlanName());
            emergencyEventEvaluationVO.setFitRate(fitRate);
            return emergencyEventEvaluationVO;
        }).sorted(Comparator.comparing(EmergencyEventEvaluationVO::getFitRate).reversed()).collect(Collectors.toList());
    }


    private String getUserName(Long userId) {
        if (userId == null) {
            return null;
        }
        // 获取修改人名称
        UserResp userResp = userService.getUserById(userId);
        if (userResp != null) {
            return userResp.getUserName();
        }
        return null;
    }

    private EmergencyEventVO convertToVO(Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel, EmergencyEvent emergencyEvent, Set<String> keywords) {
        EmergencyEventVO emergencyEventVO = new EmergencyEventVO();
        BeanUtils.copyProperties(emergencyEvent, emergencyEventVO);
        DataDictionaryVO eventType = getEventType(allEmergencyEventTypeAndLevel, emergencyEvent.getEventType());
        if (eventType != null) {
            emergencyEventVO.setEventTypeDesc(eventType.getName());
        }
        DataDictionaryVO eventLevel = getEventLevel(allEmergencyEventTypeAndLevel, emergencyEvent.getEventLevel());
        if (eventLevel != null) {
            emergencyEventVO.setEventLevelDesc(eventLevel.getName());
        }
        // 关键词
        emergencyEventVO.setKeywords(keywords);
        // 现场照片
        List<EmergencyEventVO.Attachment> scenePhotos = getAttachmentVos(emergencyEvent, EmergencyEventAttachmentTypeEnum.EVENT_IMAGE);
        emergencyEventVO.setScenePhotos(scenePhotos);
        // 资料附件
        List<EmergencyEventVO.Attachment> attachments = getAttachmentVos(emergencyEvent, EmergencyEventAttachmentTypeEnum.EVENT_FILE);
        emergencyEventVO.setAttachments(attachments);
        // 事件状态
        emergencyEventVO.setEventStatusDesc(emergencyEventVO.getEventStatus().getDesc());
        // 审核状态
        EmergencyEventCheckStatusEnum checkStatus = emergencyEventVO.getCheckStatus();
        if (checkStatus != null) {
            emergencyEventVO.setCheckStatusDesc(checkStatus.getDesc());
        }
        // 获取用户名
        Long checkUserId = emergencyEventVO.getCheckUserId() == null ? 0 : emergencyEventVO.getCheckUserId();
        Long assignedUserId = emergencyEventVO.getAssignedUserId() == null ? 0 : emergencyEventVO.getAssignedUserId();
        Long assignerUserId = emergencyEventVO.getAssignerUserId() == null ? 0 : emergencyEventVO.getAssignerUserId();
        Long closeUserId = emergencyEventVO.getCloseUserId() == null ? 0 : emergencyEventVO.getCloseUserId();
        List<Long> ids = Arrays.asList(checkUserId, assignedUserId, assignerUserId, closeUserId);
        List<UserVO> userInfoList = userList(ids);

        // 审核人员名称
        emergencyEventVO.setCheckUserName(getUserNameByIdInList(userInfoList, checkUserId));
        // 被指派人员名称
        emergencyEventVO.setAssignedUserName(getUserNameByIdInList(userInfoList, assignedUserId));
        // 指派人名称
        emergencyEventVO.setAssignerUserName(getUserNameByIdInList(userInfoList, assignerUserId));
        // 结案人名称
        emergencyEventVO.setCloseUserName(getUserNameByIdInList(userInfoList, closeUserId));
        // 反馈现场照片
        List<EmergencyEventVO.Attachment> feedbackPhotos = getAttachmentVos(emergencyEvent, EmergencyEventAttachmentTypeEnum.FEEDBACK_IMAGE);
        emergencyEventVO.setFeedbackPhotos(feedbackPhotos);
        // 结案附件
        List<EmergencyEventVO.Attachment> closeAttachments = getAttachmentVos(emergencyEvent, EmergencyEventAttachmentTypeEnum.CLOSE_FILE);
        emergencyEventVO.setCloseAttachments(closeAttachments);
        // 结案图片
        List<EmergencyEventVO.Attachment> closeImages = getAttachmentVos(emergencyEvent, EmergencyEventAttachmentTypeEnum.CLOSE_IMAGE);
        emergencyEventVO.setCloseImages(closeImages);
        return emergencyEventVO;
    }

    private List<UserVO> userList(List<Long> ids) {
        net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.req.PageRequest pageRequest =
                new net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.req.PageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(ids.size());
        pageRequest.setUserIds(ids);
        Result<Page<UserVO>> result = ssoFeignService.userList(pageRequest);
        if (result.getSuccess()) {
            return result.getData().getRecords();
        } else {
            return new ArrayList<UserVO>();
        }
    }

    private String getUserNameByIdInList(List<UserVO> userList, long id) {
        if (CollectionUtils.isEmpty(userList)) {
            return "";
        }
        return userList.stream().filter(userVO -> userVO.getId().equals(id)).findFirst().map(UserVO::getUserName).orElse("");
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

    private void checkEventTypeAndLevel(Map<String, Map<String, DataDictionaryVO>> map, String eventType, String eventLevel) {
        checkEventType(map, eventType);
        checkEventLevel(map, eventLevel);
    }

    private List<EmergencyEventAttachment> getAttachments(Long eventId, List<EmergencyEventVO.Attachment> attachments, EmergencyEventAttachmentTypeEnum type) {
        if (CollectionUtils.isEmpty(attachments)) {
            return null;
        }
        return attachments.stream().map(attachment -> {
            EmergencyEventAttachment emergencyEventAttachment = new EmergencyEventAttachment();
            BeanUtils.copyProperties(attachment, emergencyEventAttachment, "id");
            emergencyEventAttachment.setEventId(eventId);
            emergencyEventAttachment.setType(type);
            return emergencyEventAttachment;
        }).collect(Collectors.toList());
    }

    private List<EmergencyEventVO.Attachment> getAttachmentVos(EmergencyEvent emergencyEvent, EmergencyEventAttachmentTypeEnum type) {
        QueryWrapper<EmergencyEventAttachment> queryWrapper = Wrappers.query();
        queryWrapper.lambda()
                .eq(EmergencyEventAttachment::getEventId, emergencyEvent.getId())
                .eq(EmergencyEventAttachment::getType, type);
        List<EmergencyEventAttachment> emergencyEventAttachments = emergencyEventAttachmentService.list(queryWrapper);
        if (CollectionUtils.isEmpty(emergencyEventAttachments)) {
            return null;
        }
        return emergencyEventAttachments.stream().map(emergencyEventAttachment -> {
            EmergencyEventVO.Attachment attachment = new EmergencyEventVO.Attachment();
            BeanUtils.copyProperties(emergencyEventAttachment, attachment);
            return attachment;
        }).collect(Collectors.toList());
    }
}
