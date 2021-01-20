package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author fangyunlong
 * @date 2020/3/7 23:41
 */
@Data
@TableName(value = "hy_auto_warning_record_receiver")
public class AutoWarningRecordReceiver {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long recordId;
    /** 接收者类型 1 诱导屏， 2 短信 **/
    private Integer type;
    /** 接收者id：诱导屏id、联系人ID**/
    private Long receiverId;
    /** 接收者姓名：诱导屏名称、联系人名称**/
    private String receiverName;
}
