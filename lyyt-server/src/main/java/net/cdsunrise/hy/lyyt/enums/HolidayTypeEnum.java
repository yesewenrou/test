package net.cdsunrise.hy.lyyt.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 节假日枚举
 */
@AllArgsConstructor
public enum HolidayTypeEnum {

    /** 元旦节*/
    YDJ("ydj","元旦节"),
    /** 春节*/
    CJ("cj","春节"),
    /** 清明节*/
    QMJ("qmj","清明节"),
    /** 劳动节*/
    LDJ("ldj","劳动节"),
    /** 端午节*/
    DWJ("dwj","端午节"),
    /** 中秋节*/
    ZQJ("zqj","中秋节"),
    /** 国庆节*/
    GQJ("gqj","国庆节"),
    ;

    static final Map<String, HolidayTypeEnum> NAME_MAP =
            Collections.unmodifiableMap(Arrays.stream(HolidayTypeEnum.values()).collect(Collectors.toMap(HolidayTypeEnum::getName, Function.identity())));

    /**
     * 名称是否存在
     * @param name 名称，如：中秋节
     * @return 是否
     */
    public static boolean isNameExists(String name){
        return getNameMap().containsKey(name);
    }

    /**
     * 根据名称获取简称
     * @param name 名称，如：国庆节
     * @return 简称，如GQJ
     */
    public static String getShorteningByName(String name){
        final HolidayTypeEnum holidayTypeEnum = getNameMap().get(name);
        return Objects.isNull(holidayTypeEnum) ? null : holidayTypeEnum.getShortening();
    }


    /** 简写*/
    private String shortening;
    /** 名称*/
    private String name;

    public String getShortening() {
        return shortening;
    }

    public String getName() {
        return name;
    }

    public static Map<String, HolidayTypeEnum> getNameMap() {
        return NAME_MAP;
    }
}
