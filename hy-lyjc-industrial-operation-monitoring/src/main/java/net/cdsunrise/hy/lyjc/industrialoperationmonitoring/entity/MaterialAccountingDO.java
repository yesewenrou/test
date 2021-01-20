package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * MaterialAccountingDO
 * 物资核算
 * @author LiuYin
 * @date 2021/1/17 15:27
 */
@Data
@TableName("hy_material_accounting")
public class MaterialAccountingDO {


    /** 主键*/
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 物资条目id*/
    private Long termId;

    /** 核算单号*/
    private String accountingCode;

    /** 核校数量*/
    private Long quantity;

    /** 校核员*/
    private String accountingUser;

    /** 校核日期*/
    private LocalDateTime accountingDate;

    /** 创建时间*/
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /** 更新时间*/
    private LocalDateTime updateTime;

}
