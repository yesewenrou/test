package net.cdsunrise.hy.lyyt.service;


import net.cdsunrise.hy.lyyt.entity.resp.GatherResponse;
import net.cdsunrise.hy.lyyt.entity.resp.SourceShareResponse;
import net.cdsunrise.hy.lyyt.entity.resp.VisualResponse;
import net.cdsunrise.hy.lyyt.entity.vo.DataGatherStatisticsVO;
import net.cdsunrise.hy.lyyt.entity.vo.ShareStatisticsVO;
import net.cdsunrise.hy.lyyt.entity.vo.TotalStatisticsVO;

import java.util.List;

/**
 * 采集呈现服务
 * @author liuyin
 */
public interface GatherViewService {

    /**
     * 获取数据可视化对象
     * @param typeIndex ip地址
     * @return 可视化对象
     */
    VisualResponse getByTypeIndex(Integer typeIndex);

    /**
     * 获取数据可视化统计
     * @return 统计对象
     */
    TotalStatisticsVO getTotalStatisticsVO();


    /**
     * 获取采集对象
     * @param typeIndex 数据类型索引
     * @return
     */
    GatherResponse getGatherResponse(Integer typeIndex);

    /**
     * 获取采集统计列表
     * @return 采集统计列表
     */
    List<DataGatherStatisticsVO> getDataGatherStatisticsList();


    /**
     * 获取数据共享对象
     * @param typeIndex 数据类型索引
     * @return
     */
    SourceShareResponse getShareResponse(Integer typeIndex);

}
