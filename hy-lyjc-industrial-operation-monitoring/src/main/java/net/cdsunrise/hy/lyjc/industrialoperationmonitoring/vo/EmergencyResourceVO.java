package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lijiafeng
 * @date 2019/11/22 9:51
 */
@Data
public class EmergencyResourceVO {

    /**
     * 主键ID
     */
    @NotNull(message = "资源ID不能为空", groups = {UpdateCheckGroup.class})
    private Long id;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空")
    private String name;

    /**
     * 资源类型编码
     */
    @NotBlank(message = "资源类型编码不能为空")
    private String type;

    /**
     * 资源类型
     */
    private String typeName;

    /**
     * 资源数量
     */
    @NotNull(message = "资源数量不能为空")
    private BigDecimal inventory;

    /**
     * 资源数量单位
     */
    @NotNull(message = "资源数量单位不能为空")
    private String unit;

    /**
     * 更新时间
     */
    private Date gmtModified;
}
