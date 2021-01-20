package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author LHY
 */
@Data
@TableName("hy_data_resource")
public class DataResource {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String scenicName;
    private String countryName;
    private String provName;
    private String cityName;
    private Integer peopleNum;
    private String datasource;
    private Timestamp time;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Timestamp createTime;
}
