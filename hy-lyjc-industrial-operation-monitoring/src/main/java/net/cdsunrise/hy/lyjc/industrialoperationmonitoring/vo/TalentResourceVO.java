package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * @author lijiafeng
 * @date 2020/1/17 11:01
 */
@Data
public class TalentResourceVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 人才姓名
     */
    private String name;

    /**
     * 人才类型
     */
    private String type;

    /**
     * 性别
     */
    private String sex;

    /**
     * 学历
     */
    private String education;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * 单位及职务
     */
    private String unitAndPosition;

    /** 诚信等级*/
    private Integer integrityLevel;
    /** 诚信等级描述*/
    private String integrityLevelDesc;
}
