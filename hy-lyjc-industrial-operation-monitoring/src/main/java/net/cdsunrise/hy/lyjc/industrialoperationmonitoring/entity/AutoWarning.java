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
@TableName(value = "hy_auto_warning")
public class AutoWarning {
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 预警对象 **/
    private String object;
    /** 预警类型  **/
    private String type;
    /** 状态  033001:待确认, 033002:已忽略  033003:已申请 033004:已过期  **/
    private String status;
    /** 预警时间 **/
    private Date warningTime;
    /** 应急事件ID 预警正式发布后 生成对应的预警事件**/
    private Long emergencyEventId = 0L;
    private Date createTime;
    private Date updateTime;
}
