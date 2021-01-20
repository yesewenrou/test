package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningContactConfig;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.TouristNumNewest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristPassengerTicket;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.AutoWarningTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.MessageMenuEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.AutoWarningConditionMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.TouristNumNewestMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated.TouristLocalData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.mapper.RoleMenuMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.AutoWarningDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ScenicTouristCapacityConfigVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TouristNumNewestVO;
import net.cdsunrise.hy.lyxxfb.iip.autoconfigure.feign.SmsFeignClient;
import net.cdsunrise.hy.lyxxfb.iip.autoconfigure.feign.req.SmsFeignRequest;
import net.cdsunrise.hy.message.center.autoconfigure.feign.MessageCenterFeignClient;
import net.cdsunrise.hy.message.center.autoconfigure.feign.enums.AppEnum;
import net.cdsunrise.hy.message.center.autoconfigure.feign.enums.CategoryEnum;
import net.cdsunrise.hy.message.center.autoconfigure.feign.req.MessageRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2019/9/29 16:16
 */
@Slf4j
@Service
public class TouristNumNewestServiceImpl extends ServiceImpl<TouristNumNewestMapper, TouristNumNewest> implements ITouristNumNewestService {

    private MessageCenterFeignClient messageCenterFeignClient;
    private DataDictionaryFeignClient dataDictionaryFeignClient;
    private IScenicTouristCapacityConfigService scenicTouristCapacityConfigService;
    private RoleMenuMapper roleMenuMapper;

    private final IAutoWarningConditionService iAutoWarningConditionService;

    private final AutoWarningConditionMapper autoWarningConditionMapper;

    private final IAutoWarningService iAutoWarningService;
    private final ISmsContactService iSmsContactService;
    private final SmsFeignClient smsFeignClient;
    /**
     * 周边游客
     **/
    private static final List<String> PEOPLE_IN_LOCAL = Arrays.asList("七里坪", "柳江古镇", "槽渔滩", "主城区");
    /**
     * 园内游客
     **/
    private static final List<String> PEOPLE_IN_PARK = Arrays.asList("玉屏山", "瓦屋山");

