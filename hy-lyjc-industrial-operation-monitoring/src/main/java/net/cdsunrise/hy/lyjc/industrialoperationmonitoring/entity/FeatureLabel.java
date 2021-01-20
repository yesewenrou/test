package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Binke Zhang
 * @date 2020/1/16 9:28
 */
@Data
@TableName("feature_label")
public class FeatureLabel {
    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;
    /** 标签名称 */
    private String labelName ;
    /** 更新人 */
    private Long updateBy ;
    /** 更新人 */
    private String updateName ;
    /** 更新时间 */
    private Date updateTime ;

}
