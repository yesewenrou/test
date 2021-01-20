package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author fang yun long
 * on 2021/1/18
 */
@Data
public class AttachmentRequest {
    /** 附件名称 */
    @NotBlank(message = "附件名称不能为空")
    private String attachName;

    /** 附件url */
    @NotBlank(message = "附件url不能为空")
    private String attachUrl;
}
