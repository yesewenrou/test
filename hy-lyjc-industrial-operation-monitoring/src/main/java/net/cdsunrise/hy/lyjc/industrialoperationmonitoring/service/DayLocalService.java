package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DayLocal;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated.TouristLocalData;

import java.util.Date;

/**
 * @Author: LHY
 * @Date: 2019/9/29 17:31
 */
public interface DayLocalService extends IService<DayLocal>{

    void dealTouristLocalOperated(TouristLocalData touristLocalData);

}
