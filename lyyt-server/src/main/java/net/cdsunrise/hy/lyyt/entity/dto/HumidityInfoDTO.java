package net.cdsunrise.hy.lyyt.entity.dto;

import lombok.Data;

/**
 * HumidityInfoDTO
 * 湿度信息转换层对象
 * @author LiuYin
 * @date 2021/1/13 14:02
 */
@Data
public class HumidityInfoDTO {

    /** 景区名称(英文）*/
    private String name;
    /** 景区描述（中文）*/
    private String desc;
    /** 数据采集地址*/
    private String url;
    /** 数值*/
    private Integer value;
    /** 采集时间*/
    private String collectTime;
}
