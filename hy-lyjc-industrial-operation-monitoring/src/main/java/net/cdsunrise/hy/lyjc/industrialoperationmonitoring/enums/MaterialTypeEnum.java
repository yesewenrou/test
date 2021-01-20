package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MaterialTypeEnum
 * 物资类型枚举
 * @author LiuYin
 * @date 2021/1/17 11:35
 */
public enum MaterialTypeEnum {

    /** 器材工具*/
    EQUIPMENT_AND_TOOLS(1,"器材工具"),
    PUBLICITY_MATERIAL(2,"宣传材料"),
    PROTECTIVE_ARTICLE(3,"防护用品"),
    LIFE_RESCUE(4,"生命救助"),
    INTELLIGENT_HARDWARE(5,"智能硬件"),
    OTHER(6,"其他"),
    ;

    MaterialTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    private static final Map<Integer, MaterialTypeEnum> TYPE_MAP =
            Collections.unmodifiableMap(Arrays.stream(MaterialTypeEnum.values()).collect(Collectors.toMap(MaterialTypeEnum::getType, Function.identity())));

    private Integer type;

    private String name;

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static Map<Integer, MaterialTypeEnum> getTypeMap() {
        return TYPE_MAP;
    }

    public static boolean isExist(Integer type){
        return getTypeMap().containsKey(type);
    }

    public static MaterialTypeEnum getByType(Integer type){
        return getTypeMap().get(type);
    }
}
