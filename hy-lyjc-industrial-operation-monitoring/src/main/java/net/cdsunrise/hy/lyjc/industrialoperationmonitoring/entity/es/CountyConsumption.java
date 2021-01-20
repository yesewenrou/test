package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author LHY
 * @date 2019/11/28 11:31
 *
 * 每日洪雅县旅游消费规模
 */
@Data
@Document(indexName = "county_consumption", type = "doc", replicas = 2)
public class CountyConsumption {

    private String id;

    /**
     * 地市（此处存储城市code，方便统一查询）
     * */
    @Field(type = FieldType.Keyword)
    private String consumeCity;

    /**
     * 区县
     * */
    @Field(type = FieldType.Keyword)
    private String consumeCountry;

    /**
     * 游客类型，省内:1, 省外:2
     * */
    @Field(type = FieldType.Keyword)
    private String travellerType;

    /**
     * 日期，格式：2019-11-28
     * */
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String dealDay;

    /**
     * 消费金额
     * */
    @Field(type = FieldType.Double)
    private Double transAt;

    /**
     * 消费笔数
     * */
    @Field(type = FieldType.Long)
    private Integer transNum;

    /**
     * 消费人次
     * */
    @Field(type = FieldType.Long)
    private Integer acctNum;

    public CountyConsumption() {
    }

    public CountyConsumption(String consumeCity, String consumeCountry, String travellerType, String dealDay, Double transAt, Integer transNum, Integer acctNum) {
        this.consumeCity = consumeCity;
        this.consumeCountry = consumeCountry;
        this.travellerType = travellerType;
        this.dealDay = dealDay;
        this.transAt = transAt;
        this.transNum = transNum;
        this.acctNum = acctNum;
    }
}
