package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author funnylog
 */
@Data
public class AutoWarningTrafficDTO {

    /** 预警时间 **/
    @NotNull(message = "预警时间不能为空")
    private Long warningTime;

    /** 路段名称 **/
    @NotBlank(message = "路段名称不能为空")
    private String roadName;

    /** 总里程 **/
    @NotBlank(message = "总里程不能为空")
    private String totalMileage;

    /** 路段描述 **/
    @NotBlank(message = "路段描述不能空")
    private String roadDesc;

    /** 告警原因 **/
    @NotBlank(message = "告警原因不能为空")
    private String warningReason;

    /** 拥堵指数 **/
    @NotBlank(message = "拥堵指数不能为空")
    private String congestionIndex;

    /** 平均速度 **/
    @NotBlank(message = "平均速度不能为空")
    private String averageSpeed;
}
