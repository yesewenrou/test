package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lijiafeng
 * @date 2019/11/27 16:32
 */
@Data
public class EmergencyEventCheckVO {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * 审核结果 true-通过、false-未通过
     */
    @NotNull(message = "审核结果不能为空")
    private Boolean checkFlag;

    /**
     * 审核状态原因描述
     */
    @NotBlank(message = "原因描述不能为空")
    private String checkContent;
}
