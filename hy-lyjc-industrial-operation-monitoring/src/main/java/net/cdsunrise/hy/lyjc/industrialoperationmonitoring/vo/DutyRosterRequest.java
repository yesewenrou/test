package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum.DayOfWeek;

import javax.validation.constraints.NotNull;

/**
 * @author lixiao
 */
@Data
public class DutyRosterRequest {

    /**
     * 值班时间 MONDAY ~ SUNDAY
     */
    @NotNull(message = "值班时间不能为空")
    private DayOfWeek dutyTime;

    /**
     * 联系方式
     */
//    @NotNull(message = "值班人电话不能为空")
//    private String contact;

    /**
     * 值班人員 ID
     */
    @NotNull(message = "值班人员不能为空")
    private Long memberId;



}
