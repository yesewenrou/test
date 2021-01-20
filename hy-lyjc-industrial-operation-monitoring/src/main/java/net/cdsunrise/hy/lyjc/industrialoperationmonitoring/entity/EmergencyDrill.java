package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 应急演练
 *
 * @author lijiafeng
 * @date 2019/11/26 16:28
 */
@Data
public class EmergencyDrill {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 演练标题
     */
    private String drillTitle;

    /**
     * 演练日期
     */
    private Date drillDate;

    /**
     * 参与部门
     */
    private String department;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件等级
     */
    private String eventLevel;

    /**
     * 创建时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date gmtModified;
}
