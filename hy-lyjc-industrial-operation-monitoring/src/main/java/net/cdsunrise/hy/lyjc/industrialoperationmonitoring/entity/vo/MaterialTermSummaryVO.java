package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * MaterialTermSummaryVO
 * 物资条目摘要视图对象
 * @author LiuYin
 * @date 2021/1/17 14:46
 */
@Data
public class MaterialTermSummaryVO {


    /** id*/
    private Long id;

    /** 名称*/
    private String name;

    /** 用途*/
    private String purpose;

    /** 物资类型*/
    private Integer type;

    /** 物资类型名称*/
    private String typeName;

    /** 单位*/
    private String unit;

    /** 期初库存*/
    private Long beginningInventory;

    /** 物资图片*/
    private List<String> pics;

    /** 创建时间*/
    private Long createTime;

    /** 更新时间*/
    private Long updateTime;

    /** 采购总数*/
    private Long purchaseTotal;

    /** 报损总数*/
    private Long lossReportTotal;

    /**
     * 核算总数
     */
    private Long accountingTotal;

    /** 租赁总数*/
    private Long leaseTotal;

    /** 存货*/
    private Long inventory;

}
