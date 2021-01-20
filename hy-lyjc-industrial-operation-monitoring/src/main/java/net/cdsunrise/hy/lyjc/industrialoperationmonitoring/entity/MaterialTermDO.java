package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * MaterialTermDO
 * 物资条目
 * @author LiuYin
 * @date 2021/1/17 11:35
 */
@Data
@TableName("hy_material_term")
public class MaterialTermDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 名称*/
    private String name;

    /** 用途*/
    private String purpose;

    /** 统计单位*/
    private String unit;

    /** 物资类型*/
    private Integer type;

    /** 期初库存*/
    private Long beginningInventory;

    /** 创建时间*/
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /** 修改时间*/
    private LocalDateTime updateTime;

}
