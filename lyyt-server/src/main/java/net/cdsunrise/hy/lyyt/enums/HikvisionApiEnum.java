package net.cdsunrise.hy.lyyt.enums;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName enums
 * @Description
 * @Author LiuYin
 * @Date 2019/10/18 16:21
 */
public enum HikvisionApiEnum {

    /**
     * 按事件类型订阅事件
     */
    EVENT_SUBSCRIPTION_BY_EVENT_TYPES("/api/eventService/v1/eventSubscriptionByEventTypes", "按事件类型订阅事件"),

    /**
     * 查询事件订阅信息
     */
    EVENT_SUBSCRIPTION_VIEW("/api/eventService/v1/eventSubscriptionView", "查询事件订阅信息"),

    /**
     * 按事件类型取消订阅
     */
    EVENT_UN_SUBSCRIPTION_BY_EVENT_TYPES("/api/eventService/v1/eventUnSubscriptionByEventTypes", "按事件类型取消订阅"),

    /**
     * 分页获取监控点资源
     */
    CAMERAS("/api/resource/v1/cameras", "分页获取监控点资源"),

    /**
     * 获取监控点预览取流URL
     */
    CAMERAS_PREVIEW_URLS("/api/video/v1/cameras/previewURLs", "获取监控点预览取流URL"),
    ;

    /**
     * 访问地址
     */
    private String url;
    /**
     * 请求参数
     */
    private JSONObject jsonObject;
    /**
     * 描述
     */
    private String description;

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    HikvisionApiEnum(String url, String description) {
        this.url = url;
        this.description = description;
    }


}
