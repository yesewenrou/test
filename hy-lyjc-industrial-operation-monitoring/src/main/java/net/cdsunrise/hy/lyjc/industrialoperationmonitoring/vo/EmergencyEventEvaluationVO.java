package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lijiafeng 2021/01/19 13:39
 */
@Data
public class EmergencyEventEvaluationVO {

    /**
     * 应急预案名称
     */
    private Long emergencyPlanId;

    /**
     * 应急预案名称
     */
    private String emergencyPlanName;

    /**
     * 适配率
     */
    private BigDecimal fitRate;
}
