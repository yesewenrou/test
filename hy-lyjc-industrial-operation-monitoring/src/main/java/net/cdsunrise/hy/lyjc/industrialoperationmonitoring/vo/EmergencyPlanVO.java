package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author lijiafeng
 * @date 2019/11/25 16:22
 */
@Data
public class EmergencyPlanVO {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = {UpdateCheckGroup.class})
    private Long id;

    /**
     * 预案名称
     */
    @NotBlank(message = "预案名称不能为空")
    private String planName;

    /**
     * 事件类型
     */
    @NotBlank(message = "事件类型不能为空")
    private String eventType;

    /**
     * 事件类型描述
     */
    private String eventTypeDesc;

    /**
     * 事件等级
     */
    @NotBlank(message = "事件等级不能为空")
    private String eventLevel;

    /**
     * 事件等级描述
     */
    private String eventLevelDesc;

    /**
     * 关键词
     */
    private Set<String> keywords;

    /**
     * 修改人ID
     */
    private Long modifierId;

    /**
     * 修改人名称
     */
    private String modifierName;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 附件列表
     */
    @Valid
    @NotEmpty(message = "预案附件不能为空")
    private List<Attachment> attachments;

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
        @Length(max = 100, message = "url长度不能超过100")
        private String fileName;

        /**
         * 文件地址
         */
        @NotBlank(message = "文件地址不能为空")
        @Length(max = 200, message = "url长度不能超过200")
        private String fileUrl;
    }
}
