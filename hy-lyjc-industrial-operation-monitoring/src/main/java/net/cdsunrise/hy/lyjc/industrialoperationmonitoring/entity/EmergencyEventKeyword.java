package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author lijiafeng 2021/01/19 10:38
 */
@Data
public class EmergencyEventKeyword {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 应急事件ID
     */
    private Long emergencyEventId;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 创建时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date gmtModified;
}
