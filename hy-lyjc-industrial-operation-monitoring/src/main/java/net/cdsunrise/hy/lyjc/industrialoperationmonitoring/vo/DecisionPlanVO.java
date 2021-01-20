package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author fang yun long
 * on 2021/1/18
 */
@Data
public class DecisionPlanVO {
    /** id */
    private Long id;
    /** 名称 */
    private String name;
    /** 事件类型 */
    private String eventType;
    /** 事件类型名称 */
    private String eventTypeName;
    /** 事件等级 */
    private String eventLevel;
    /** 事件等级名称 */
    private String eventLevelName;
    /** 创建时间 */
    private Date gmtCreate;
    /** 修改时间 */
    private Date gmtModified;

    private List<AttachmentVO> attaches;
}
