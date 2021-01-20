package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;
/**
 * @author lixiao
 */
@Data
@TableName("duty_department")
public class DutyDepartment {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 值班单位
     */
    private String dutyInstitutions;

    /**
     * 值班部门
     */
    private String departmentName;

    /**
     * 部门负责人
     */
    private String departmentLeader;

    /**
     * 联系电话
     */
    private String leaderContact;
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
