package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author sh
 * @date 2020-01-16 18:06
 */
@Document(indexName = "hy_feature_tourism_resource", type = "_doc")
@Data
public class FeatureTourismResourceData {
    /** 主键 */
    @Id
    private String id ;
    /** 资源名称 */
    private String resourceName ;
    /** 资源类型编码 */
    private String resourceTypeCode ;
    /** 资源类型名称 */
    private String resourceTypeName ;
    /** 所属区域编码 */
    private String regionalCode ;
    /** 所属区域名称 */
    private String regionalName ;
    /** 详细地址 */
    private String addressDetail ;
    /** 特色标签 */
    private String featureLabelIds ;
    /** 特色标签名称 */
    private String featureLabelNames ;
    /** 更新人 */
    private Long updateBy ;
    /** 更新人 */
    private String updateName ;
    /** 更新时间 */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS)
    private String updateTime ;
    /** 经度 */
    private Double longitude;
    /** 纬度 */
    private Double latitude;
}
