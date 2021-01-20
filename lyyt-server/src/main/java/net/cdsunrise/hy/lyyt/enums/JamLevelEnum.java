package net.cdsunrise.hy.lyyt.enums;

/**
 * @author YQ on 2019/8/22.
 */
public enum JamLevelEnum {
    /**
     * 畅通
     */
    UNBLOCKED(2.00),
    /**
     * 缓行
     */
    SLOW(3.00),
    /**
     * 拥堵
     */
    JAM(5.00),
    /**
     * 非常拥堵
     */
    VERY_JAM(10.00),
    ;
    private Double value;

    JamLevelEnum(double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public static JamLevelEnum fetch(double tpi) {
        JamLevelEnum jamLevelEnum = JamLevelEnum.UNBLOCKED;
        if (tpi >= JamLevelEnum.UNBLOCKED.getValue() && tpi < JamLevelEnum.SLOW.getValue()) {
            jamLevelEnum = JamLevelEnum.SLOW;
        }else if (tpi >= JamLevelEnum.SLOW.getValue() && tpi < JamLevelEnum.JAM.getValue()) {
            jamLevelEnum = JamLevelEnum.JAM;
        }else if (tpi >= JamLevelEnum.JAM.getValue()) {
            jamLevelEnum = JamLevelEnum.VERY_JAM;
        }
        return jamLevelEnum;
    }
}
