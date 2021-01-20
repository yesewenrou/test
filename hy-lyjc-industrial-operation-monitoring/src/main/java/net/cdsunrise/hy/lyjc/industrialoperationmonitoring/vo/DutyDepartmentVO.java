/**
 * @author lixiao
 * @date 2021/1/16 10:38
 */

package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;

import javax.validation.constraints.NotNull;
import java.util.Date;
/**
 * @author lixiao
 */
@Data
public class DutyDepartmentVO {

    @NotNull(message = "id不能为空", groups = {UpdateCheckGroup.class})
    private Long id;

    /**
     * 值班单位
     */
    @NotNull(message = "值班单位不能为空")
    private String dutyInstitutions;

    /**
     * 值班部门
     */
    @NotNull(message = "值班部门不能为空")
    private String departmentName;

    /**
     * 部门负责人
     */
    private String departmentLeader;

    /**
     * 联系电话
     */
    private String leaderContact;

    /**
     * 更新时间
     */
    private Date gmtModified;

}
