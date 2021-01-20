package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * MaterialLossReportDO
 * 物资报损
 * @author LiuYin
 * @date 2021/1/17 15:33
 */
@Data
@TableName("hy_material_loss_report")
public class MaterialLossReportDO {

    /** 主键*/
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 物资条目id*/
    private Long termId;

    /** 报损单号*/
    private String reportCode;

    /** 报损数量*/
    private Long quantity;

    /** 报损员*/
    private String reportUser;

    /** 报损日期*/
    private LocalDateTime reportDate;

    /** 创建时间*/
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /** 更新时间*/
    private LocalDateTime updateTime;

}
