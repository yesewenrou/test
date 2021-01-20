package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author fang yun long
 * on 2021/1/18
 */
@Data
@TableName("hy_decision_plan")
public class DecisionPlan {

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 名称 */
    private String name;
    /** 事件类型 */
    private String eventType;
    /** 事件等级 */
    private String eventLevel;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date gmtCreate;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date gmtModified;
}

