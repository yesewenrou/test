package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author LHY
 * 用于分页搜索条件
 */
@Data
public class FeatureTourismResourceCondition {

    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 资源类型编码
     */
    @NotBlank(message = "资源类型不能为空")
    private String resourceTypeCode;
    /**
     * 所属区域编码
     */
    private String regionalCode;
    /**
     * 特色标签
     */
    private String featureLabel;

}
