package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated.TouristSourceCityData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated.TouristSourceCountryData;

/**
 * 按天指定查询时间，反馈眉山市洪雅县国内各地市来源地日人流数据
 *
 * @author LHY
 * @date 2019/9/29 15:54
 */
public interface DaySourceCityService {

    /**
     * 处理各地市数据
     *
     * @param touristSourceCityData 数据
     */
    void dealTouristSourceCityOperated(TouristSourceCityData touristSourceCityData);

    /**
     * 处理国际数据
     *
     * @param touristSourceCountryData 国际数据
     */
    void dealTouristSourceCountryOperated(TouristSourceCountryData touristSourceCountryData);
}
