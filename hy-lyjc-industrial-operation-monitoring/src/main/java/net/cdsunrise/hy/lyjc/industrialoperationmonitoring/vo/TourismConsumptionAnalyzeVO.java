package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lijiafeng
 * @date 2020/3/3 13:44
 */
@Data
public class TourismConsumptionAnalyzeVO {

    /**
     * 当月
     */
    private TransInfo currentMonth;

    /**
     * 上月
     */
    private TransInfo lastMonth;

    /**
     * 当年
     */
    private TransInfo currentYear;

    @Data
    public static class TransInfo {

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
    }
}
