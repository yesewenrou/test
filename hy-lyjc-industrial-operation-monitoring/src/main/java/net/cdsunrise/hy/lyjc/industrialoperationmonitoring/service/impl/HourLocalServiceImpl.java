package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.CountyHourLocal;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.HourLocal;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.ScenicEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.HourLocalMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated.TouristLocalData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ICountyHourLocalService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IHourLocalService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author lijiafeng
 * @date 2019/9/30 14:07
 */
@Slf4j
@Service
public class HourLocalServiceImpl extends ServiceImpl<HourLocalMapper, HourLocal> implements IHourLocalService {

    private ICountyHourLocalService countyHourLocalService;

    public HourLocalServiceImpl(ICountyHourLocalService countyHourLocalService) {
        this.countyHourLocalService = countyHourLocalService;
    }

    @Override
    public void dealTouristLocalOperated(TouristLocalData touristLocalData) {
        ScenicEnum scenicEnum = ScenicEnum.getByCode(touristLocalData.getScenicId());

        // 县域数据
        if (ScenicEnum.HY_COUNTY.equals(scenicEnum)) {
            CountyHourLocal countyHourLocal = new CountyHourLocal();
            BeanUtils.copyProperties(touristLocalData, countyHourLocal);
            countyHourLocal.setTime(DateUtil.parse(touristLocalData.getTime(), DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS));
            //去重处理
            QueryWrapper<CountyHourLocal> queryWrapper = Wrappers.query();
            queryWrapper.lambda()
                    .eq(CountyHourLocal::getScenicId, countyHourLocal.getScenicId())
                    .eq(CountyHourLocal::getMemberType, countyHourLocal.getMemberType())
                    .eq(CountyHourLocal::getTime, countyHourLocal.getTime());
            int count = countyHourLocalService.count(queryWrapper);
            if (count > 0) {
                return;
            }

            countyHourLocalService.save(countyHourLocal);
        } else {// 景区数据
            HourLocal hourLocal = new HourLocal();
            BeanUtils.copyProperties(touristLocalData, hourLocal);
            hourLocal.setTime(DateUtil.parse(touristLocalData.getTime(), DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS));
            //去重处理
            QueryWrapper<HourLocal> queryWrapper = Wrappers.query();
            queryWrapper.lambda()
                    .eq(HourLocal::getScenicId, hourLocal.getScenicId())
                    .eq(HourLocal::getMemberType, hourLocal.getMemberType())
                    .eq(HourLocal::getTime, hourLocal.getTime());
            int count = super.count(queryWrapper);
            if (count > 0) {
                return;
            }

            super.save(hourLocal);
        }
    }
}
