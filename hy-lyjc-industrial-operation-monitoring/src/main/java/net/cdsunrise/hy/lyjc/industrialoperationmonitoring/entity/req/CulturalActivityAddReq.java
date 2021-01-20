package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req;

import lombok.Data;

import java.util.List;

/**
 * @Author: suzhouhe @Date: 2020/3/26 14:18 @Description: 文化活动新增
 */
@Data
public class CulturalActivityAddReq {
    /**
     * 文化活动名称
     */
    private String culturalName;
    /**
     * 文化活动时间
     */
    private Long culturalTime;
    /**
     * 封面照片url 可以不传  传也只能传一个
     */
    private String coverPhotoUrl;
    /**
     * 附件urls 附件地址，可以传多个，必传字段
     */
    private List<String> fileUrls;
    /**
     * 附件名称
     */
    private List<String> fileNames;
}
