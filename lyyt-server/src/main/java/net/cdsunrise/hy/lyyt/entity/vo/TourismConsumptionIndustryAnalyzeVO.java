package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lijiafeng
 * @date 2020/3/3 13:44
 */
@Data
public class TourismConsumptionIndustryAnalyzeVO {

    /**
     * 行业
     */
    private String industry;

    /**
     * 消费
     */
    private BigDecimal transAt;

    /**
     * 去年同期消费
     */
    private BigDecimal sameTimeLastYear;

    /**
     * 去年同比
     */
    private BigDecimal yearOnYear;

    /**
     * 游客贡献度
     */
    private BigDecimal transAtRatio;

    /**
     * 消费人次
     */
    private Integer acctNum;

    /**
     * 人均消费
     */
    private BigDecimal perAcctTransAt;
}
