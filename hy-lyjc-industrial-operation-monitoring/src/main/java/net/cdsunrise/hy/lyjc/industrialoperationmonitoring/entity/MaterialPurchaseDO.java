package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * MaterialProcurementDO
 * 物资采购
 * @author LiuYin
 * @date 2021/1/17 14:54
 */
@Data
@TableName("hy_material_purchase")
public class MaterialPurchaseDO {

    /** 主键*/
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 物资条目id*/
    private Long termId;

    /** 采购单号*/
    private String purchaseCode;

    /** 采购数量*/
    private Long quantity;

    /** 采购员*/
    private String purchaseUser;

    /** 采购日期*/
    private LocalDateTime purchaseDate;

    /** 创建时间*/
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /** 更新时间*/
    private LocalDateTime updateTime;


}
