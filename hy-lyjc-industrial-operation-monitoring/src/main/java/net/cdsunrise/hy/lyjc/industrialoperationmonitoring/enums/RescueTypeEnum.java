package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lijiafeng 2021/01/18 15:17
 */
public enum RescueTypeEnum {

    TYPE_1(1, "道路救援"),
    TYPE_2(2, "消防救援"),
    TYPE_3(3, "医疗救援"),
    TYPE_4(4, "自然灾害救援"),
    TYPE_5(5, "综合性救援"),
    TYPE_6(99, "其他");

    private static final Map<Integer, RescueTypeEnum> MAP = Arrays.stream(values())
            .collect(Collectors.toMap(RescueTypeEnum::getType, Function.identity()));

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 类型描述
     */
    private final String typeDesc;

    RescueTypeEnum(Integer type, String typeDesc) {
        this.type = type;
        this.typeDesc = typeDesc;
    }

    public static RescueTypeEnum getByType(Integer type) {
        return MAP.get(type);
    }

    public Integer getType() {
        return type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }
}
