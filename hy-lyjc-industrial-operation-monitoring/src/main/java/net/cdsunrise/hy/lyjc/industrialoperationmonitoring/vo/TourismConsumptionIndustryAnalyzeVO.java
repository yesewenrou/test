package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

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
    private String industry = "";

    /**
     * 消费
     */
    private BigDecimal transAt = BigDecimal.ZERO;

    /**
     * 去年同期消费
     */
    private BigDecimal sameTimeLastYear = BigDecimal.ZERO;

    /**
     * 去年同比
     */
    private BigDecimal yearOnYear = BigDecimal.ZERO;

    /**
     * 游客贡献度
     */
    private BigDecimal transAtRatio = BigDecimal.ZERO;

    /**
     * 消费人次
     */
    private Integer acctNum = 0;

    /**
     * 人均消费
     */
    private BigDecimal perAcctTransAt = BigDecimal.ZERO;
}
