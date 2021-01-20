package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/3/3 14:04
 */
public interface ITourismConsumptionAnalyzeService {

    /**
     * 旅游消费分析
     *
     * @return 结果
     */
    TourismConsumptionAnalyzeVO getTourismConsumptionAnalyze();

    /**
     * 旅游消费来源地分析
     *
     * @param condition 查询条件
     * @return 结果
     */
    TourismConsumptionSourceAnalyzeVO getTourismConsumptionSourceAnalyze(TourismConsumptionAnalyzeCondition condition);

    /**
     * 旅游消费行业分析
     *
     * @param condition 查询条件
     * @return 结果
     */
    List<TourismConsumptionIndustryAnalyzeVO> getTourismConsumptionIndustryAnalyze(TourismConsumptionAnalyzeCondition condition);

    /**
     * 旅游消费商圈分析
     *
     * @param condition 查询条件
     * @return 结果
     */
    TourismConsumptionBusinessCircleAnalyzeVO getTourismConsumptionBusinessCircleAnalyze(TourismConsumptionAnalyzeCondition condition);
}
