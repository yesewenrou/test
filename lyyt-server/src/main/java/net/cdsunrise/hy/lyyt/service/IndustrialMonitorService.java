package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.domain.vo.BusinessCircleVO;
import net.cdsunrise.hy.lyyt.domain.vo.ScenicTouristVO;
import net.cdsunrise.hy.lyyt.entity.vo.*;
import net.cdsunrise.hy.lyyt.enums.HolidayTypeEnum;
import java.util.List;
import java.util.Map;

/**
 * @author LHY
 * @date 2019/11/5 14:34
 */
public interface IndustrialMonitorService {

    /**
     * 洪雅旅游一张图 -> 景区游客分析（按月统计聚合）
     *
     * */
    Map<String,Object> touristAnalyze();

    /**
     * 洪雅旅游一张图 -> 游客来源地TOP10（按月统计聚合）
     *
     * */
    Map<String,Object> touristSourceCity();

    /**
     * 产业运行监测 -> 游客来源地TOP10（统计昨天）
     *
     * */
    Map<String,Object> latestTouristSourceCity();

    /**
     * 产业运行监测 -> 游客人数监测（实时统计）
     *
     * */
    Map<String,Object> touristMonitor();

    /**
     * 产业运行监测 -> 舆情关键词（实时统计）
     *
     * */
    List<ScenicTouristVO> publicSentimentKeyword();

    /**
     * 节假日专题 -> 游客画像分析 -> 游客来源地TOP10（按节日统计）
     *
     * */
    Map<String,Object> holidayTouristSourceCity(HolidayTypeEnum holiday);

    /**
     * 节假日专题 -> 游客画像分析 -> 年龄和性别（按节日统计）
     *
     * */
    Map<String,Object> holidayTouristAgeSex(HolidayTypeEnum holiday);

    /**
     * 节假日专题 -> 景区游客接待分析（按节日统计）
     *
     * */
    Map<String,Object> holidayScenicTourist(HolidayTypeEnum holiday);

    /**
     * 洪雅旅游一张图 -> 酒店入住分析（年度数据统计）
     *
     * */
    Map<String,Object> hotelAnalyze();

    /**
     * 洪雅旅游一张图 -> 游客画像分析 -> 过夜天数
     *
     * */
    Map<String,Object> stayOvernight();

    /**
     * 洪雅旅游一张图 -> 游客画像分析 -> 游客年龄
     *
     * */
    List<ScenicTouristVO> tourismAge();

    /**
     * 洪雅旅游一张图 -> 游客画像分析 -> 游客性别
     *‘
     * */
    Map<String,Object> tourismSex();

    /**
     * 洪雅旅游一张图 -> 县域旅游收入分析 -> 各景区旅游收入占比（景区即商圈）
     *
     * */
    List<ScenicTouristVO> businessCircleTourismConsumption();

    /**
     * 洪雅旅游一张图 -> 县域旅游收入分析 -> 县域收入分析（按月统计年度）
     *
     * */
    Map<String,Object> tourismConsumptionAnalyze();

    /**
     * 洪雅旅游一张图 -> 游客画像分析 -> 消费行为分析（行业消费类型）
     *
     * */
    List<ScenicTouristVO> industryConsumptionType();

    /**
     * 洪雅旅游一张图 -> 游客画像分析 -> 客源地人均消费
     *
     * */
    Map<String,Object> perConsumptionCity();

    /**
     * 产业运行监测 -> 住宿接待
     *
     * */
    Map<String,Object> hotelPassengerReception();

    /**
     * 产业运行监测 -> 旅游收入趋势（从有数据的日期开始，倒推2周）+ 上周收入总分析（从有数据的日期开始，倒推1周）
     *
     * */
    Map<String,Object> tourismConsumptionTrend();

    /**
     * 节假日专题 -> 旅游收入分析（按节日统计）
     *
     * */
    Map<String,Object> holidayTourismConsumption(HolidayTypeEnum holiday);

    /**
     * 节假日专题 -> 住宿接待分析（按节日统计）
     *
     * */
    Map<String,Object> holidayPassengerReception(HolidayTypeEnum holiday);

    /**
     * 旅游商户专题 -> 涉旅行业收入分析（今年和去年）
     *
     * */
    Map<String,Object> industryConsumptionAnalyze();

    /**
     * 旅游商户专题 -> 各区域月度营收（默认查询当月）
     *
     * */
    Map<String,Object> businessCircleConsumptionMonth();

    /**
     * 旅游商户专题 -> 各区域年度累计收入和消费人次
     *
     * */
    List<BusinessCircleVO> businessCircleConsumptionYear();

    /**
     * 旅游商户专题 -> 本周商户营收走势（本周和上周）
     *
     * */
    Map<String,Object> businessConsumptionTrend();

    /**
     * 构造枚举类型节假日
     *
     * */
    List<DefaultHolidayVO> holiday();

    /**
     * 酒店年度省内省外游客数
     *
     * */
    Map<String,Object> hotelTouristSource();

    /**
     * 游客画像-》客源地分析
     *
     * @param request 请求
     * @return 结果
     */
    Map<String, Object> touristSourceStatistics(TouristStatisticsRequest request);

    /**
     * 获取景区热度
     *
     * @param req 请求
     * @return 结果
     */
    List<NameAndValueVO<Integer>> getScenicHeat(TouristPortraitReq req);
}
