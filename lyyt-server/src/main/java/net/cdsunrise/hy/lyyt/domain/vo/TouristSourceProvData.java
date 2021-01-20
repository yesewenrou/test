package net.cdsunrise.hy.lyyt.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author LHY
 * @date 2019/11/21 19:16
 */
@Data
@Document(indexName = "tourist_source_prov", type = "doc", replicas = 2)
public class TouristSourceProvData {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Field(type = FieldType.Keyword)
    private String id;

    /**
     * 区域名称
     */
    @Field(type = FieldType.Keyword)
    private String scenicName;

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
     * 标记：month、day
     */
    @Field(type = FieldType.Keyword)
    private String flag;
}
