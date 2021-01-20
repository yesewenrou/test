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
public class EmergencyEventCloseVO {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * 结案描述
     */
    @NotBlank(message = "结案描述不能为空")
    private String closeContent;

    /**
     * 结案附件
     */
    @Valid
    private List<EmergencyEventVO.Attachment> closeAttachments;

    /**
     * 结案图片
     */
    @Valid
    private List<EmergencyEventVO.Attachment> closeImages;
}
