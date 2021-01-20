package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum.DayOfWeek;

import javax.validation.constraints.NotNull;

/**
 * @author lijiafeng
 * @date 2019/11/25 10:37
 */
@Data
public class DutyRosterVO {

    /**
     * 值班时间 MONDAY ~ SUNDAY
     */
    @NotNull(message = "值班时间不能为空")
    private DayOfWeek dutyTime;

    /**
     * 值班时间描述 星期一 ~ 星期日
     */
    private String dutyTimeDesc;

    /**
     * 值班人员
     */
    private String dutyPerson;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 值班人員 ID
     */
    private Long memberId;

    /** 值班领导 **/
    private String dutyLeader;

    /**
     * 值班单位
     */
    private String dutyInstitutions;

    /**
     * 值班部门
     */
    private String dutyDepartment;

    /**
     * 部门负责人
     */
    private String departmentLeader;

    /**
     * 负责人联系电话
     */
    private String leaderContact;


}
