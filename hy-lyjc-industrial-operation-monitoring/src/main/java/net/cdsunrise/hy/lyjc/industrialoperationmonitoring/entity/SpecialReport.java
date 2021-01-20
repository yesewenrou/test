package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author lijiafeng
 * @date 2020/1/17 14:29
 */
@Data
public class SpecialReport {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 专题报告名称
     */
    private String name;

    /**
     * 专题报告类型
     */
    private String type;

    /**
     * 附件名称
     */
    private String attachmentName;

    /**
     * 附件地址
     */
    private String attachmentUrl;

    /**
     * 生成日期
     */
    private Date reportTime;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;
}
