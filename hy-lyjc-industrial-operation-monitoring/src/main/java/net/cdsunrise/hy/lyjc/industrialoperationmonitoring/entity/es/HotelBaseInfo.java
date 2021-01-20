package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author LHY
 * @date 2019/10/30 14:18
 */
@Data
@Document(indexName = "hotel_base_info", type = "doc", replicas = 2)
public class HotelBaseInfo {

    @Id
    @Field(type = FieldType.Keyword)
    private String stationId;

    @Field(type = FieldType.Text,searchAnalyzer = "ik_smart",
            analyzer = "ik_smart")
    private String name;

    @Field(type = FieldType.Keyword)
    private String area;

    @Field(type = FieldType.Keyword)
    private String businessCircle;

    @Field(type = FieldType.Text,searchAnalyzer = "ik_smart",
            analyzer = "ik_smart")
    private String address;

    @Field(type = FieldType.Integer)
    private Integer bedNum;

    @Field(type = FieldType.Text)
    private String phoneNum;

}
