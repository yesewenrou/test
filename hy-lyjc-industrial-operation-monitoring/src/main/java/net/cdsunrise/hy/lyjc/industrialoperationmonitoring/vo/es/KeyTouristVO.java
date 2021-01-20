package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2020/1/13 15:28
 */
@Data
public class KeyTouristVO {

    /**
     * 实时游客数
     */
    private Integer realTimeTouristNum;

    /**
     * 今日游客总数
     */
    private Integer todayTouristCount;

    /**
     * 去年今日游客总数
     */
    private Integer lastTodayTouristCount;

    /**
     * 游客总数同比去年
     */
    private Double compareLastTouristCount;

    /**
     * 同比去年扩展类型（正负代表上涨或下跌）
     */
    private Integer compareLastExtendType;

    /**
     * 前年今日游客总数
     */
    private Integer beforeLastTodayTouristCount;

    /**
     * 游客总数同比前年
     */
    private Double compareBeforeLastTouristCount;

    /**
     * 同比前年扩展类型（正负代表上涨或下跌）
     */
    private Integer compareBeforeLastExtendType;

    /**
     * 采集时间
     */
    private String responseTime;
}
