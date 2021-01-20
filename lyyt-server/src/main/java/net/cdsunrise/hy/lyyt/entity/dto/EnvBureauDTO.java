package net.cdsunrise.hy.lyyt.entity.dto;

import lombok.Data;

/**
 * @author sh
 * @date 2020-01-14 15:47
 */
@Data
public class EnvBureauDTO {
    private String id;
    /** 一氧化碳 */
    private Double co;
    /** 一氧化氮 */
    private Double no;
    /** 二氧化氮 */
    private Double no2;
    /** 氮氧化物 */
    private Double nox;
    /** 臭氧 */
    private Double o3;
    /** pm1.0 */
    private Double pm10;
    /** pm2.5 */
    private Double pm25;
    /** 二氧化硫 */
    private Double so2;
    /** 景区名 */
    private String recordSite;
    /** 景区code码 */
    private String scenicArea;
    /** 采集时间 */
    private String recordTime;
}
