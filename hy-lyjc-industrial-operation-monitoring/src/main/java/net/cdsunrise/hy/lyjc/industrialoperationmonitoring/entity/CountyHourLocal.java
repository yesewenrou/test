package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author lijiafeng
 * @date 2019/9/30 13:52
 */
@Data
@TableName("hy_county_hour_local")
public class CountyHourLocal {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 景区ID
     */
    private String scenicId;

    /**
     * 景区名称
     */
    private String scenicName;

    /**
     * 人数
     */
    private Integer peopleNum;

    /**
     * 人员类型:0为游客 1 为居住 2为上班
     */
    private Integer memberType;

    /**
     * 统计时间
     */
    private Date time;

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
