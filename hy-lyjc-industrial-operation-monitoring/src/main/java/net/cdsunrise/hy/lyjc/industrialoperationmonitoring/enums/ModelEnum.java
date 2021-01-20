package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

/**
 * @Author: LHY
 * @Date: 2019/9/20 17:07
 */
public enum ModelEnum {

    /**
     * 系统设置
     */
    SYSTEM_SET("系统设置"),
    /**
     * 行业管理
     */
    INDUSTRY_MANAGE("行业管理"),
    /**
     * 旅游管理
     */
    TOURISM_MANAGE("旅游管理"),
    /**
     * 公共管理
     */
    PUBLIC_MANAGE("公共管理"),
    /**
     * 投诉管理
     */
    COMPLAINT_MANAGE("投诉管理"),
    /**
     * 处理结果
     */
    HANDLE_RESULT("处理结果"),
    /**
     * 平台用户
     */
    PLATFORM_USER("平台用户"),
    /**
     * 角色权限
     */
    ROLE_PERMISSION("角色权限"),
    /**
     * 特色标签
     */
    FEATURE_LABEL("特色标签"),
    /**
     * 特色旅游资源
     */
    FEATURE_TOURISM_RESOURCE("特色旅游资源")
    ;

    ModelEnum(String modelName) {
        this.id = this.name().toLowerCase();
        this.modelName = modelName;
    }

    private String id;
    private String modelName;

    public String getId() {
        return id;
    }

    public String getModelName() {
        return modelName;
    }
}
