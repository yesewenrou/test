package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto;

import lombok.Data;
import net.cdsunrise.common.utility.annotations.FieldInfo;

/**
 * @author : suzhouhe  @date : 2019/9/3 19:04  @description : 平台用户角色配置日志dto
 */
@Data
public class PlatformUserLgDTO {

    @FieldInfo(value = "用户名", order = 1)
    private String userName;

    @FieldInfo(value = "角色名称", order = 2)
    private String roleNames;
}
