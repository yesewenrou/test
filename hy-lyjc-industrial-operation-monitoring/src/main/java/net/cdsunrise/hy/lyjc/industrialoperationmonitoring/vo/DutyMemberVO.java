package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import java.util.Date;
/**
 * @author lixiao
 */
@Data
public class DutyMemberVO {

    private Long id;

    /**
     * 值班单位
     */
    private String dutyInstitutions;

    /**
     * 值班部门
     */
    private String dutyDepartment;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 值班人员
     */
    private String dutyPerson;

    /**
     * 联系方式
     */
    private String dutyContact;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

}
