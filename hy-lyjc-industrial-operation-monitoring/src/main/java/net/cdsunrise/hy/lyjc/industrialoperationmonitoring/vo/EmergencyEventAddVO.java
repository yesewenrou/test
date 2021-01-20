package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author lijiafeng
 * @date 2019/11/27 16:12
 */
@Data
public class EmergencyEventAddVO {

    /**
     * 事件名称
     */
    @NotBlank(message = "事件名称不能为空")
    private String eventName;

    /**
     * 事件类型
     */
    @NotBlank(message = "事件类型不能为空")
    private String eventType;

    /**
     * 事件等级
     */
    @NotBlank(message = "事件等级不能为空")
    private String eventLevel;

    /**
     * 事件地址
     */
    @NotBlank(message = "事件地址不能为空")
    private String eventAddress;

    /**
     * 事件内容
     */
    @NotBlank(message = "事件内容不能为空")
    private String eventContent;

    /**
     * 关键词
     */
    private Set<String> keywords;

    /**
     * 事发时间
     */
    @NotNull(message = "事发时间不能为空")
    private Date eventTime;

    /**
     * 上报人电话
     */
    private String contact;

    /**
     * 现场照片
     */
    @Valid
    private List<EmergencyEventVO.Attachment> scenePhotos;

    /**
     * 资料附件
     */
    @Valid
    private List<EmergencyEventVO.Attachment> attachments;
}
