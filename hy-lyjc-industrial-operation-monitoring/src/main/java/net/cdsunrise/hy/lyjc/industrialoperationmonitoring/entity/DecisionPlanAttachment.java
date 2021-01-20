package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author fang yun long
 * on 2021/1/18
 */
@Data
@TableName("hy_decision_plan_attachment")
public class DecisionPlanAttachment {
    private Long id;
    /** 决策方案ID */
    private Long decisionPlanId;
    /** 附件名称 */
    private String attachName;
    /** 附件URL */
    private String attachUrl;
}
