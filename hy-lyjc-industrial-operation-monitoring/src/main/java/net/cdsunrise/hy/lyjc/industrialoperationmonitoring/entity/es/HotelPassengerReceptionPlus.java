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
 * @date 2020/5/6 13:46
 *
 * 酒店接待量统计升级版（基于凌晨校准数据 + 当日入住数据）
 */
@Data
@Document(indexName = "hotel_passenger_reception_plus", type = "doc", replicas = 2)
public class HotelPassengerReceptionPlus {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String stationId;

    /**
     * 当日累计接待
     * */
    @Field(type = FieldType.Integer)
    private Integer cumulativeReception;

    /**
     * 统计日期
     * */
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String statisticalDate;

    /** 记录插入系统时间，方便追溯数据 **/
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String createTime;

}
