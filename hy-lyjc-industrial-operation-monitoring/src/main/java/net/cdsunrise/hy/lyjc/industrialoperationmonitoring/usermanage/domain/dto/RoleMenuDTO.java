package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author LHY
 */
@Data
@TableName("hy_role_menu")
public class RoleMenuDTO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long roleId;
    private Long menuId;
}
