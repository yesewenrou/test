package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.traffic;

import lombok.Data;

import java.util.List;

/**
 * @author : suzhouhe  @date : 2019/8/24 10:50  @description : 产业运行监测图形数据
 */
@Data
public class ExternalGraphVO {
    /**
     * 图形数据
     */
    private List<Graph> graphs;
    /**
     * 节假日流量总和
     */
    private Integer flowCount;
    /**
     * 省内车流量总和
     */
    private Integer innerProvinceFlowCount;
    /**
     * 省外车流量总和
     */
    private Integer outProvinceFlowCount;
    /**
     * 省内同比 单位：%
     */
    private Integer innerProvinceOnYear;
    /**
     * 省外同比  单位：%
     */
    private Integer outProvinceOnYear;

    @Data
    public static class Graph {
        /**
         * 排序，第几天
         */
        private Integer sortDays;
        /**
         * 今年节假日时间
         */
        private Long nowHolidayTime;
        /**
         * 去年节假日时间
         */
        private Long lastHolidayTime;
        /**
         * 今年流量
         */
        private Integer nowYearFlow;
        /**
         * 去年流量
         */
        private Integer lastYearFlow;
    }
}
