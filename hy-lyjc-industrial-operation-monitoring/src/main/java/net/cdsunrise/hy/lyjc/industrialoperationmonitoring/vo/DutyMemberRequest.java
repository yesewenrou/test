package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author lixiao
 */
@Data
public class DutyMemberRequest {

    @NotNull(message = "id不能为空", groups = {UpdateCheckGroup.class})
    private Long id;

    /**
     * 值班部门ID
     */
    private Long departmentId;

    /**
     * 值班人员
     */
    @NotNull(message = "值班人员不能为空")
    private String dutyPerson;

    /**
     * 联系方式
     */
    @NotNull(message = "联系方式不能为空")
    private String dutyContact;

    /**
     * 更新时间
     */
    private Date gmtModified;
}
