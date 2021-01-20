package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.sql.Timestamp;

/**
 * @Author: LHY
 * @Date: 2019/9/17 17:25
 */
@Data
@TableName("hy_tourism_income")
public class TourismIncome {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Double income;
    private String incomeSource;
    private String scenicName;
    private Timestamp time;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Timestamp createTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Timestamp updateTime;
}
