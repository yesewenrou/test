package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author lijiafeng
 * @date 2019/11/20 15:07
 */
@Data
public class ScenicTouristCapacityConfig {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 景区编码
     */
    private String scenicCode;

    /**
     * 预警人数
     */
    private Integer warningCapacity;

    /**
     * 舒适
     */
    private Integer comfortableCapacity;

    /**
     * 较舒适
     */
    private Integer lessComfortableCapacity;

    /**
     * 一般
     */
    private Integer ordinaryCapacity;

    /**
     * 饱和容量丶较拥挤
     */
    private Integer saturationCapacity;

    /**
     * 超负荷容量丶拥挤
     */
    private Integer overloadCapacity;

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
