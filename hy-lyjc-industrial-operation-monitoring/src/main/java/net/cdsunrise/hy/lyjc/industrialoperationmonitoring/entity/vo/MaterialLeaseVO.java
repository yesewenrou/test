package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

/**
 * MaterialLeaseVO
 *
 * @author LiuYin
 * @date 2021/1/17 15:54
 */
@Data
public class MaterialLeaseVO {


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

    /** 创建时间*/
    private Long createTime;

    /** 更新时间*/
    private Long updateTime;


}