    public TouristNumNewestServiceImpl(MessageCenterFeignClient messageCenterFeignClient,
                                       DataDictionaryFeignClient dataDictionaryFeignClient,
                                       IScenicTouristCapacityConfigService scenicTouristCapacityConfigService,
                                       RoleMenuMapper roleMenuMapper,
                                       IAutoWarningConditionService iAutoWarningConditionService,
                                       AutoWarningConditionMapper autoWarningConditionMapper,
                                       IAutoWarningService iAutoWarningService, ISmsContactService iSmsContactService, SmsFeignClient smsFeignClient) {
        this.messageCenterFeignClient = messageCenterFeignClient;
        this.dataDictionaryFeignClient = dataDictionaryFeignClient;
        this.scenicTouristCapacityConfigService = scenicTouristCapacityConfigService;
        this.roleMenuMapper = roleMenuMapper;
        this.iAutoWarningConditionService = iAutoWarningConditionService;
        this.autoWarningConditionMapper = autoWarningConditionMapper;
        this.iAutoWarningService = iAutoWarningService;
        this.iSmsContactService = iSmsContactService;
        this.smsFeignClient = smsFeignClient;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void dealTouristLocalOperated(TouristLocalData touristLocalData) {
        TouristNumNewest touristNumNewest = new TouristNumNewest();
        BeanUtils.copyProperties(touristLocalData, touristNumNewest);
        QueryWrapper<TouristNumNewest> queryWrapper = Wrappers.query();
        queryWrapper.lambda()
                .eq(TouristNumNewest::getScenicId, touristNumNewest.getScenicId())
                .eq(TouristNumNewest::getMemberType, touristNumNewest.getMemberType());
        TouristNumNewest old = super.getOne(queryWrapper);
        if (old != null) {
            touristNumNewest.setId(old.getId());
            super.updateById(touristNumNewest);
        } else {
            super.save(touristNumNewest);
        }

        try {
            // 调用数据字典获取所有景区
            String code = "SCENIC_AREA";
            Result<Map<String, DataDictionaryVO>> result = dataDictionaryFeignClient.getByCodes(new String[]{code});
            Map<String, DataDictionaryVO> data = result.getData();
            DataDictionaryVO dataDictionaryVO = data.get(code);
            List<DataDictionaryVO> children = dataDictionaryVO.getChildren();
            DataDictionaryVO scenic = children.stream().filter(item -> item.getName().equals(touristLocalData.getScenicName())).findFirst().orElse(null);
            if (scenic != null && checkScenicNameLocalPeople(scenic.getName())) {
                List<ScenicTouristCapacityConfigVO> scenicTouristCapacityConfigVos = scenicTouristCapacityConfigService.listAllConfig();
                ScenicTouristCapacityConfigVO scenicTouristCapacityConfigVO = scenicTouristCapacityConfigVos.stream().filter(item -> item.getScenicCode().equals(scenic.getCode())).findFirst().orElse(null);
                if (scenicTouristCapacityConfigVO == null) {
                    return;
                }

                Integer warningCapacity = scenicTouristCapacityConfigVO.getWarningCapacity();
                Integer peopleNum = touristLocalData.getPeopleNum();
                if (peopleNum.compareTo(warningCapacity) >= 0) {
                    if (checkIsNeedToWarning(scenic.getName(), warningCapacity, peopleNum)) {
                        log.info("景区告警, 景区名称:{}, 游客数: {}, 预警配置人数:{}", scenic.getName(), peopleNum, warningCapacity);
                        // 推送消息
                        try {
                            MessageRequest.AddReq addReq = getAddReq(scenic.getName(), MessageMenuEnum.OPERATION_SCENIC_TOURISTS_OVERLOAD, peopleNum, warningCapacity);
                            messageCenterFeignClient.add(addReq);
                        } catch (Exception e) {
                            log.error("推送站内告警消息失败", e);
                        }
                        // 保存到预警表
                        AutoWarningDTO.ScenicWarning scenicWarning = new AutoWarningDTO.ScenicWarning();
                        scenicWarning.setType(AutoWarningTypeEnum.AutoWarningType_1.getCode());
                        scenicWarning.setPeopleNum(peopleNum);
                        scenicWarning.setObject(scenic.getName());
                        scenicWarning.setWarningTime(new Date());
                        iAutoWarningService.saveScenicAutoWarning(scenicWarning);

                        // 自动发送短信
                        autoSendSMS(scenic.getName());
                    }
                }
            }
        } catch (Exception e) {
            log.error("推送景区负荷消息失败", e);
        }
    }


    /**
     * 当实时游客数 连续两次超出 预警数 时，生成告警，且2小时内，不再重复告警。
     *
     * @param scenicName      景区名称
     * @param warningCapacity 预警值
     * @param peopleNum       当前值
     * @return 是否需要预警
     */
    private boolean checkIsNeedToWarning(String scenicName, Integer warningCapacity, Integer peopleNum) {
        if (warningCapacity > peopleNum) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        Date dateNow = new Date();
        AutoWarningCondition condition = autoWarningConditionMapper.selectByScenicName(scenicName);
        // 如果已经告警
        if (condition != null) {
            Integer alertCount = condition.getAlertCount();
            LocalDateTime lastWarning = DateUtil.longToLocalDateTime(condition.getAlertTime().getTime());
            // 如果告警次数为1
            if (alertCount.equals(1)) {
                // 如果距上次告警未超过两小时, 则进行告警
                if (lastWarning.plusHours(2).isAfter(now)) {
                    condition.setAlertCount(2);
                    condition.setAlertTime(dateNow);
                    iAutoWarningConditionService.updateById(condition);
                    return true;
                } else {
                    // 已超过2小时 重置告警时间
                    condition.setAlertTime(dateNow);
                    iAutoWarningConditionService.updateById(condition);
                }
            } else {
                // 如果告警次数为2 说明已经告警了, 则判断是否已经超过两小时
                if (lastWarning.plusHours(2).isBefore(now)) {
                    // 已超过 则将次数置为1
                    condition.setAlertCount(1);
                    condition.setAlertTime(dateNow);
                    iAutoWarningConditionService.updateById(condition);
                }
            }
        } else {
            // 如果未告警 插入告警记录
            condition = new AutoWarningCondition();
            condition.setAlertCount(1);
            condition.setAlertTime(dateNow);
            condition.setScenicName(scenicName);
            iAutoWarningConditionService.save((condition));
            return false;
        }
        return false;
    }


    /**
     * 处理数据
     *
     * @param touristTicketData 数据
     */
    @Override
    @SuppressWarnings("Duplicates")
    public void dealTouristTicketOperated(TouristPassengerTicket touristTicketData) {
        log.info("receiveTouristTicketsData: {}", touristTicketData.toString());
        try {
            // 调用数据字典获取所有景区
            String code = "SCENIC_AREA";
            Result<Map<String, DataDictionaryVO>> result = dataDictionaryFeignClient.getByCodes(new String[]{code});
            Map<String, DataDictionaryVO> data = result.getData();
            DataDictionaryVO dataDictionaryVO = data.get(code);
            List<DataDictionaryVO> children = dataDictionaryVO.getChildren();
            DataDictionaryVO scenic = children.stream().filter(item -> item.getName().equals(touristTicketData.getScenicName())).findFirst().orElse(null);
            if (scenic != null && checkScenicNameParkPeople(scenic.getName())) {
                List<ScenicTouristCapacityConfigVO> scenicTouristCapacityConfigVos = scenicTouristCapacityConfigService.listAllConfig();
                ScenicTouristCapacityConfigVO scenicTouristCapacityConfigVO = scenicTouristCapacityConfigVos.stream().filter(item -> item.getScenicCode().equals(scenic.getCode())).findFirst().orElse(null);
                if (scenicTouristCapacityConfigVO == null) {
                    return;
                }
                Integer warningCapacity = scenicTouristCapacityConfigVO.getWarningCapacity();
                Integer realTimeTouristNum = touristTicketData.getRealTimeTouristNum();
                if (realTimeTouristNum.compareTo(warningCapacity) >= 0) {
                    if (checkIsNeedToWarning(scenic.getName(), warningCapacity, realTimeTouristNum)) {
                        log.info("景区告警, 景区名称:{}, 游客数: {}, 预警配置人数:{}", scenic.getName(), realTimeTouristNum, warningCapacity);
                        // 推送消息
                        try {
                            MessageRequest.AddReq addReq = getAddReq(scenic.getName(), MessageMenuEnum.OPERATION_SCENIC_TOURISTS_OVERLOAD, realTimeTouristNum, warningCapacity);
                            messageCenterFeignClient.add(addReq);
                        } catch (Exception e) {
                            log.error("推送站内告警消息失败", e);
                        }
                        // 保存到数据库
                        AutoWarningDTO.ScenicWarning scenicWarning = new AutoWarningDTO.ScenicWarning();
                        scenicWarning.setType(AutoWarningTypeEnum.AutoWarningType_1.getCode());
                        scenicWarning.setPeopleNum(realTimeTouristNum);
                        scenicWarning.setObject(scenic.getName());
                        scenicWarning.setWarningTime(new Date());
                        iAutoWarningService.saveScenicAutoWarning(scenicWarning);

                        // 自动发送短信
                        autoSendSMS(scenic.getName());
                    }
                }
            }
        } catch (Exception e) {
            log.error("推送景区负荷消息失败", e);
        }
    }

    private void autoSendSMS(String scenicName) {
        List<AutoWarningContactConfig> list = iSmsContactService.getScenicAuto(true);
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> phones = list.stream().map(AutoWarningContactConfig::getPhone).collect(Collectors.toList());
            String content = "当前[" + scenicName + "]客流量较大，请注意诱导游客合理规划出行。";
            SmsFeignRequest req = new SmsFeignRequest();
            req.setMobiles(phones);
            req.setContent(content);
            Result result = smsFeignClient.send(req);
            log.info("短信发送返回结果:{}", result.toString());
        }
    }

