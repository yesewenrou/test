package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import java.sql.Timestamp;

/**
 * @Author: LHY
 * @Date: 2019/10/10 15:29
 * 商户投诉
 */
@Data
public class MerchantComplaintVO {

    /**投诉人**/
    private String complainant;
    /**投诉时间**/
    private Timestamp complaintTime;
    /**投诉内容**/
    private String content;
    /**投诉渠道**/
    private String channel;
    /**反馈人**/
    private String handler;
    /**反馈时间**/
    private Timestamp handleTime;
}
