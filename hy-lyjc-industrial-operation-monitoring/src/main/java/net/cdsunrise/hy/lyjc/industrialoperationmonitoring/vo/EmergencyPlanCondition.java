package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * @author lijiafeng
 * @date 2019/11/25 16:55
 */
@Data
public class EmergencyPlanCondition {

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件等级
     */
    private String eventLevel;
}
