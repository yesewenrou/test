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
 * @date 2019/9/26 13:44
 */
@Data
@Document(indexName = "tourist_local_data", type = "doc", replicas = 2)
public class TouristLocalData {

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
     * 人数
     */
    @Field(type = FieldType.Integer)
    private Integer peopleNum;

    /**
     * 人员类型 0为游客 1 为居住 2为上班
     */
    @Field(type = FieldType.Integer)
    private Integer memberType;

    /**
     * 时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom,
            pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS + "||" + DateUtil.PATTERN_YYYY_MM_DD + "||" + DateUtil.PATTERN_YYYY_MM)
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
