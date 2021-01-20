package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author LHY
 */
@Data
public class FeatureTourismResourceUpdateVO {
    @NotNull(message = "id不能为空")
    private Long id;
    /** 资源名称 */
    private String resourceName ;
    /** 资源类型编码 */
    private String resourceTypeCode ;
    /** 资源类型名称 */
    private String resourceTypeName ;
    /** 所属区域编码 */
    private String regionalCode ;
    /** 所属区域名称 */
    private String regionalName ;
    /** 详细地址 */
    private String addressDetail ;
    /** 特色标签 */
    private String featureLabelIds ;
    /** 更新人 */
    private Long updateBy ;
    /** 更新人 */
    private String updateName ;
    /** 更新时间 */
    private Date updateTime ;
}
