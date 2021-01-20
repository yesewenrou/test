package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/11/27 16:43
 */
@Data
public class EmergencyEventFeedbackVO {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * 反馈内容
     */
    @NotBlank(message = "反馈内容不能为空")
    private String feedbackContent;

    /**
     * 反馈现场照片
     */
    @Valid
    private List<EmergencyEventVO.Attachment> feedbackPhotos;
}
