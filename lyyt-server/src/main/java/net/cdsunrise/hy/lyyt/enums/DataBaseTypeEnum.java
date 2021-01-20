package net.cdsunrise.hy.lyyt.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据资源库类型枚举
 */
public enum DataBaseTypeEnum {

    /** 公共基础库*/
    PBA(1,"公共基础库"),
    /** 公共业务库*/
    PBU(2,"公共业务库"),
    /** 公共主题库*/
    PTO(3,"公共主题库"),
    ;

    private Integer index;
    private String name;

    DataBaseTypeEnum(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

    private static final Map<Integer, DataBaseTypeEnum> INDEX_MAP =
            Arrays.stream(DataBaseTypeEnum.values()).collect(Collectors.toMap(DataBaseTypeEnum::getIndex,Function.identity()));

    public static Map<Integer, DataBaseTypeEnum> getIndexMap() {
        return INDEX_MAP;
    }

    public Integer getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

}
