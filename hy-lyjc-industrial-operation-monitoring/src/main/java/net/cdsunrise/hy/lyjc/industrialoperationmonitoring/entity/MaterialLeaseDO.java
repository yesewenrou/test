package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * MaterialLeaseDO
 *
 * @author LiuYin
 * @date 2021/1/17 15:37
 */
@Data
@TableName("hy_material_lease")
public class MaterialLeaseDO {

    /** 主键*/
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 物资条目id*/
    private Long termId;

    /** 租赁单号*/
    private String leaseCode;

    /** 核校数量*/
    private Long quantity;

    /** 租赁员*/
    private String leaseUser;

    /** 租赁日期*/
    private LocalDateTime leaseDate;

    /** 创建时间*/
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /** 更新时间*/
    private LocalDateTime updateTime;
}
