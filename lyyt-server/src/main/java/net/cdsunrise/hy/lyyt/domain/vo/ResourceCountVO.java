package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

/**
 * ResourceCountVO
 *
 * @author LiuYin
 * @date 2021/1/11 15:24
 */
@Data
public class ResourceCountVO {
    /** 数据类型*/
    private String type;
    /** 数据类型名称*/
    private String typeName;
    /** 数据父类型*/
    private String parentType;
    /** 数据父类型名称*/
    private String parentTypeName;
    /** 统计数量*/
    private Integer count;
    /** 更新时间*/
    private String updateTime;
    /** 顺序*/
    private Integer order;
}
