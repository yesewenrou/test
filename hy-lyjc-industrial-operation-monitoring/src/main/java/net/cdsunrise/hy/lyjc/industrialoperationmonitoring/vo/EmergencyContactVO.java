package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author fang yun long
 * on 2021/1/19
 */
@Data
public class EmergencyContactVO {
    /** id */
    private Long id;
    /** 名称 */
    private String name;
    /** 电话 */
    private String phone;
    /** 机构 */
    private String org;
    /** 职位 */
    private String position;
    private Date gmtCreate;
    private Date gmtModified;
}
