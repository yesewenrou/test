package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.common.utility.annotations.FieldInfo;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author LHY
 */
@Data
public class ResourceVO {

    @NotNull(message = ParamConst.ID_ERROR, groups = {UpdateCheckGroup.class})
    private Long id;
    @NotBlank(message = ParamConst.NAME_ERROR)
    @FieldInfo(value = "资源名称",order = 1)
    private String name;
    @NotBlank(message = ParamConst.AREA_ERROR)
    @FieldInfo(value = "所属区域",order = 2)
    private String area;
    @NotBlank(message = ParamConst.LOCATION_ERROR)
    @FieldInfo(value = "地理位置",order = 4)
    private String location;
    @NotBlank(message = ParamConst.RESOURCE_TYPE_ERROR)
    @FieldInfo(value = "行业类型",order = 3)
    private String type;
    @FieldInfo(value = "经度",order = 6)
    private Double longitude;
    @FieldInfo(value = "纬度",order = 7)
    private Double latitude;
    @FieldInfo(value = "服务电话",order = 5)
    private String servicePhone;

}
