package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author FangYunLong
 * @date in 2020/5/9
 */
@Data
public class ScenicReportVO {

    private Overview overview;
    private PassengerFlowAnalysis PFA;

    /** 一: 概况 **/
    @Data
    public static class Overview {
        /** 日期范围 **/
        private String dateRange = "";
        /** 洪雅县域累计客流量 万人次 **/
        private Double passenger = 0.0;
        /** 各大景区客流量 **/
        private String passengerByScenic = "";
    }

    /**
     * 客流量分析 备注:Passenger Flow Analysis简化为PFA
     */
    @Data
    public static class PassengerFlowAnalysis {


        // 1. 游客来源地
        /** 县域游客来源 **/
        private PiePassengerSource piePassengerSource;

        /** 我省累计客流 **/
        private String passengerInProvince = "";
        /** 我省客流量占比 **/
        private String ratioInProvince = "";

        /** 省内前5城市 **/
        private List<LineChartVO> barPassengerInProvince;
        /** 省内游客前五城市 **/
        private String top5PassengerInProvince = "";

        /** 省外前5省份 **/
        private List<LineChartVO> barPassengerOutsideProvince;
        private String top5PassengerOutsideProvince = "";


        // 2. 趋势及景区热度
        /** 景区游客趋势分析 **/
        private LinePassengerFlowByScenic linePassengerFlowByScenic;
        /** 最受游客喜爱的热门景区 **/
        private String firstFavoriteScenic = "";
        /** 景区热度排行第二 **/
        private String secondFavoriteScenic = "";
        /** 景区热度排行第三 **/
        private String thirdFavoriteScenic = "";
        /** 累计进入瓦屋山园区内的游客数 **/
        private Integer passengerInWawu = 0;
    }

    @Data
    public static class PiePassengerSource {
        /** 省内 **/
        private Integer provinceInner = 0;
        /** 省外 **/
        private Integer provinceOuter = 0;
        /** 境外 **/
        private Integer countryOuter = 0;
    }

    /**
     * 景区游客趋势分析
     */
    @Data
    public static class LinePassengerFlowByScenic {
        /** 景区游客趋势分析 **/
        private List<LineChartVO> qiLiPing;
        private List<LineChartVO> zhuChengQu;
        private List<LineChartVO> liuJiangGuZhen;
        private List<LineChartVO> caoYuTan;
        private List<LineChartVO> hongYaXian;
        private List<LineChartVO> yuPingShan;
        private List<LineChartVO> waWuShan;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineChartVO {
        /** date / city name **/
        private String name;
        /** value **/
        private Integer value;
    }
}
