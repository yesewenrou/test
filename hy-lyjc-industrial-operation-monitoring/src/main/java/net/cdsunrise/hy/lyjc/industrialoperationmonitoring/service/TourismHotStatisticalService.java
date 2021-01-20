package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ChartVO;

import java.util.List;
import java.util.Map;

/**
 * @author LHY
 * @date 2019/11/25 11:15
 *
 * 游客热力图右侧数据统计
 */
public interface TourismHotStatisticalService {

    /**
     * 县域实时游客数 + 县域昨日高峰游客数
     *
     */
    Map<String,Object> countyStatistical();

    /**
     * 当天县域游客趋势
     *
     */
    List<ChartVO> countyTourismTrend();

    /**
     * 当天景区游客趋势
     *
     */
    Map<String,List<ChartVO>> scenicTourismTrend();
}
