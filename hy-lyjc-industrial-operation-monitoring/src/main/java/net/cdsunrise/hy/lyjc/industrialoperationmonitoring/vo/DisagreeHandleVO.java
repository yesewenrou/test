package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author LHY
 */
@Data
public class DisagreeHandleVO {

    /**投诉ID**/
    @NotNull(message = ParamConst.COMPLAINT_ID_ERROR)
    private Long complaintId;

    /**不受理原因**/
    @NotBlank(message = ParamConst.REJECT_REASON_ERROR)
    private String rejectReason;

}
