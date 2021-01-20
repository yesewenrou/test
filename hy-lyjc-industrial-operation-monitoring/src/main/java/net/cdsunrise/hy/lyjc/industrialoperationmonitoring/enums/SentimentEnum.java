package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

/**
 * @author LHY
 * @date 2019/11/15 11:55
 */
public enum SentimentEnum {
    /**
     * 正面
     */
    POSITIVE("1", "正面"),
    NEUTRAL("0","中性"),
    NEGATIVE("-1","负面");

    private final String number;

    private final String name;

    SentimentEnum(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public static SentimentEnum getByNumber(String number) {
        for (SentimentEnum value : SentimentEnum.values()) {
            if (value.getNumber().equals(number)) {
                return value;
            }
        }
        return null;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
