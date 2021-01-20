package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import net.cdsunrise.hy.lydsjdatacenter.starter.util.JsonLocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/3/3 22:24
 */
@Data
public class TourismConsumptionBusinessCircleAnalyzeVO {

    /**
     * 消费汇总
     */
    private BigDecimal totalTransAt;

    /**
     * 涉旅商户数
     */
    private Integer merchantNum;

    /**
     * 数据列表
     */
    private List<TransData> list;

    @Data
    public static class TransData {
        /**
         * 日期
         */
        @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
        private LocalDateTime date;

        /**
         * 消费
         */
        private BigDecimal transAt;

        /**
         * 昨天或上月消费
         */
        private BigDecimal lastTransAt;

        /**
         * 环比
         */
        private BigDecimal periodOnPeriod;

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
