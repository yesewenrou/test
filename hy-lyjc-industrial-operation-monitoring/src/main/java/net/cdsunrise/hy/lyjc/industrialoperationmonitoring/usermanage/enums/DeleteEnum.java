package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums;

/**
 * @author LHY
 */
public enum DeleteEnum {
    /**
     * 已删除
     */
    DELETE(1, "已删除"),
    /**
     * 未删除
     */
    NOT_DELETE(0, "未删除");

    private Integer code;
    private String message;

    DeleteEnum() {
    }

    DeleteEnum(Integer code, String message) {
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
