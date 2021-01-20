package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

/**
 * 游客来源标识枚举
 * @author LiuYin
 */
public enum TouristSourceFlagType {

    /** 按照日统计*/
    DAY("day"),
    /** 按照月统计*/
    MONTH("month"),
    ;

    private String type;

    TouristSourceFlagType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
