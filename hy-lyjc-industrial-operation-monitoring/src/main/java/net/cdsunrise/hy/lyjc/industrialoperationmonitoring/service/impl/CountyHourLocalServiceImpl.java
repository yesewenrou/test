package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.CountyHourLocal;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.CountyHourLocalMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ICountyHourLocalService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author lijiafeng
 * @date 2019/9/30 14:07
 */
@Slf4j
@Service
public class CountyHourLocalServiceImpl extends ServiceImpl<CountyHourLocalMapper, CountyHourLocal> implements ICountyHourLocalService {
}
