package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TouristLocalDataAppVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TouristPeopleHotVO;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/11/29 11:02
 */
public interface ITouristHeatMapService {

    /**
     * 查询全部游客热力图
     *
     * @return 结果
     */
    List<TouristPeopleHotVO> listAllTouristPeopleHot();

    /**
     * 旅游管理APP 游客统计
     * @return TouristLocalDataAppVO
     */
    TouristLocalDataAppVO touristStatisticsForApp();

}
