package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 区域类型枚举
 * @author LiuYin
 */
public enum RegionTypeEnum {

    /**
     * 境内，指的是中国大陆
     */
    INSIDE(1,"境内"),
    /**
     * 境外，指的是大陆之外，如:其他国家; 以及中国台湾、中国香港、中国澳门等
     */
    OUTSIDE(2,"境外"),
    /**
     * 中国，指的是中国大陆及中国台湾、中国香港、中国澳门
     */
    CHINA(3,"中国"),
    ;


    private int type;
    private String description;

    public static final Map<Integer, RegionTypeEnum> TYPE_MAP =
            Collections.unmodifiableMap(Arrays.stream(RegionTypeEnum.values()).collect(Collectors.toMap(RegionTypeEnum::getType, Function.identity())));


    RegionTypeEnum(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public static boolean isTypeExist(Integer type){
        return Objects.nonNull(type) || getTypeMap().containsKey(type);
    }

    public static RegionTypeEnum getTypeEnum(Integer type){
        return getTypeMap().get(type);
    }


    public static Map<Integer, RegionTypeEnum> getTypeMap() {
        return TYPE_MAP;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
