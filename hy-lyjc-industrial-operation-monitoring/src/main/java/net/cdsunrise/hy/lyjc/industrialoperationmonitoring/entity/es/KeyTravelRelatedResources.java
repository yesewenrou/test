package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author lijiafeng
 * @date 2020/3/8 16:14
 */
@Data
@Document(indexName = "key_travel_related_resources", type = "doc", replicas = 2)
public class KeyTravelRelatedResources {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    @Field(type = FieldType.Keyword)
    private String name;

    /**
     * 资源类型
     */
    @Field(type = FieldType.Keyword)
    private String mainType;

    /**
     * 资源细类
     */
    @Field(type = FieldType.Keyword)
    private String subType;

    /**
     * 区域编码
     */
    @Field(type = FieldType.Keyword)
    private String region;

    /**
     * 星级
     */
    @Field(type = FieldType.Keyword)
    private String starLevel;

    /**
     * 评定部门
     */
    @Field(type = FieldType.Keyword)
    private String department;

    /**
     * 地址
     */
    @Field(type = FieldType.Keyword)
    private String address;

    /**
     * 负责人
     */
    @Field(type = FieldType.Keyword)
    private String principal;

    /**
     * 联系电话
     */
    @Field(type = FieldType.Keyword)
    private String phone;

    /**
     * 评定时间或文号
     */
    @Field(type = FieldType.Keyword)
    private String symbol;

    /**
     * 关联对象 游客范围或酒店公安编码
     */
    @Field(type = FieldType.Keyword)
    private String link;
}
