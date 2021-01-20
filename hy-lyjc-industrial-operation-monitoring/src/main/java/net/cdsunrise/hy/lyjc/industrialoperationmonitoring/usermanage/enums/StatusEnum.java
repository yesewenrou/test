package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums;

/**
 * @author LHY
 */
public enum StatusEnum {
    /**
     * 已删除
     */
    ENABLE(1, "启用"),
    /**
     * 未删除
     */
    DISABLE(0, "禁用");

    private Integer code;
    private String message;

    StatusEnum() {
    }

    StatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
