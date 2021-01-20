package net.cdsunrise.hy.lyyt.es.entity;

import lombok.Data;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author fang yun long
 * @date 2020-01-15 10:09
 */
@Data
@Document(indexName = "tourist_passenger_ticket", type = "doc", replicas = 2)
public class TouristPassengerTicket {

    private String id;

    /**
     * 景区名称
     */
    @Field(type = FieldType.Keyword)
    private String scenicName;

    /**
     * 实时游客数
     */
    @Field(type = FieldType.Integer)
    private Integer realTimeTouristNum;

    /**
     * 今日游客总数
     */
    @Field(type = FieldType.Integer)
    private Integer todayTouristCount;

    /**
     * 去年今日游客总数
     */
    @Field(type = FieldType.Integer)
    private Integer lastTodayTouristCount;

    /**
     * 前年今日游客总数
     */
    @Field(type = FieldType.Integer)
    private Integer beforeLastTodayTouristCount;

    /**
     * 游客总数同比去年
     */
    @Field(type = FieldType.Double)
    private Double compareLastTouristCount;

    /**
     * 游客总数同比前年
     */
    @Field(type = FieldType.Double)
    private Double compareBeforeLastTouristCount;

    /**
     * 同比去年扩展类型？？？
     */
    @Field(type = FieldType.Integer)
    private Integer compareLastExtendType;

    /**
     * 同比前年扩展类型？？？
     */
    @Field(type = FieldType.Integer)
    private Integer compareBeforeLastExtendType;

    /**
     * 线上销售票数
     */
    @Field(type = FieldType.Integer)
    private Integer onlineTicketCount;

    /**
     * 线下销售票数
     */
    @Field(type = FieldType.Integer)
    private Integer offlineTicketCount;

    /**
     * 线上索道票数占比
     */
    @Field(type = FieldType.Double)
    private Double cablewayPercent;

    /**
     * 线上观光车票数占比
     */
    @Field(type = FieldType.Double)
    private Double sightseeingCarPercent;

    /**
     * 线上门票数占比
     */
    @Field(type = FieldType.Double)
    private Double ticketPercent;

    /**
     * 采集时间.
     */
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String responseTime;
}
