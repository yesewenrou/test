package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req;

import lombok.Data;

/**
 * MaterialAccountingRequest
 * 物资核算请求
 * @author LiuYin
 * @date 2021/1/17 15:44
 */
@Data
public class MaterialAccountingRequest {

    /** 主键*/
    private Long id;

    /** 物资条目id*/
    private Long termId;

    /** 核算单号*/
    private String accountingCode;

    /** 核校数量*/
    private Long quantity;

    /** 校核员*/
    private String accountingUser;

    /** 校核日期*/
    private Long accountingDate;

    /** 凭据地址*/
    private String voucherUrl;

}
