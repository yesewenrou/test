package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

/**
 * @Author: LHY
 * @Date: 2019/9/28 11:11
 */
public enum ScenicEnum {

    /**
     * 玉屏山
     */
    YUPINGSHAN("2830027", "玉屏山"),

    HY_COUNTY("511423","洪雅县");

    private final String code;

    private final String name;

    ScenicEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static boolean contain(String code) {
        for (ScenicEnum value : ScenicEnum.values()) {
            if (value.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static ScenicEnum getByCode(String code) {
        for (ScenicEnum value : ScenicEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
