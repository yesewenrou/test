package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import net.cdsunrise.hy.lydsjdatacenter.starter.util.JsonLocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/3/10 10:29
 */
@Data
public class ConsumptionMapVO {

    /**
     * 开始时间
     */
    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    private LocalDateTime beginDate;

    /**
     * 结束时间
     */
    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    private LocalDateTime endDate;

    /**
     * 总消费金额
     */
    private BigDecimal totalTransAt;

    /**
     * 省内各城市
     */
    private List<Consumption> innerProvince;

    /**
     * 省外各省份
     */
    private List<Consumption> outerProvince;

    @Data
    public static class Consumption {

        /**
         * 来源地
         */
        private String source;

        /**
         * 交易金额
         */
        private BigDecimal transAt;

        /**
         * 交易笔数
         */
        private Integer transNum;

        /**
         * 消费人次
         */
        private Integer acctNum;

        /**
         * 人均消费金额
         */
        private BigDecimal perConsumption;

        /**
         * 人均消费笔数
         */
        private BigDecimal perConsumptionPens;
    }
}
