package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author LHY
 * @date 2019/11/29 10:49
 *
 * 来源城市-每日各省内外城市在洪雅县旅游旅游消费金额、笔数、人次
 */
@Data
@Document(indexName = "county_consumption_city", type = "doc", replicas = 2)
public class CountyConsumptionCity {

    private String id;

    /**
     * 区县
     * */
    @Field(type = FieldType.Keyword)
    private String consumeCountry;

    /**
     * 省份（此处存储省份code，方便统一查询）
     * */
    @Field(type = FieldType.Keyword)
    private String sourceProvince;

    /**
     * 城市（此处存储城市code，方便统一查询）
     * */
    @Field(type = FieldType.Keyword)
    private String sourceCity;

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
    @Field(type = FieldType.Integer)
    private Integer transNum;

    /**
     * 消费人次
     * */
    @Field(type = FieldType.Integer)
    private Integer acctNum;

    /**
     * 人均消费金额(虽然银联原数据中没有该字段，但会在采集器processor中增加上，因为方便业务系统聚合查询)
     * */
    @Field(type = FieldType.Double)
    private Double perConsumption;

    /**
     * 人均消费笔数(虽然银联原数据中没有该字段，但会在采集器processor中增加上，因为方便业务系统聚合查询)
     * */
    @Field(type = FieldType.Double)
    private Double perConsumptionPens;

    public CountyConsumptionCity() {
    }

    public CountyConsumptionCity(String consumeCountry, String sourceProvince, String sourceCity, String travellerType, String dealDay, Double transAt, Integer transNum, Integer acctNum, Double perConsumption, Double perConsumptionPens) {
        this.consumeCountry = consumeCountry;
        this.sourceProvince = sourceProvince;
        this.sourceCity = sourceCity;
        this.travellerType = travellerType;
        this.dealDay = dealDay;
        this.transAt = transAt;
        this.transNum = transNum;
        this.acctNum = acctNum;
        this.perConsumption = perConsumption;
        this.perConsumptionPens = perConsumptionPens;
    }
}
