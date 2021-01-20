package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.TouristNumNewest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristPassengerTicket;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated.TouristLocalData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TouristNumNewestVO;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/9/29 16:14
 */
public interface ITouristNumNewestService extends IService<TouristNumNewest> {

    /**
     * 处理数据
     *
     * @param touristLocalData 数据
     */
    void dealTouristLocalOperated(TouristLocalData touristLocalData);


    /**
     * 处理数据
     *
     * @param touristTicketData 数据
     */
    void dealTouristTicketOperated(TouristPassengerTicket touristTicketData);

    /**
     * 获取游客实时数据
     *
     * @return 结果
     */
    List<TouristNumNewestVO> listTouristNumNewest();
}
