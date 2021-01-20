package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

/**
 * 预警列表可做的操作
 * @author fangyunlong
 * @date 2020/3/8 1:17
 */
public enum AutoWarningOperateEnum {
    /**
     * 忽略
     */
    IGNORE("IGNORE", "忽略"),
    /**
     * 申请发布
     */
    PUBLISH("PUBLISH", "申请发布"),
    /**
     * 短信
     */
    SMS("SMS", "短信"),
    /**
     * 自动过期
     */
    AUTO_EXPIRE("AUTO_EXPIRE", "自动过期"),

    /**
     * 忽略申请
     */
    REQUEST_PUBLISH_IGNORE("REQUEST_PUBLISH_IGNORE", "忽略申请")
    ;

    private String code;
    private String name;
    AutoWarningOperateEnum(){}
    AutoWarningOperateEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