    @Override
    public List<TouristNumNewestVO> listTouristNumNewest() {
        QueryWrapper<TouristNumNewest> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(TouristNumNewest::getMemberType, 0);
        List<TouristNumNewest> touristNumNewestList = super.list(queryWrapper);
        List<TouristNumNewestVO> touristNumNewestVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(touristNumNewestList)) {
            touristNumNewestList.forEach(touristNumNewest -> {
                TouristNumNewestVO touristNumNewestVO = new TouristNumNewestVO();
                BeanUtils.copyProperties(touristNumNewest, touristNumNewestVO);
                touristNumNewestVoList.add(touristNumNewestVO);
            });
        }
        return touristNumNewestVoList;
    }

    /**
     * 判断是否是 七里坪 和 柳江古镇
     *
     * @param scenicName 景区名称
     * @return boolean
     */
    private boolean checkScenicNameLocalPeople(String scenicName) {
        return PEOPLE_IN_LOCAL.contains(scenicName);
    }

    /**
     * 判断是否是 瓦屋山 和 玉屏山
     *
     * @param scenicName 景区名称
     * @return boolean
     */
    private boolean checkScenicNameParkPeople(String scenicName) {
        return PEOPLE_IN_PARK.contains(scenicName);
    }

    /**
     * 准备预警推送数据
     *
     * @param scenicName      景区名称
     * @param messageMenuEnum MessageMenuEnum
     * @param peopleNum       游客数
     * @param warningCapacity 预警配置树
     * @return MessageRequest.AddReq
     */
    private MessageRequest.AddReq getAddReq(String scenicName, MessageMenuEnum messageMenuEnum, Integer peopleNum, Integer warningCapacity) {
        double touristsDegree = 0;
        if (warningCapacity != null && warningCapacity != 0 && warningCapacity != -1) {
            touristsDegree = new BigDecimal(peopleNum * 1.0 / warningCapacity * 100).setScale(2, RoundingMode.DOWN).doubleValue();
        }
        MessageRequest.AddReq addReq = new MessageRequest.AddReq();
        addReq.setApp(AppEnum.OPERATION);
        addReq.setCategory(CategoryEnum.ALARM);
        addReq.setType("景区客流预警");
        addReq.setMenuUri(messageMenuEnum.getUri());
        addReq.setRedirect(true);
        addReq.setAggs(scenicName);
        addReq.setBrief("景区客流预警");
        addReq.setBtime(System.currentTimeMillis());
        addReq.setDetail("游客数量:" + peopleNum + ", 游客承载百分比:" + touristsDegree + "%");
        List<Long> users = roleMenuMapper.getUserIdByMenuCode(messageMenuEnum.getCode());
        addReq.setUserIds(users);
        return addReq;
    }


}
