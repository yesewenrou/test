package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum.EmergencyEventCheckStatusEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum.EmergencyEventStatusEnum;

import java.util.Date;

/**
 * @author lijiafeng
 * @date 2019/11/27 14:54
 */
@Data
public class EmergencyEvent {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件等级
     */
    private String eventLevel;

    /**
     * 事件地址
     */
    private String eventAddress;

    /**
     * 事件内容
     */
    private String eventContent;

    /**
     * 事发时间
     */
    private Date eventTime;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 事件状态 0-待审核、1-待处理、2-待结案、3-已结案、4-未通过
     */
    private EmergencyEventStatusEnum eventStatus;

    /**
     * 审核状态 0-通过、1-未通过
     */
    private EmergencyEventCheckStatusEnum checkStatus;

    /**
     * 审核状态原因描述
     */
    private String checkContent;

    /**
     * 审核人员ID
     */
    private Long checkUserId;

    /**
     * 审核时间
     */
    private Date checkTime;

    /**
     * 被指派人员
     */
    private Long assignedUserId;

    /**
     * 指派人id
     */
    private Long assignerUserId;

    /**
     * 指派时间
     */
    private Date assignTime;

    /**
     * 反馈内容
     */
    private String feedbackContent;

    /**
     * 反馈时间
     */
    private Date feedbackTime;

    /**
     * 结案描述
     */
    private String closeContent;

    /**
     * 结案时间
     */
    private Date closeTime;

    /**
     * 结案人id
     */
    private Long closeUserId;

    /**
     * 是否是自动生成
     */
    private Boolean autoCreated;

    /**
     * 创建时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date gmtModified;
}
