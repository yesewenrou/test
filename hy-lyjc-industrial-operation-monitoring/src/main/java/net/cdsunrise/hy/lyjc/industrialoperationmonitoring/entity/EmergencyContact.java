package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author fang yun long
 * on 2021/1/19
 */
@Data
@TableName("emergency_contact")
public class EmergencyContact {
    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 名称 */
    private String name;
    /** 电话 */
    private String phone;
    /** 机构 */
    private String org;
    /** 职位 */
    private String position;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date gmtCreate;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date gmtModified;
}
