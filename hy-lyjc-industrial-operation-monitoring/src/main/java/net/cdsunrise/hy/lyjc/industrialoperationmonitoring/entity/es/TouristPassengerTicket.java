package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author LHY
 * @date 2020/1/2 16:35
 *
 * 游客门票数据
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
     * 同比去年扩展类型（正负代表上涨或下跌）
     */
    @Field(type = FieldType.Integer)
    private Integer compareLastExtendType;

    /**
     * 同比前年扩展类型（正负代表上涨或下跌）
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
     * 线上索道收入占比
     */
    @Field(type = FieldType.Double)
    private Double cablewayPercent;

    /**
     * 线上观光车收入占比
     */
    @Field(type = FieldType.Double)
    private Double sightseeingCarPercent;

    /**
     * 线上门票收入占比
     */
    @Field(type = FieldType.Double)
    private Double ticketPercent;

    /**
     * 采集时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String responseTime;
}
