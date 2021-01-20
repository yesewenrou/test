package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

/**
 * 预警类型
 * @author fangyunlong
 * @date 2020/3/8 3:55
 */
public enum AutoWarningTypeEnum {
    /**
     * 预警类型 035001:客流量预警, 035002:停车位预警, 035003:车流量预警
     */
    AutoWarningType_1("035001", "客流量预警"),
    AutoWarningType_2("035002", "停车位预警"),
    AutoWarningType_3("035003", "车流量预警");

    private String code;
    private String name;

    AutoWarningTypeEnum() {
    }
    AutoWarningTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    public static AutoWarningTypeEnum getByCode(String code) {
        for (AutoWarningTypeEnum typeEnum : AutoWarningTypeEnum.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }
}
