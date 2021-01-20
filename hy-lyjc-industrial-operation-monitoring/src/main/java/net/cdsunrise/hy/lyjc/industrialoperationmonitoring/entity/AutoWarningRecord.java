package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author fangyunlong
 * @date 2020/3/7 23:36
 */
@Data
@TableName(value = "hy_auto_warning_record")
public class AutoWarningRecord {
    /** id **/
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 预警id **/
    private Long warningId;
    /** 处理人姓名 **/
    private String handler;
    /** 处理类型 1 申请发布 2 发送短信 **/
    private Integer handleType;
    /** 节目名称  正式发布时有值 **/
    private String programName;
    /** 推送渠道 正式发布时有值 **/
    private String messageChannel;
    /** 发布内容(消息推送) **/
    private String content;
    /** 诱导屏发布内容 **/
    private String programContent;
    private Date createTime;
    private Date updateTime;
}
