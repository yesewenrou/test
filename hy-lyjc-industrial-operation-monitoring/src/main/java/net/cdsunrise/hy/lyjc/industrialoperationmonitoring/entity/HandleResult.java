package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author LHY
 */
@Data
@TableName("hy_handle_result")
public class HandleResult {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**投诉ID**/
    private Long complaintId;
    /**被投诉对象全称**/
    private String complaintObjectFullname;
    /**投诉行业分类**/
    private String industryType;
    /**投诉重要性**/
    private Integer importance;
    /**不受理原因**/
    private String rejectReason;
    /**受理人**/
    private String assignee;
    /**受理人处理时间**/
    private Timestamp assigneeTime;
    /**最终处理人**/
    private String handler;
    /**最终处理时间**/
    private Timestamp handleTime;
    /**处理结果**/
    private String handlerResult;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Timestamp createTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Timestamp updateTime;

}
