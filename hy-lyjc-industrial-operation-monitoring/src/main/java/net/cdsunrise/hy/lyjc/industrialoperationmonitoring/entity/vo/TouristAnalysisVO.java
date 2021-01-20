package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author funnylog
 */
@Data
public class TouristAnalysisVO {

    /** 客流量合计 */
    private Integer dateRangeTotalTourist;

    /** 折线图 */
    private List<LineChartVO> touristsLineChartVO;

    /** 游客对比统计 **/
    private TouristCompareVO touristCompareVO;



    @Data
    public static class LineChartVO {
        /** 日期 **/
        private String day;
        /** 游客数 **/
        private Integer tourists;
    }

    @Data
    public static class TouristCompareVO {
        /** 当前游客实时数 **/
        private Integer currentTimeTourists;

        /** 当前舒适度 **/
        private String comfortableStatus;

        /** 今日对比 **/
        private TouristCompareCommonVO today;

        /** 当月对比 **/
        private TouristCompareCommonVO currentMonth;

        /** 当年对比 **/
        private TouristCompareCommonVO currentYear;

    }

    @Data
    public static class TouristCompareCommonVO {
        /** 当前累计客流量 **/
        private Integer currentTotalTourists;
        /** 去年同期客流量 **/
        private Integer lastYearTotalTourists;
        /** 同比扩展类型 **/
        private Integer compareLastExtendType;
        /** 游客总数同比去年 **/
        private Double compareLastCount;
    }

}
