package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lijiafeng 2021/01/18 14:58
 */
@Data
public class RescueTeamVO {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = {UpdateCheckGroup.class})
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 救援类型
     */
    @NotNull(message = "救援类型不能为空")
    private Integer type;

    /**
     * 救援类型描述
     */
    private String typeDesc;

    /**
     * 隶属单位
     */
    @NotBlank(message = "隶属单位不能为空")
    private String org;

    /**
     * 负责人
     */
    @NotBlank(message = "负责人不能为空")
    private String principal;

    /**
     * 负责人电话
     */
    @NotBlank(message = "负责人电话不能为空")
    private String principalPhone;

    /**
     * 队伍电话
     */
    @NotBlank(message = "队伍电话不能为空")
    private String teamPhone;
}
