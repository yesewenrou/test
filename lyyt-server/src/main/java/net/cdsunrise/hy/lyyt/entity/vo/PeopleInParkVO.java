package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

/**
 * @author fang yun long
 * @date 2020-03-02 17:55
 */
@Data
public class PeopleInParkVO {
    /** 玉屏山 实时游客数**/
    private Integer ypsRealCount;
    /** 瓦屋山 实时游客数**/
    private Integer wwsRealCount;
    /** 玉屏山 今日累计游客数**/
    private Integer ypsTodayCount;
    /** 瓦屋山 今日累计游客数**/
    private Integer wwsTodayCount;
}
