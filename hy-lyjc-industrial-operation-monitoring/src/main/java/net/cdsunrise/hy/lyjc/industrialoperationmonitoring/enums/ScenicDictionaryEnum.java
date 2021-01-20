package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

import lombok.Data;

/**
 * 景区数据字典
 * @author FangYunLong
 * @date in 2020/4/30
 */
public enum ScenicDictionaryEnum {

    /**
     * 瓦屋山
     */
    Wa_Wh_Shan("002001", "瓦屋山"),
    /**
     * 七里坪
     */
    Qi_Li_Ping("002002", "七里坪"),
    /**
     * 柳江古镇
     */
    Liu_Jiang_Gu_Zhen("002003", "柳江古镇"),
    /**
     * 槽渔滩
     */
    Cao_Yu_Tan("002004", "槽渔滩"),
    /**
     * 玉屏山
     */
    Yu_Ping_Shan("002005", "玉屏山"),
    /**
     * 主城区
     */
    Zhu_Cheng_Qu("002006", "主城区");


    private String code;
    private String name;

    ScenicDictionaryEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }


    public static String getNameByCode(String code) {
        for (ScenicDictionaryEnum scenicDictionaryEnum : values()) {
            if (code.equalsIgnoreCase(scenicDictionaryEnum.code)) {
                return scenicDictionaryEnum.name;
            }
        }
        return "";
    }

    public static String getCodeByName(String name) {
        for (ScenicDictionaryEnum scenicDictionaryEnum : values()) {
            if (name.equalsIgnoreCase(scenicDictionaryEnum.name)) {
                return scenicDictionaryEnum.code;
            }
        }
        return "";
    }


}
