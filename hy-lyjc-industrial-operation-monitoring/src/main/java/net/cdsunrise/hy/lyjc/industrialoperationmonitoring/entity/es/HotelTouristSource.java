package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author LHY
 * @date 2019/11/19 11:44
 * 旅客来源地统计
 */
@Data
@Document(indexName = "hotel_tourist_source", type = "doc", replicas = 2)
public class HotelTouristSource {

    private String id;

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

    /** 记录插入系统时间，方便追溯数据 **/
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String createTime;
}
