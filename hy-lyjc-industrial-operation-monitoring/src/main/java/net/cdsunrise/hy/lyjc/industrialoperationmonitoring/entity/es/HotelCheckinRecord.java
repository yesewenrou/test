package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author LHY
 * @date 2019/10/30 16:07
 */
@Data
@Document(indexName = "hotel_checkin_record", type = "doc", replicas = 2)
public class HotelCheckinRecord {

    /** 入住数据唯一编码 **/
    @Id
    @Field(type = FieldType.Keyword)
    private String recordId;

    /** 酒店公安编码 **/
    @Field(type = FieldType.Keyword)
    private String stationId;

    /** 酒店名字 **/
    @Field(type = FieldType.Text,searchAnalyzer = "ik_smart",
            analyzer = "ik_smart")
    private String name;

    /** 房间号 **/
    @Field(type = FieldType.Keyword)
    private String roomNum;

    /** 证件号码 **/
    @Field(type = FieldType.Keyword)
    private String idNum;

    /**
     * 国家名称
     */
    @Field(type = FieldType.Keyword)
    private String countryName;

    /**
     * 省份名称
     */
    @Field(type = FieldType.Keyword)
    private String provName;

    /**
     * 城市名称
     */
    @Field(type = FieldType.Keyword)
    private String cityName;

    /**
     * 区县名称
     */
    @Field(type = FieldType.Keyword)
    private String countyName;

    @Field(type = FieldType.Keyword)
    private String sex;

    /**
     * 标记年代，例如：80后，90后
     */
    @Field(type = FieldType.Integer)
    private Integer age;

    /**
     * 标记过夜天数，例如：1天，2天，3天
     */
    @Field(type = FieldType.Integer)
    private Integer overnight;

    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS_S + "||" + DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String checkinTime;

    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS_S + "||" + DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String checkoutTime;

    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS_S + "||" + DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String updateTime;

    /** 记录插入系统时间，方便追溯数据 **/
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String createTime;
}
