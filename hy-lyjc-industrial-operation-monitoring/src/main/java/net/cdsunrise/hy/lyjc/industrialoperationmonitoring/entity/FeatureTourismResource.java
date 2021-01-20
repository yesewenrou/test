package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Binke Zhang
 * @date 2020/1/16 9:29
 */
@Data
@TableName("feature_tourism_resource")
public class FeatureTourismResource {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 资源类型编码
     */
    private String resourceTypeCode;
    /**
     * 资源类型名称
     */
    private String resourceTypeName;
    /**
     * 所属区域编码
     */
    private String regionalCode;
    /**
     * 所属区域名称
     */
    private String regionalName;
    /**
     * 详细地址
     */
    private String addressDetail;
    /**
     * 特色标签
     */
    private String featureLabelIds;
    /**
     * 特色标签名称
     */
    private String featureLabelNames;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新人
     */
    private String updateName;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
}
