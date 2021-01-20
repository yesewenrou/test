package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author funnylog
 */
@Data
@TableName(value = "hy_auto_warning_condition")
public class AutoWarningCondition {
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 景区名称 **/
    private String scenicName;
    /** 告警时间 **/

    private Date alertTime;

    /** 告警次数 **/
    private Integer alertCount;

    private Date createTime;

    private Date updateTime;

}
