package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: suzhouhe @Date: 2020/3/24 10:18 @Description:
 */
@Data
public class MonitorSiteListVO {
    /**
     * 今日进县城统计车流量
     */
    private Integer inCityTodayCount;
    /**
     * 实时进县城车流量
     */
    private Integer realTimeInCityCount;
    /**
     * 11个监控点当日累计车流量
     */
    private List<MonitorSiteToDay> monitorSiteToDays;

    @Data
    public static class MonitorSiteToDay {
        /**
         * 监控点id
         */
        private String monitorSiteId;
        /**
         * 监控点名称
         */
        private String monitorSiteName;
        /**
         * 当日车流量统计
         */
        private Integer todayCount;
    }
}
