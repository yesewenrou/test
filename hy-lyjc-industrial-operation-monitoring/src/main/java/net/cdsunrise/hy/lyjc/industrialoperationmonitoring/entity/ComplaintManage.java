package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import net.cdsunrise.common.utility.annotations.FieldInfo;

import java.sql.Timestamp;

/**
 * @author LHY
 */
@Data
@TableName("hy_complaint_manage")
public class ComplaintManage {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**投诉编号**/
    @FieldInfo(value = "投诉编号",order = 1)
    private String complaintNumber;
    /**被投诉对象**/
    private String complaintObject;
    /**投诉人**/
    private String complainant;
    private String sex;
    private String mobile;
    /**投诉内容**/
    private String content;
    /**投诉凭证**/
    private String certificate;
    /**投诉分类**/
    private String type;
    /**投诉渠道**/
    private String channel;
    /**投诉时间**/
    private Timestamp complaintTime;
    /**投诉单状态：1：未处理，2：受理中，3：不受理，4：已完成**/
    private Integer status;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Timestamp createTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Timestamp updateTime;

}
