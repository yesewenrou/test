package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

/**
 * @author sh
 * @date 2020-02-14 14:51
 */
@Data
public class WeatherVo {
    private String id;
    /** 各个地方天气类型 */
    private String weatherAddr;
    /** 实时温度，单位：℃ */
    private Integer realTemp;
    /** 实时天气 */
    private String realWeather;
    /** 白天最高温度 */
    private Integer maxTemp;
    /** 夜间最低温度 */
    private Integer minTemp;
    /**空起质量指数*/
    private Integer aqi;
    /** 空起质量等级 */
    private String aqiDesc;
    /** 空起质量颜色 */
    private String aqiColor;
    /** PM2.5，单位：μg/m³ */
    private Double pm25;
    /** PM10，单位：μg/m³ */
    private Double pm10;
    /** 二氧化硫，单位：μg/m³ */
    private Double so2;
    /** 二氧化氮，单位：μg/m³ */
    private Double no2;
    /** 一氧化碳，单位：μg/m³ */
    private Double co;
    /** 臭氧，单位：μg/m³ */
    private Double o3;
    /** 采集地址 */
    private String url;
    /** 景点名称 */
    private String scenicName;
    /** 天气更新时间 */
    private String updateTime;
    /** 采集时间 */
    private String gatherTime;
}
