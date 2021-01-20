package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ScenicStatusEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.ScenicTouristCapacityConfig;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristPassengerTicket;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper.TouristPassengerTicketMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IHotMapFixService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IScenicTouristCapacityConfigService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITouristNumNewestService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ScenicTouristCapacityConfigVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ScenicTouristNewestWithStatusVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TouristNumNewestVO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author funnylog
 */
@Slf4j
@Service
public class HotMapFixServiceImpl implements IHotMapFixService {

    private IScenicTouristCapacityConfigService scenicTouristCapacityConfigService;

    private ITouristNumNewestService touristNumNewestService;

    private TouristPassengerTicketMapper touristPassengerTicketMapper;



    /** 瓦屋山 玉屏山 景区数据字典 code **/
    private static final String[] TOURISTS_IN_PARK = {"002001", "002005"};


    public HotMapFixServiceImpl(IScenicTouristCapacityConfigService scenicTouristCapacityConfigService,
                                ITouristNumNewestService touristNumNewestService,
                                TouristPassengerTicketMapper touristPassengerTicketMapper) {
        this.scenicTouristCapacityConfigService = scenicTouristCapacityConfigService;
        this.touristNumNewestService = touristNumNewestService;
        this.touristPassengerTicketMapper = touristPassengerTicketMapper;
    }


    /**
     * 查询景区实时游客数及运营状态
     *
     * @return 结果
     */
    @Override
    public List<ScenicTouristNewestWithStatusVO> scenicTouristHotMapStatistics() {
        List<ScenicTouristCapacityConfigVO> scenicTouristCapacityConfigVoList = scenicTouristCapacityConfigService.listAllConfig();
        List<TouristNumNewestVO> touristNumNewestVoList = touristNumNewestService.listTouristNumNewest();
        List<ScenicTouristNewestWithStatusVO> list = scenicTouristCapacityConfigVoList.stream().map(scenicTouristCapacityConfigVO -> {
            String scenicName = scenicTouristCapacityConfigVO.getScenicName();
            String scenicCode = scenicTouristCapacityConfigVO.getScenicCode();
            log.info("景区名称:{}, 景区编码:{}", scenicName, scenicCode);
            ScenicTouristNewestWithStatusVO scenicTouristNewestWithStatusVO = new ScenicTouristNewestWithStatusVO();
            scenicTouristNewestWithStatusVO.setScenicId(scenicTouristCapacityConfigVO.getScenicCode());
            scenicTouristNewestWithStatusVO.setScenicName(scenicTouristCapacityConfigVO.getScenicName());
            scenicTouristNewestWithStatusVO.setOverloadCapacity(scenicTouristCapacityConfigVO.getOverloadCapacity());

            // 周边游客数
            TouristNumNewestVO touristNumNewest = touristNumNewestVoList.stream()
                    .filter(touristNumNewestVO -> touristNumNewestVO.getScenicName().equals(scenicName))
                    .findFirst()
                    .orElse(null);
            if (touristNumNewest != null) {
                scenicTouristNewestWithStatusVO.setPeopleNum(touristNumNewest.getPeopleNum());
            } else {
                scenicTouristNewestWithStatusVO.setPeopleNum(0);
            }
            // 用于判断拥挤度
            int peopleNum = 0;
            // 如果是玉屏山或者瓦屋山
            if (Arrays.asList(TOURISTS_IN_PARK).contains(scenicCode)) {
                // 查询园内游客数
                List<TouristPassengerTicket> touristPassengerTicket = touristPassengerTicketMapper.findByScenicNameOrderByResponseTimeDesc(scenicName, PageRequest.of(0, 1));
                if (touristPassengerTicket.size() > 0) {
                    // 园内游客数来判断拥挤度
                    peopleNum = touristPassengerTicket.get(0).getRealTimeTouristNum();
                    scenicTouristNewestWithStatusVO.setPeopleInPark(peopleNum);
                } else {
                    scenicTouristNewestWithStatusVO.setPeopleInPark(0);
                }
            } else {
                scenicTouristNewestWithStatusVO.setPeopleInPark(0);
                peopleNum = scenicTouristNewestWithStatusVO.getPeopleNum();
            }

            // 计算拥挤度
            ScenicStatusEnum scenicStatus = getScenicStatus(scenicCode, peopleNum);
            scenicTouristNewestWithStatusVO.setScenicStatus(scenicStatus);
            scenicTouristNewestWithStatusVO.setScenicStatusDesc(scenicStatus.getDesc());

            // 计算舒适度百分比
            if (peopleNum == 0) {
                scenicTouristNewestWithStatusVO.setComfortablePercent(0D);
            } else {
                Integer maxCapacity = scenicStatus.getMaxCapacity();
                BigDecimal bigDecimal = new BigDecimal(1.0 * peopleNum / maxCapacity);
                Double comfortablePercent = bigDecimal.setScale(4, RoundingMode.HALF_UP).doubleValue();
                scenicTouristNewestWithStatusVO.setComfortablePercent(comfortablePercent);
            }
            return scenicTouristNewestWithStatusVO;
        }).collect(Collectors.toList());
        // 判断是否存在 主城区 存在则移除
        list.removeIf(scenic -> checkScenicName(scenic.getScenicName()));
        return list;
    }

