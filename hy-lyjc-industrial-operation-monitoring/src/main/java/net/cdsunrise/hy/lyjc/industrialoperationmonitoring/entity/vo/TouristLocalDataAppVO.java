package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author FangYunLong
 * @date in 2020/4/29
 */
@Data
public class TouristLocalDataAppVO {
    /** 实时 **/
    private Map<String, Integer> realTime;
    /** 昨日累计 **/
    private Map<String, Integer> yesterday;
    /** 当年累计 **/
    private Map<String, Integer> currentYear;
    /** 当日累计 **/
    private Map<String, Integer> currentDay;

    /**
     * 由于返的Map 此类暂时不用
     */
    @Data
    public static class Statistics {
        /** 洪雅县 全域 **/
        private Integer hongYaXian;
        /** 瓦屋山 在园 **/
        private Integer waWuShanInPark;
        /** 瓦屋山 入园 **/
        private Integer waWuShanInParkTotal;
        /** 柳江古镇 **/
        private Integer liuJiangGuZhen;
        /** 玉屏山 **/
        private Integer yuPingShan;
        /** 七里坪 **/
        private Integer qiLiPing;
        /** 槽渔滩 **/
        private Integer caoYuTan;
        /** 主城区 **/
        private Integer zhuChengQu;
    }
}
