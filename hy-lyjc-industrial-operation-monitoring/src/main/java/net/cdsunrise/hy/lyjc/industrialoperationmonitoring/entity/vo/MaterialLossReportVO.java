package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

/**
 * MaterialLossReportRequest
 * 物资报损视图对象
 * @author LiuYin
 * @date 2021/1/17 15:56
 */
@Data
public class MaterialLossReportVO {

    /** 主键*/
    private Long id;

    /** 物资条目id*/
    private Long termId;

    /** 报损单号*/
    private String reportCode;

    /** 报损数量*/
    private Long quantity;

    /** 报损员*/
    private String reportUser;

    /** 报损日期*/
    private Long reportDate;

    /** 凭证地址*/
    private String voucherUrl;

    private Long createTime;

    private Long updateTime;





}
