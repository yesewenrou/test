package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * @author lijiafeng
 * @date 2020/3/8 16:14
 */
@Data
public class KeyTravelRelatedResourcesVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 资源类型
     */
    private String mainType;

    /**
     * 资源类型名称
     */
    private String mainTypeName;

    /**
     * 资源细类
     */
    private String subType;

    /**
     * 区域编码
     */
    private String region;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 星级
     */
    private String starLevel;

    /**
     * 评定部门
     */
    private String department;

    /**
     * 地址
     */
    private String address;

    /**
     * 负责人
     */
    private String principal;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 评定时间或文号
     */
    private String symbol;

    /**
     * 关联对象 游客范围或酒店公安编码
     */
    private String link;
}
