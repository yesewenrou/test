package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * 消费来源报告
 *
 * @author lijiafeng
 * @date 2020/05/11 08:47
 */
@Data
public class ConsumptionSourceReport {

    /**
     * 四川省合计 万元
     */
    private Double consumptionInProvince;

    /**
     * 四川省占比  百分比
     */
    private Double consumptionInProvinceRatio;

    /**
     * 省外合计 万元
     */
    private Double consumptionOutsideProvince;

    /**
     * 省外占比  百分比
     */
    private Double consumptionOutsideProvinceRatio;

    /**
     * 省内游客消费金额排行前五
     */
    private String top5ConsumptionInProvince;

    /**
     * 省外游客消费金额排行前五
     */
    private String top5ConsumptionOutsideProvince;

    /**
     * 原始数据
     */
    private TourismConsumptionSourceAnalyzeVO sourceData;
}
