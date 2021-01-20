package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author LHY
 */
@Data
@TableName("hy_user_role")
public class UserRoleDTO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long roleId;
}
