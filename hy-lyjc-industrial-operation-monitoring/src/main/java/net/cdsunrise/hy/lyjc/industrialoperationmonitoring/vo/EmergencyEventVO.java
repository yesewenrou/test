package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum.EmergencyEventCheckStatusEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum.EmergencyEventStatusEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author lijiafeng
 * @date 2019/11/27 16:04
 */
@Data
public class EmergencyEventVO {

    /**
     * 主键ID
     */
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
     * 事件类型描述
     */
    private String eventTypeDesc;

    /**
     * 事件等级
     */
    private String eventLevel;

    /**
     * 事件等级描述
     */
    private String eventLevelDesc;

    /**
     * 事件地址
     */
    private String eventAddress;

    /**
     * 事件内容
     */
    private String eventContent;

    /**
     * 关键词
     */
    private Set<String> keywords;

    /**
     * 事发时间
     */
    private Date eventTime;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 现场照片
     */
    private List<Attachment> scenePhotos;

    /**
     * 资料附件
     */
    private List<Attachment> attachments;

    /**
     * 事件状态 0-待审核、1-待处理、2-待结案、3-已结案、4-未通过
     */
    private EmergencyEventStatusEnum eventStatus;

    /**
     * 事件状态描述 0-待审核、1-待处理、2-待结案、3-已结案、4-未通过
     */
    private String eventStatusDesc;

    /**
     * 审核状态 0-通过、1-未通过
     */
    private EmergencyEventCheckStatusEnum checkStatus;

    /**
     * 审核状态描述 0-通过、1-未通过
     */
    private String checkStatusDesc;

    /**
     * 审核状态原因描述
     */
    private String checkContent;

    /**
     * 审核人员ID
     */
    private Long checkUserId;

    /**
     * 审核人员名称
     */
    private String checkUserName;

    /**
     * 审核时间
     */
    private Date checkTime;

    /**
     * 被指派人员
     */
    private Long assignedUserId;

    /**
     * 被指派人员名称
     */
    private String assignedUserName;

    /**
     * 指派人id
     **/
    private Long assignerUserId;
    /**
     * 指派人姓名
     **/
    private String assignerUserName;

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
     * 反馈现场照片
     */
    private List<Attachment> feedbackPhotos;

    /**
     * 结案描述
     */
    private String closeContent;
    /**
     * 结案人id
     **/
    private Long closeUserId;
    /**
     * 结案人用户名
     **/
    private String closeUserName;

    /**
     * 结案时间
     */
    private Date closeTime;

    /**
     * 结案附件
     */
    private List<Attachment> closeAttachments;

    /**
     * 结案图片
     */
    private List<Attachment> closeImages;

    /**
     * 是否是自动生成的
     */
    private Boolean autoCreated;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    @Data
    public static class Attachment {

        /**
         * 主键ID
         */
        private Long id;

        /**
         * 文件名
         */
        @NotBlank(message = "文件名不能为空")
        @Length(max = 100, message = "文件名长度不能超过100")
        private String fileName;

        /**
         * 文件地址
         */
        @NotBlank(message = "文件地址不能为空")
        @Length(max = 200, message = "url长度不能超过200")
        private String fileUrl;
    }
}