    @Override
    public ScenicStatusEnum getScenicStatus(String scenicCode, Integer peopleNum) {
        QueryWrapper<ScenicTouristCapacityConfig> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(ScenicTouristCapacityConfig::getScenicCode, scenicCode);
        ScenicTouristCapacityConfig scenicTouristCapacityConfig = scenicTouristCapacityConfigService.getOne(queryWrapper);
        Integer comfortableCapacity = ScenicTouristCapacityServiceImpl.DEFAULT_COMFORTABLE_CAPACITY;
        Integer lessComfortableCapacity = ScenicTouristCapacityServiceImpl.DEFAULT_LESS_COMFORTABLE_CAPACITY;
        Integer ordinaryCapacity = ScenicTouristCapacityServiceImpl.DEFAULT_ORDINARY_CAPACITY;
        Integer saturationCapacity = ScenicTouristCapacityServiceImpl.DEFAULT_SATURATION_CAPACITY;
        Integer overloadCapacity = ScenicTouristCapacityServiceImpl.DEFAULT_OVERLOAD_CAPACITY;
        if (scenicTouristCapacityConfig != null) {
            comfortableCapacity = scenicTouristCapacityConfig.getComfortableCapacity();
            lessComfortableCapacity = scenicTouristCapacityConfig.getLessComfortableCapacity();
            ordinaryCapacity = scenicTouristCapacityConfig.getOrdinaryCapacity();
            saturationCapacity = scenicTouristCapacityConfig.getSaturationCapacity();
            overloadCapacity = scenicTouristCapacityConfig.getOverloadCapacity();
        }
        ScenicStatusEnum scenicStatusEnum;
        if (peopleNum.compareTo(saturationCapacity) >= 0) {
            scenicStatusEnum = ScenicStatusEnum.OVERLOAD;
        } else if (peopleNum.compareTo(ordinaryCapacity) >= 0) {
            scenicStatusEnum = ScenicStatusEnum.SATURATED;
        } else if (peopleNum.compareTo(lessComfortableCapacity) >= 0){
            scenicStatusEnum = ScenicStatusEnum.NORMAL;
        } else if (peopleNum.compareTo(comfortableCapacity) >= 0) {
            scenicStatusEnum = ScenicStatusEnum.LESS_COMFORTABLE;
        } else {
            scenicStatusEnum = ScenicStatusEnum.COMFORTABLE;
        }
        log.info("拥挤度判断结果为:{}", scenicStatusEnum.getDesc());
        scenicStatusEnum.setMaxCapacity(overloadCapacity);
        return scenicStatusEnum;
    }

    /**
     * 移除 主城区  true 需要移除  false不需要移除
     * @param scenicName 景区名称
     * @return boolean
     */
    private boolean checkScenicName(String scenicName) {
        String[] scenicNames = {"主城区"};
        return Arrays.asList(scenicNames).contains(scenicName);
    }
}
