/**
 * @author lixiao
 * @date 2021/1/16 10:38
 */

package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
/**
 * @author lixiao
 */
@Data
public class DutyDepartmentCondition {

    private String dutyInstitutions;

    private String departmentName;

    private String departmentLeader;

    private String leaderContact;

}
