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
 * @date 2019/11/18 17:22
 * 酒店接待量统计
 */
@Data
@Document(indexName = "hotel_passenger_reception", type = "doc", replicas = 2)
public class HotelPassengerReception {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String stationId;

    @Field(type = FieldType.Text,searchAnalyzer = "ik_smart",
            analyzer = "ik_smart")
    private String name;

    @Field(type = FieldType.Text,searchAnalyzer = "ik_smart",
            analyzer = "ik_smart")
    private String address;

    /**
     * 统计日期
     * */
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String statisticalDate;

    /**
     * 当日累计接待
     * */
    @Field(type = FieldType.Double)
    private Double cumulativeReception;

    /**
     * 当日估算入住率
     * */
    @Field(type = FieldType.Keyword)
    private String estimateOccupancy;

    /** 当日酒店状态，0：未满，1：已满客 **/
    @Field(type = FieldType.Integer)
    private Integer status = 0;

    /** 记录插入系统时间，方便追溯数据 **/
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String createTime;
}
