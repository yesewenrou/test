package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req;

import lombok.Data;

/**
 * MaterialLossReportRequest
 * 物资报损请求
 * @author LiuYin
 * @date 2021/1/17 15:48
 */
@Data
public class MaterialLossReportRequest {

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
}
