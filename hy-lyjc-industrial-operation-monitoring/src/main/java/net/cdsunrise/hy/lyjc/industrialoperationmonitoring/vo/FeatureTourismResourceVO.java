package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author LHY
 */
@Data
public class FeatureTourismResourceVO {
    /** 资源名称 */
    @NotBlank(message = "资源名称不能为空")
    private String resourceName ;
    /** 资源类型编码 */
    @NotBlank(message = "资源类型编码不能为空")
    private String resourceTypeCode ;
    /** 资源类型名称 */
    @NotBlank(message = "资源类型名称不能为空")
    private String resourceTypeName ;
    /** 所属区域编码 */
    @NotBlank(message = "所属区域编码不能为空")
    private String regionalCode ;
    /** 所属区域名称 */
    @NotBlank(message = "所属区域名称不能为空")
    private String regionalName ;
    /** 详细地址 */
    @NotBlank(message = "详细地址不能为空")
    private String addressDetail ;
    /** 特色标签 */
    @NotBlank(message = "特色标签不能为空")
    private String featureLabelIds ;
    /** 更新人 */
    private Long updateBy ;
    /** 更新人 */
    private String updateName ;
    /** 更新时间 */
    private Date updateTime ;
}
