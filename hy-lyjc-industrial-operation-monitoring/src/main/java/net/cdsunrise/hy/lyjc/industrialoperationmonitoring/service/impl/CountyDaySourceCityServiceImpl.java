package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.CountyDaySourceCity;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.CountyDaySourceCityMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.CountyDaySourceCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: LHY
 * @Date: 2019/9/29 16:12
 */
@Service
public class CountyDaySourceCityServiceImpl extends ServiceImpl<CountyDaySourceCityMapper,CountyDaySourceCity> implements CountyDaySourceCityService{

    @Autowired
    private CountyDaySourceCityMapper countyDaySourceCityMapper;

    @Override
    public void add(CountyDaySourceCity countyDaySourceCity) {
        save(countyDaySourceCity);
    }

    @Override
    public Integer checkDaySourceCityExist(String countryName, String cityName, String time) {
        QueryWrapper<CountyDaySourceCity> wrapper = new QueryWrapper<>();
        wrapper.eq("country_name",countryName)
                .eq("city_name",cityName)
                .eq("time",time);
        return countyDaySourceCityMapper.selectCount(wrapper);
    }

}
