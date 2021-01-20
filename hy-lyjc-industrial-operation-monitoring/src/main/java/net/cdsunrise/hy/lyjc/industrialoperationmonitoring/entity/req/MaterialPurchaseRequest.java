package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req;

import lombok.Data;

/**
 * MaterialProcurementRequest
 * 物资采购请求
 * @author LiuYin
 * @date 2021/1/17 15:17
 */
@Data
public class MaterialPurchaseRequest {

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

    /** 采购日期*/
    private Long purchaseDate;

    /** 凭证地址*/
    private String voucherUrl;


}
