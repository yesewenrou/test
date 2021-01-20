package net.cdsunrise.hy.lyyt.enums;

/**
 * 采集状态
 */
public enum GatherStatusEnum {

    /**  采集成功*/
    SUCCESS(1,"成功"),
    /** 采集失败*/
    FAIL(2,"失败"),
    ;

    private Integer status;
    private String name;

    public Integer getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    GatherStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }}
