package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author lijiafeng
 * @date 2019/11/25 16:55
 */
@Data
public class EmergencyDrillCondition {

    /**
     * 演练标题
     */
    private String drillTitle;

    /**
     * 开始演练日期
     */
    private Date startDrillDate;

    /**
     * 结束演练日期
     */
    private Date endDrillDate;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件等级
     */
    private String eventLevel;
}
