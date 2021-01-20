package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author LHY
 */
@Data
@TableName("hy_role")
public class RoleDTO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer status;
    private Integer deleted;
    private Timestamp createTime;
    private Timestamp updateTime;
}
