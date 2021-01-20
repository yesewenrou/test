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
@TableName("hy_menu")
public class MenuDTO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String code;
    private String path;
    private String permission;
    private Long parentId;
    private String moduleType;
    private String icon;
    private String mark;
    private Long weight;
    private Timestamp createTime;
    private Timestamp updateTime;
}
