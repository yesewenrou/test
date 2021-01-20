package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

/**
 * @Author: suzhouhe @Date: 2019/12/12 14:06 @Description: 新的路段信息实体
 */
@Data
public class RoadSectionExtDO {
    /** 主键id */
    private Long id;
    /** 道路id */
    private Integer roadId;
    /** 路段id */
    private String sectionId;
    /** 路段名称 */
    private String roadSectionName;
    /** 路段方向 */
    private Integer roadSectionDirection;
    /** 关联对象 */
    private String relationObjectCodes;
    /** 总里程，单位m */
    private Integer mileageCount;
    /** 路段描述 */
    private String roadSectionDescribe;
    /** 是否是实时数据中的重要路段  1：是  2：不是 */
    private Integer important;
    /** 删除状态  1：正常  2：删除 */
    private Integer deleteStatus;
    /** 经度 */
    private Double longitude;
    /** 纬度 */
    private Double latitude;
}
