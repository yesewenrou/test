package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum.EmergencyEventStatusEnum;

/**
 * @author lijiafeng
 * @date 2019/11/27 16:09
 */
@Data
public class EmergencyEventCondition {

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件等级
     */
    private String eventLevel;

    /**
     * 事件状态 0-待审核、1-待处理、2-待结案、3-已结案、4-未通过
     */
    private EmergencyEventStatusEnum eventStatus;

    /**
     * 被指派的人员ID
     */
    private Long assignedUserId;
}
