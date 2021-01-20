package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author LHY
 */
@Data
@TableName("hy_tourism_resource")
public class TourismResource {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String area;
    private String location;
    private String type;
    private Double longitude;
    private Double latitude;
    private String servicePhone;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Timestamp createTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Timestamp updateTime;
}
