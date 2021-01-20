package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/3/3 13:44
 */
@Data
public class TourismConsumptionSourceAnalyzeVO {

    /**
     * 总消费
     */
    private BigDecimal totalTransAt = BigDecimal.ZERO;

    /**
     * 省内消费
     */
    private BigDecimal innerProvTransAt = BigDecimal.ZERO;

    /**
     * 省内来源地列表
     */
    private List<TransInfo> innerProvList;

    /**
     * 省外消费
     */
    private BigDecimal outerProvTransAt = BigDecimal.ZERO;

    /**
     * 省外来源地列表
     */
    private List<TransInfo> outerProvList;

    @Data
    public static class TransInfo {

        /**
         * 来源
         */
        private String source = "";

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
         * 占总消费比例
         */
        private BigDecimal proportion = BigDecimal.ZERO;

        /**
         * 消费人次
         */
        private Integer acctNum = 0;

        /**
         * 人均消费
         */
        private BigDecimal perAcctTransAt = BigDecimal.ZERO;
    }
}
