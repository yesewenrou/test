package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req;

import lombok.Data;

/**
 * MaterialLeaseRequest
 * 物资租赁请求
 * @author LiuYin
 * @date 2021/1/17 15:46
 */
@Data
public class MaterialLeaseRequest {


    /** 主键*/
    private Long id;

    /** 物资条目id*/
    private Long termId;

    /** 租赁单号*/
    private String leaseCode;

    /** 核校数量*/
    private Long quantity;

    /** 租赁员*/
    private String leaseUser;

    /** 租赁日期*/
    private Long leaseDate;

    /** 凭据地址*/
    private String voucherUrl;
}
