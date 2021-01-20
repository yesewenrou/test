package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;

import java.util.Date;

/**
 * @Author: LHY
 * @Date: 2019/9/25 11:56
 * ES获取舆情数据
 */
@Data
public class PublicSentiment {

    /**标题**/
    private String title;
    /**作者**/
    private String author;
    /**正面：1，中性：0，负面：-1**/
    private Integer sentiment;
    private String date;
    /**内容**/
    private String content;
    /**舆情采集时间**/
    private String adddate;
    /**来源**/
    private String origin;
    /**微博原地址**/
    private String url;
}
