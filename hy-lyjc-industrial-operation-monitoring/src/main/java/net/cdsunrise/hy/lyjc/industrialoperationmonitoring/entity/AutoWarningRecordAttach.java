package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author fangyunlong
 * @date 2020/3/7 23:40
 */
@Data
@TableName(value = "hy_auto_warning_record_attach")
public class AutoWarningRecordAttach {
    /** id **/
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 记录id **/
    private Long recordId;
    /** 附件名称 **/
    private String attachName;
    /** 附件地址 **/
    private String attachUrl;
}

