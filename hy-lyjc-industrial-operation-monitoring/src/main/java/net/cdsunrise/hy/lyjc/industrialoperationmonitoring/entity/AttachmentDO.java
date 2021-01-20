package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * AttachmentDO
 * 附件
 * @author LiuYin
 * @date 2021/1/17 14:42
 */
@Data
@TableName("hy_attachment")
public class AttachmentDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 名称*/
    private String name;

    /** 地址*/
    private String url;

    /** 所属业务类型*/
    private String businessType;

    /** 所属业务类型id*/
    private Long businessId;

    /** 附件后缀*/
    private String suffix;
}
