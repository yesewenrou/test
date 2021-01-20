package net.cdsunrise.hy.lyyt.entity.vo;

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
    private String cbdName;

    /**
     * 消费金额
     */
    private BigDecimal transAt;

    /**
     * 消费笔数
     */
    private Integer transNum;

    /**
     * 消费人次
     */
    private Integer acctNum;

    /**
     * 人均消费金额
     */
    private BigDecimal transAtAvg;

    /**
     * 人均消费笔数
     */
    private BigDecimal transNumAvg;
}
