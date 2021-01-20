package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

/**
 * ImportantProjectVO
 * 重点建设项目VO
 * @author LiuYin
 * @date 2020/1/19 14:18
 */
@Data
public class ImportantProjectVO {

    /** 主键id */
    private Long id;
    /** 项目名称 */
    private String projectName;
    /** 建设地址 */
    private String buildAddress;
    /** 类型 */
    private String serviceType;
    /** 建设年限 */
    private String buildTime;
    /** 总投资 */
    private Double totalInvestment;
    /** 已投资*/
    private Double globalInvestment;
    /** 建设内容及规模 */
    private String buildContentAndScale;
    /** 业主单位 */
    private String ownerUnit;
    /** 责任单位 */
    private String responsibilityUnit;


}
