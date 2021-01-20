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
public class HandleResultVO {

    private Long id;
    /**投诉编号**/
    @NotNull(message = ParamConst.COMPLAINT_ID_ERROR)
    private Long complaintId;
    /**被投诉对象全称**/
    @NotBlank(message = ParamConst.COMPLAINT_OBJECT_ERROR)
    private String complaintObjectFullname;
    /**投诉行业分类**/
    @NotBlank(message = ParamConst.COMPLAINT_INDUSTRY_ERROR)
    private String industryType;
    /**投诉重要性**/
    @NotNull(message = ParamConst.COMPLAINT_IMPORTANCE_ERROR)
    private Integer importance;
    /**投诉重要性描述**/
    private String importanceDesc;
    /**处理结果**/
    @NotBlank(message = ParamConst.HANDLE_RESULT_ERROR)
    private String handlerResult;
    /**受理人**/
    private String assignee;
    /**受理人处理时间**/
    private Timestamp assigneeTime;
    /**不受理原因**/
    private String rejectReason;
    /**最终处理人**/
    private String handler;
    /**最终处理时间**/
    private Timestamp handleTime;
}
