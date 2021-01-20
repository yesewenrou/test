package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author LHY
 */
@Data
public class FeatureLabelVO {
    /** 标签名称 */
    @NotBlank(message = "标签名称不能为空")
    private String labelName ;
    /** 更新人 */
    private Long updateBy ;
    /** 更新人 */
    private String updateName ;
    /** 更新时间 */
    private Date updateTime ;
}
