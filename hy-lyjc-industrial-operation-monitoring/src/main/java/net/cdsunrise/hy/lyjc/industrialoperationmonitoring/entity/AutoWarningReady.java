package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author funnylog
 */
@Data
@TableName(value = "hy_auto_warning_ready")
public class AutoWarningReady {
    /** id **/
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 预警ID **/
    private Long warningId;
    /** 状态 数据字典:  **/
    private String status;
}
