package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.HourLocal;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated.TouristLocalData;

/**
 * @author lijiafeng
 * @date 2019/9/30 13:44
 */
public interface IHourLocalService extends IService<HourLocal> {

    /**
     * 处理数据
     *
     * @param touristLocalData 数据
     */
    void dealTouristLocalOperated(TouristLocalData touristLocalData);
}
