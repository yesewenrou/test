package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

/**
 * MaterialProcurementVO
 * 物资采购视图对象
 * @author LiuYin
 * @date 2021/1/17 15:25
 */
@Data
public class MaterialPurchaseVO {

    /** 主键*/
    private Long id;

    /** 物资条目id*/
    private Long termId;

    /** 采购单号*/
    private String purchaseCode;

    /** 采购数量*/
    private Long quantity;

    /** 采购员*/
    private String purchaseUser;

    /** 凭证地址*/
    private String voucherUrl;

    /** 采购日期*/
    private Long purchaseDate;

    /** 创建时间*/
    private Long createTime;

    /** 更新时间*/
    private Long updateTime;
}
