package net.cdsunrise.hy.lyyt.entity.vo;

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
    private BigDecimal totalTransAt;

    /**
     * 省内消费
     */
    private BigDecimal innerProvTransAt;

    /**
     * 省内来源地列表
     */
    private List<TransInfo> innerProvList;

    /**
     * 省外消费
     */
    private BigDecimal outerProvTransAt;

    /**
     * 省外来源地列表
     */
    private List<TransInfo> outerProvList;

    @Data
    public static class TransInfo {

        /**
         * 来源
         */
        private String source;

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
         * 占总消费比例
         */
        private BigDecimal proportion;

        /**
         * 消费人次
         */
        private Integer acctNum;

        /**
         * 人均消费
         */
        private BigDecimal perAcctTransAt;
    }
}
