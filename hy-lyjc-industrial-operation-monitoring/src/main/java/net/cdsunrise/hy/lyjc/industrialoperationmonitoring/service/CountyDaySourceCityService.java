package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.CountyDaySourceCity;

import java.util.List;

/**
 * @Author: LHY
 * @Date: 2019/9/29 16:11
 */
public interface CountyDaySourceCityService extends IService<CountyDaySourceCity> {

    /**
     * 新增
     */
    void add(CountyDaySourceCity countyDaySourceCity);

    Integer checkDaySourceCityExist(String countryName,String cityName,String time);
}
