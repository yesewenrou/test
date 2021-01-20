package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: suzhouhe @Date: 2020/3/26 14:04 @Description: 文化活动
 */
@Data
@TableName("hy_cultural_activity")
public class CulturalActivityDO {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 文化活动名称
     */
    private String culturalName;
    /**
     * 文化活动时间
     */
    private LocalDateTime culturalTime;
    /**
     * 封面照片url 可以不传  传也只能传一个
     */
    private String coverPhotoUrl;
    /**
     * 附件urls 附件地址，可以传多个，必传字段
     */
    private String fileUrls;
    /**
     * 附件名称
     */
    private String fileNames;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
