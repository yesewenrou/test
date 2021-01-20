package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import net.cdsunrise.hy.lydsjdatacenter.starter.util.JsonLocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lijiafeng
 * @date 2020/3/31 17:19
 */
@Data
public class ConsumptionSourceTrendResp {

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
