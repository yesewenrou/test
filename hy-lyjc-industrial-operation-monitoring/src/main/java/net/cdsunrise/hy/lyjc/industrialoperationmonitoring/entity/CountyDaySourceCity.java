package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: LHY
 * @Date: 2019/9/29 16:06
 * 洪雅县国内日来源地数据
 */
@Data
@TableName("hy_county_day_source_city")
public class CountyDaySourceCity {

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
