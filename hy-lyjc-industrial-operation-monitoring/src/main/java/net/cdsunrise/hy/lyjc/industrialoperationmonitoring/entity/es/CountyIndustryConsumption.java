package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author LHY
 * @date 2019/12/2 9:47
 *
 * 各行业旅游消费对总体消费的贡献度
 */
@Data
@Document(indexName = "county_industry_consumption", type = "doc", replicas = 2)
public class CountyIndustryConsumption {

    private String id;

    /**
     * 区县
     * */
    @Field(type = FieldType.Keyword)
    private String consumeCountry;

    /**
     * 行业
     * */
    @Field(type = FieldType.Keyword)
    private String type;

    /**
     * 日期，格式：2019-11-28
     * */
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String dealDay;

    /**
     * 游客消费金额
     * */
    @Field(type = FieldType.Double)
    private Double transAt;

    /**
     * 游客消费笔数
     * */
    @Field(type = FieldType.Integer)
    private Integer transNum;

    /**
     * 游客消费人次
     * */
    @Field(type = FieldType.Integer)
    private Integer acctNum;

    /**
     * 行业总消费金额
     * */
    @Field(type = FieldType.Double)
    private Double transAtTotal;

    /**
     * 游客消费额金额贡献度
     * */
    @Field(type = FieldType.Double)
    private Double transAtRatio;

    public CountyIndustryConsumption() {
    }

    public CountyIndustryConsumption(String consumeCountry, String type, String dealDay, Double transAt, Integer transNum, Integer acctNum, Double transAtTotal, Double transAtRatio) {
        this.consumeCountry = consumeCountry;
        this.type = type;
        this.dealDay = dealDay;
        this.transAt = transAt;
        this.transNum = transNum;
        this.acctNum = acctNum;
        this.transAtTotal = transAtTotal;
        this.transAtRatio = transAtRatio;
    }
}
