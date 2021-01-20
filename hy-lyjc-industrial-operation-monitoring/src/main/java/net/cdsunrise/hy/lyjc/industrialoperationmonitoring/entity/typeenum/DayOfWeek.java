package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum;

/**
 * @author lijiafeng
 * @date 2019/11/25 10:42
 */
public enum DayOfWeek {
    /**
     * 星期
     */
    MONDAY("星期一"),
    TUESDAY("星期二"),
    WEDNESDAY("星期三"),
    THURSDAY("星期四"),
    FRIDAY("星期五"),
    SATURDAY("星期六"),
    SUNDAY("星期日");

    private String desc;

    DayOfWeek(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
