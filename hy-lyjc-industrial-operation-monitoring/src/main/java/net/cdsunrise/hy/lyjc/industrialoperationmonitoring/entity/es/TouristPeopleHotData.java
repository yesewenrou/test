package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author lijiafeng
 * @date 2019/9/26 14:23
 */
@Data
@Document(indexName = "tourist_people_hot", type = "doc", replicas = 2)
public class TouristPeopleHotData {

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
     * 经度
     */
    @Field(type = FieldType.Keyword)
    private String lng;

    /**
     * 纬度
     */
    @Field(type = FieldType.Keyword)
    private String lat;

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
