package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * MaterialTermVO
 * 物资条目视图对象
 * @author LiuYin
 * @date 2021/1/17 14:45
 */
@Data
public class MaterialTermVO {

    /** id*/
    private Long id;

    /** 名称*/
    private String name;

    /** 用途*/
    private String purpose;

    /** 单位*/
    private String unit;

    /** 期初库存*/
    private String beginningInventory;

    /** 物资图片*/
    private List<String> pics;

    /** 创建时间*/
    private Long createTime;

    /** 更新时间*/
    private Long updateTime;
}
