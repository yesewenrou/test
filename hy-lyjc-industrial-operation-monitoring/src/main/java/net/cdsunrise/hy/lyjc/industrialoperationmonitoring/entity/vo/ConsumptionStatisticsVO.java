package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商圈消费统计VO
 *
 * @author YQ on 2020/3/25.
 */
@Data
public class ConsumptionStatisticsVO {

    /**
     * 范围
     */
    private String cbdName = "";

    /**
     * 消费金额
     */
    private BigDecimal transAt = BigDecimal.ZERO;

    /**
     * 消费笔数
     */
    private Integer transNum = 0;

    /**
     * 消费人次
     */
    private Integer acctNum = 0;

    /**
     * 人均消费金额
     */
    private BigDecimal transAtAvg = BigDecimal.ZERO;

    /**
     * 人均消费笔数
     */
    private BigDecimal transNumAvg = BigDecimal.ZERO;
}
