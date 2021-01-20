package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ScenicStatusEnum;

/**
 * @author lijiafeng
 * @date 2019/11/29 16:22
 */
@Data
public class ScenicTouristNewestWithStatusVO {

    /**
     * 景区ID
     */
    private String scenicId;

    /**
     * 景区名称
     */
    private String scenicName;

    /**
     * 周边游客数 : 原实时游客数丶人数
     */
    private Integer peopleNum;

    /**
     * 最大承载量 : 原高峰接待量
     */
    private Integer overloadCapacity;

    /**
     * 景区运营状态
     */
    private ScenicStatusEnum scenicStatus;

    /**
     * 景区运营状态描述
     */
    private String scenicStatusDesc;

    /**
     * 舒适度百分比 不带百分号
     */
    private Double comfortablePercent;

    /**
     * 新增园内游客数字段
     */
    private Integer peopleInPark;

}
