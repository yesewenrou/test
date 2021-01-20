package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @author LHY
 */
@Data
public class ComplaintManageVO {

    /**被投诉对象**/
    @NotBlank(message = ParamConst.COMPLAINT_OBJECT_ERROR)
    private String complaintObject;
    /**投诉人**/
    @NotBlank(message = ParamConst.COMPLAINANT_ERROR)
    private String complainant;
    private String sex;
    @NotBlank(message = ParamConst.MOBILE_ERROR)
    private String mobile;
    /**投诉内容**/
    @NotBlank(message = ParamConst.CONTENT_ERROR)
    private String content;
    /**投诉凭证**/
    private String certificate;
    /**投诉分类**/
    @NotBlank(message = ParamConst.COMPLAINT_TYPE_ERROR)
    private String type;
    /**投诉渠道**/
    @NotBlank(message = ParamConst.CHANNEL_ERROR)
    private String channel;
    /**投诉时间**/
    @NotNull(message = ParamConst.COMPLAINT_TIME_ERROR)
    private Timestamp complaintTime;
}
