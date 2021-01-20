package net.cdsunrise.hy.lyyt.entity.co;

import lombok.Data;

/**
 * @ClassName RoadSectionCo
 * @Description 路段配置对象
 * @Author LiuYin
 * @Date 2019/12/13 16:42
 */
@Data
public class RoadSectionCo {

    /** 路段对应的道路id*/
    private Integer roadId;
    /** 路段id*/
    private String sectionId;
    /** 道路名称*/
    private String roadName;
    /** 路段长度（米）*/
    private String mileageCount;
    /** 路段描述*/
    private String roadSectionDescribe;
    /** 经度*/
    private Double longitude;
    /** 纬度*/
    private Double latitude;
    /** 路段起点名称*/
    private String beginPointName;
    /** 路段终点名称*/
    private String endPointName;


    public String getSectionName(){
        return roadName + "（" + beginPointName + " - " + endPointName + "）";
    }
}
