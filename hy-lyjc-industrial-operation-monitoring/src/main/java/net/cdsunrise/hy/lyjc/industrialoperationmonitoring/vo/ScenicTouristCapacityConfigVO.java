package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lijiafeng
 * @date 2019/11/20 15:07
 */
@Data
public class ScenicTouristCapacityConfigVO {

    /**
     * 景区编码
     */
    @NotBlank(message = "景区编码不能为空")
    private String scenicCode;

    /**
     * 景区名称
     */
    private String scenicName;
    /** 舒适 **/
    @NotNull(message = "舒适容量不能为空")
    @Min(value = 1, message = "舒适容量不能小于1")
    private Integer comfortableCapacity;

    /**
     * 预警人数
     */
    @NotNull(message = "预警人数不能为空")
    @Min(value = 1, message = "预警人数不能小于1")
    private Integer warningCapacity;

    /**
     * 较舒适
     */
    @NotNull(message = "较舒适容量不能为空")
    @Min(value = 1, message = "较舒适容量不能小于1")
    private Integer lessComfortableCapacity;

    /**
     * 一般
     */
    @NotNull(message = "一般容量不能为空")
    @Min(value = 1, message = "一般容量不能小于1")
    private Integer ordinaryCapacity;

    /**
     * 较拥挤容量
     */
    @NotNull(message = "较拥挤容量不能为空")
    @Min(value = 1, message = "较拥挤容量不能小于1")
    private Integer saturationCapacity;

    /**
     * 拥挤容量
     */
    @NotNull(message = "拥挤容量不能为空")
    @Min(value = 1, message = "拥挤容量不能小于1")
    private Integer overloadCapacity;
}
