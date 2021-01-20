package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.entity.vo.*;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/3/3 14:04
 */
public interface ITourismConsumptionAnalyzeService {

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
     * @param req 请求
     * @return 结果
     */
    List<ConsumptionStatisticsVO> consumptionStatistics(ConsumptionStatisticsReq req);
}
