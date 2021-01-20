package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.CountyDaySourceCity;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DataResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.ScenicEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated.TouristSourceCityData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated.TouristSourceCountryData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.CountyDaySourceCityService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.DataResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.DaySourceCityService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * @author LHY
 * @date 2019/9/29 15:57
 */
@Service
@Slf4j
public class DaySourceCityServiceImpl implements DaySourceCityService {

    private static final String CHINA = "中国";

    private DataResourceService dataResourceService;
    private CountyDaySourceCityService countyDaySourceCityService;

    public DaySourceCityServiceImpl(DataResourceService dataResourceService, CountyDaySourceCityService countyDaySourceCityService) {
        this.dataResourceService = dataResourceService;
        this.countyDaySourceCityService = countyDaySourceCityService;
    }

    @Override
    public void dealTouristSourceCityOperated(TouristSourceCityData touristSourceCityData) {
        if (ScenicEnum.HY_COUNTY.getName().equals(touristSourceCityData.getScenicName())) {
            Integer num = countyDaySourceCityService.checkDaySourceCityExist(touristSourceCityData.getCountryName(),
                    touristSourceCityData.getCityName(), touristSourceCityData.getTime());
            if (num > 0) {
                // 已成功插入数据，不进行更新
            } else {
                CountyDaySourceCity countyDaySourceCity = new CountyDaySourceCity();
                BeanUtils.copyProperties(touristSourceCityData, countyDaySourceCity);
                countyDaySourceCity.setTime(new Timestamp(DateUtil.parse(touristSourceCityData.getTime(), DateUtil.PATTERN_YYYY_MM_DD).getTime()));
                countyDaySourceCityService.add(countyDaySourceCity);
            }
        } else {
            Integer num = dataResourceService.checkDataResourceExist(touristSourceCityData.getCountryName(),
                    touristSourceCityData.getCityName(), touristSourceCityData.getTime());
            if (num > 0) {
                // 已成功插入数据，不进行更新
            } else {
                DataResource dataResource = new DataResource();
                BeanUtils.copyProperties(touristSourceCityData, dataResource);
                dataResource.setTime(new Timestamp(DateUtil.parse(touristSourceCityData.getTime(), DateUtil.PATTERN_YYYY_MM_DD).getTime()));
                dataResourceService.add(dataResource);
            }
        }
    }

    @Override
    public void dealTouristSourceCountryOperated(TouristSourceCountryData touristSourceCountryData) {
        ScenicEnum scenicEnum = ScenicEnum.getByCode(touristSourceCountryData.getScenicId());

        // 此处不处理中国数据
        if (CHINA.equals(touristSourceCountryData.getCountryName())) {
            return;
        }

        // 县域数据
        if (ScenicEnum.HY_COUNTY.equals(scenicEnum)) {
            CountyDaySourceCity countyDaySourceCity = new CountyDaySourceCity();
            BeanUtils.copyProperties(touristSourceCountryData, countyDaySourceCity);
            countyDaySourceCity.setTime(new Timestamp(DateUtil.parse(touristSourceCountryData.getTime(), DateUtil.PATTERN_YYYY_MM_DD).getTime()));
            //去重处理
            QueryWrapper<CountyDaySourceCity> queryWrapper = Wrappers.query();
            queryWrapper.lambda()
                    .eq(CountyDaySourceCity::getScenicName, countyDaySourceCity.getScenicName())
                    .eq(CountyDaySourceCity::getCountryName, countyDaySourceCity.getCountryName())
                    .eq(CountyDaySourceCity::getTime, countyDaySourceCity.getTime());
            int count = countyDaySourceCityService.count(queryWrapper);
            if (count > 0) {
                return;
            }

            countyDaySourceCityService.save(countyDaySourceCity);
        } else {// 景区数据
            DataResource dataResource = new DataResource();
            BeanUtils.copyProperties(touristSourceCountryData, dataResource);
            dataResource.setTime(new Timestamp(DateUtil.parse(touristSourceCountryData.getTime(), DateUtil.PATTERN_YYYY_MM_DD).getTime()));
            //去重处理
            QueryWrapper<DataResource> queryWrapper = Wrappers.query();
            queryWrapper.lambda()
                    .eq(DataResource::getCountryName, dataResource.getCountryName())
                    .eq(DataResource::getScenicName, dataResource.getScenicName())
                    .eq(DataResource::getTime, dataResource.getTime());
            int count = dataResourceService.count(queryWrapper);
            if (count > 0) {
                return;
            }

            dataResourceService.save(dataResource);
        }
    }

}
