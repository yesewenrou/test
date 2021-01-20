package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 交通告警信息
 * @author funnylog
 */
@Data
@TableName(value = "hy_auto_warning_traffic")
public class AutoWarningTraffic {
    /** ID **/
    @TableId(type= IdType.AUTO)
    private Long id;
    /** 预警ID **/
    private Long warningId;
    /** 路段名称 **/
    private String roadName;
    /** 总里程 **/
    private String totalMileage;
    /** 路段描述 **/
    private String roadDesc;
    /** 告警原因 **/
    private String warningReason;
    /** 拥堵指数 **/
    private String congestionIndex;
    /** 平均速度 **/
    private String averageSpeed;
    private Date createTime;
    private Date updateTime;
}
