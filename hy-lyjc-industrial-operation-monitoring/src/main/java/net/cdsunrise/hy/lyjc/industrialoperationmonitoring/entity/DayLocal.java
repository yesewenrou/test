package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: LHY
 * @Date: 2019/9/29 17:28
 * 1.3按天指定查询时间，反馈眉山市洪雅县日客流数据
 */
@Data
@TableName("hy_day_local")
public class DayLocal {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String scenicId;
    private String scenicName;
    private Integer peopleNum;
    private Integer memberType;
    private String datasource;
    private Timestamp time;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Timestamp createTime;

}
