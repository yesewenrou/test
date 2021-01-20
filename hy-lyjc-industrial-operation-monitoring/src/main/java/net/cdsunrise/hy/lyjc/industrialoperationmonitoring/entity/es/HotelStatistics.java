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
 * @date 2020/5/6 11:15
 *
 * 存储酒店每日校准后的库存和入住率
 */
@Data
@Document(indexName = "hotel_statistics", type = "doc", replicas = 2)
public class HotelStatistics {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String stationId;

    /**
     * 酒店校准后人数
     * */
    @Field(type = FieldType.Integer)
    private Integer checkinNum;

    /**
     * 每日入住率
     * */
    @Field(type = FieldType.Double)
    private Double checkInPercent;

    /**
     * 统计日期（T-1）
     * */
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String statisticalDate;

    /** 记录插入系统时间，方便追溯数据 **/
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String createTime;
}
