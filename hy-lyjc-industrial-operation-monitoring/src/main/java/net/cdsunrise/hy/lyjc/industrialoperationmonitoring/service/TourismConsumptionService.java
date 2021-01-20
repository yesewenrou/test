package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.ConsumptionSourceResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.ConsumptionSourceTrendResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.ConsumptionTrendResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.IndustryConsumptionResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.ConsumptionStatisticsVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ConsumptionMapVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ConsumptionVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismConsumptionCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.HolidayConsumptionVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.HolidayTourismIncomeCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.ProvinceConsumptionCondition;

import java.util.List;
import java.util.Map;

/**
 * @author LHY
 * @date 2019/11/28 14:12
 * <p>
 * 旅游消费数据
 */
public interface TourismConsumptionService {

    /**
     * 消费汇总
     */
    Map<String, Object> consumptionSummary(Integer page, Integer size, TourismConsumptionCondition condition);

    /**
     * 消费来源地
     */
    Map<String, Object> consumptionSource(Integer page, Integer size, TourismConsumptionCondition condition);

    /**
     * 行业贡献
     */
    Map<String, Object> industryContribution(Integer page, Integer size, TourismConsumptionCondition condition);

    /**
     * 省内省外消费列表
     */
    ConsumptionMapVO provinceConsumptionList(ProvinceConsumptionCondition condition);

    List<ConsumptionStatisticsVO> consumptionStatistics(ConsumptionStatisticsReq req);

    ConsumptionTrendResp consumptionTrend(ConsumptionTrendReq req);

    IndustryConsumptionResp industryConsumption(IndustryConsumptionReq req);

    /**
     * 节假日旅游收入数据和走势图统计
     */
    Map statisticsData(HolidayTourismIncomeCondition condition);

    /**
     * 节假日旅游收入带搜索条件查询统计结果
     */
    List<HolidayConsumptionVO> conditionStatisticsData(HolidayTourismIncomeCondition condition);

    /**
     * 游客消费类型占比
     */
    List<ConsumptionVO> industryType(Integer year);

    /**
     * 历史数据旅游收入带搜索条件查询统计结果
     */
    List<HolidayConsumptionVO> historyConditionStatisticsData(HolidayTourismIncomeCondition condition);

    /**
     * 消费来源地
     */
    ConsumptionSourceResp sourceConsumption(ConsumptionSourceReq req);

    /**
     * 消费来源地消费趋势
     */
    List<ConsumptionSourceTrendResp> sourceConsumptionTrend(ConsumptionSourceTrendReq req);
}
