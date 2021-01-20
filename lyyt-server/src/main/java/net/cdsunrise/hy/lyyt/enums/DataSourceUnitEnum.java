package net.cdsunrise.hy.lyyt.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据源（相关单位）枚举
 */
public enum DataSourceUnitEnum {

    /** 气象局*/
    QXJ(1,"气象局"),
    STHJJ(2,"生态环境局"),
    WJJ(3,"卫建局"),
    JDGLJ(4,"监督管理局"),
    GAJ(5,"公安局"),
    JTYSGLJ(6,"交通运输管理局"),
    STSJPT(7,"省厅数据平台"),
    WLSJ(8,"网络数据"),
    JQSJ(9,"景区数据"),
    WHLYJ(10,"文化旅游局"),
    SSJPT(11,"市数据平台"),
    ZJSB(12,"自建设备"),
    ;

    private Integer index;
    private String name;
    private static final Map<Integer, DataSourceUnitEnum> INDEX_MAP =
            Collections.unmodifiableMap(Arrays.stream(DataSourceUnitEnum.values()).collect(Collectors.toMap(DataSourceUnitEnum::getIndex,Function.identity())));

    public Integer getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    DataSourceUnitEnum(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

    public static Map<Integer, DataSourceUnitEnum> getIndexMap() {
        return INDEX_MAP;
    }

    public static boolean isIndexExist(Integer index){
        return getIndexMap().containsKey(index);
    }
}
