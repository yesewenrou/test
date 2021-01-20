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
@TableName("hy_auto_warning_contact_config")
public class AutoWarningContactConfig {
    /** id **/
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 姓名 **/
    private String name;
    /** 手机号 **/
    private String phone;
    /** 是否自动发送客流预警短信 **/
    private Boolean scenicAuto;
    /** 是否自动发送交通预警短信 **/
    private Boolean trafficAuto;
    /** 是否自动发送停车预警短信 **/
    private Boolean carAuto;
    /** 创建时间 **/
    private Date createTime;
    /** 修改时间 **/
    private Date updateTime;
}
