package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.ConsumptionStatisticsVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.ScenicReportVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.StringLongVO;
import net.cdsunrise.hy.lyyx.precisionmarketing.autoconfigure.feign.vo.NameAndValueVO;

import java.util.List;
import java.util.Map;

/**
 * @author lijiafeng
 * @date 2020/05/11 10:46
 */
@Data
@SuppressWarnings("all")
public class SpecialReportWordData {

    /**
     * 概况
     */
    private Overview overview;

    /** 客流量分析 **/
    private ScenicReportVO.PassengerFlowAnalysis PFA;

    /**
     * 旅游消费分析
     */
    private TourismConsumptionAnalysis TCA;

    /** 交通分析*/
    private TrafficAnalysis TA;

    /**
     * 酒店分析
     */
    private HotelAnalysis HCA;

    @Data
    public static class Overview {

        /**
         * 时间范围
         */
        private String dateRange = "";

        /**
         * 洪雅县域累计客流
         */
        private Double passenger = 0.0;

        /**
         * 各大景区范围客流量
         */
        private String passengerByScenic = "";

        /**
         * 洪雅县域累计消费
         */
        private Double consumption = 0.0;

        /**
         * 各大商圈旅游消费
         */
        private String consumptionByBusinessCircle = "";

        /**
         * 商圈消费原始数据
         */
        private List<ConsumptionStatisticsVO> businessCircleData;
    }

    @Data
    public static class TourismConsumptionAnalysis {

        /**
         * 消费最高行业
         */
        private String highestBusiness = "";

        /**
         * 消费最高行业消费金额
         */
        private Double highestBusinessConsumption = 0.0;

        /**
         * 消费最高行业消费金额占比
         */
        private Double highestBusinessRatio = 0.0;

        /**
         * 消费次高行业
         */
        private String higherBusiness = "";

        /**
         * 消费次高行业消费金额
         */
        private Double higherBusinessConsumption = 0.0;

        /**
         * 消费次高行业消费金额占比
         */
        private Double higherBusinessRatio = 0.0;

        /**
         * 行业消费原始数据
         */
        private List<TourismConsumptionIndustryAnalyzeVO> industryData;

        /**
         * 四川省合计 万元
         */
        private Double consumptionInProvince = 0.0;

        /**
         * 四川省占比  百分比
         */
        private Double consumptionInProvinceRatio = 0.0;

        /**
         * 省外合计 万元
         */
        private Double consumptionOutsideProvince = 0.0;

        /**
         * 省外占比  百分比
         */
        private Double consumptionOutsideProvinceRatio = 0.0;

        /**
         * 省内游客消费金额排行前五
         */
        private String top5ConsumptionInProvince = "";

        /**
         * 省外游客消费金额排行前五
         */
        private String top5ConsumptionOutsideProvince = "";

        /**
         * 来源地消费原始数据
         */
        private TourismConsumptionSourceAnalyzeVO sourceData;
    }

    @Data
    public static class HotelAnalysis{
        /**
         * 累计接待人次
         **/
        private Double checkInAmount = 0.0;

        /**
         * 接待趋势
         **/
        private List<KeyAndValue<String, Integer>> lineCheckInAmount;

        /**
         * 累计接待酒店排行
         **/
        private String top5CheckInAmount = " ";

        /**
         * 性别
         **/
        private String gender = " ";

        /**
         * 占比
         **/
        private Double genderRatio = 0.0;

        /**
         * 年龄分布
         **/
        private String top3AgeDistribution = " ";

        /**
         * 年龄段
         **/
        private List<KeyAndValue<String, Integer>> barCheckInAgeDistribution;

        /**
         * 过夜天数分布
         **/
        private String top3StayOvernight = " ";

        /**
         * 过夜天数段
         **/
        private List<KeyAndValue<String, Integer>> pieDataStayOvernight;
    }


    @Data
    public static class TrafficAnalysis{

        /** 流量趋势*/
        private List<StringLongVO> lineTrafficFlowTrend;
        /** 重点路段*/
        private String heavyArterialTraffic = "";
        /** 进县域流量统计*/
        private String trafficFlow = "";
        /** 进县域省内来源top5*/
        private String top5TrafficSourceInProvince = "";
        /** 进县域省外来源top5*/
        private String top5TrafficSourceOutsideProvince = "";

        /** 原始数据*/
        private Map<String, List<StringLongVO>> data;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommonNameValue{
        /** 名称*/
        private String name;
        /** 数值*/
        private String value;
    }

}
