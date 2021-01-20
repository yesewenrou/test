package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author lijiafeng
 * @date 2019/9/29 16:05
 */
@Data
@TableName("hy_tourist_num_newest")
public class TouristNumNewest {

    /**
     * 主键ID
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
     * 人员类型：0为游客 1 为居住 2为上班
     */
    private Integer memberType;

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
