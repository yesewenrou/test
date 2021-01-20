package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

/**
 * 新闻模块
 *
 * @author YQ on 2020/3/7.
 */
@Data
public class NewsContentVO {
    /**
     * 内容
     */
    private String content;
    /**
     * id
     */
    private String id;
    /**
     * 图片路径
     */
    private String imgPath;
    /**
     * 简介
     */
    private String introduction;
    /**
     * 浏览量
     */
    private String lookNumber;
    /**
     * 发布时间
     */
    private String releaseTime;
    /**
     * 标题
     */
    private String title;
    /**
     * 类型码
     */
    private String typeCode;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 存在值则直接跳转到该url
     */
    private String url;
    /**
     * 跳转的url
     */
    private String forwardUrl;
}
