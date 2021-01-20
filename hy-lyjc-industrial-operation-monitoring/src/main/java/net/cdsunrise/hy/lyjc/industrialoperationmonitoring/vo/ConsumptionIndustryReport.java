package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import java.util.List;

/**
 * 消费行业报告
 *
 * @author lijiafeng
 * @date 2020/05/11 08:47
 */
@Data
public class ConsumptionIndustryReport {

    /**
     * 消费最高行业
     */
    private String highestBusiness;

    /**
     * 消费最高行业消费金额
     */
    private Double highestBusinessConsumption;

    /**
     * 消费最高行业消费金额占比
     */
    private Double highestBusinessRatio;

    /**
     * 消费次高行业
     */
    private String higherBusiness;

    /**
     * 消费次高行业消费金额
     */
    private Double higherBusinessConsumption;

    /**
     * 消费次高行业消费金额占比
     */
    private Double higherBusinessRatio;

    /**
     * 原始数据
     */
    private List<TourismConsumptionIndustryAnalyzeVO> industryData;
}
