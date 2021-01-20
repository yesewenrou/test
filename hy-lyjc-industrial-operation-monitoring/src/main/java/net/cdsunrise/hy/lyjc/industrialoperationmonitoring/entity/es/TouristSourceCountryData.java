package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author lijiafeng
 * @date 2019/9/26 14:23
 */
@Data
@Document(indexName = "tourist_source_country", type = "doc", replicas = 2)
public class TouristSourceCountryData {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(type = FieldType.Keyword)
    private String id;

    /**
     * 区域ID
     */
    @Field(type = FieldType.Keyword)
    private String scenicId;

    /**
     * 区域名称
     */
    @Field(type = FieldType.Keyword)
    private String scenicName;

    /**
     * 国家ID
     */
    @Field(type = FieldType.Keyword)
    private String countryId;

    /**
     * 国家名称
     */
    @Field(type = FieldType.Keyword)
    private String countryName;

    /**
     * 人数
     */
    @Field(type = FieldType.Integer)
    private Integer peopleNum;

    /**
     * 时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
    private String time;

    /**
     * 数据来源
     */
    @Field(type = FieldType.Keyword)
    private String datasource;

    /**
     * 标记：month、day、hour、minute
     */
    @Field(type = FieldType.Keyword)
    private String flag;
}
