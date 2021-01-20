package net.cdsunrise.hy.lyyt.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据类型分类枚举
 * @author liuyin
 */
public enum DataTypeEnum {

    /** 目的地数据*/
    MDDSJ(1,"目的地数据"),
    SLQYJCSJ(2,"涉旅企业基础数据"),
    LYCYRYJCSJ(3,"旅游从业人员基础数据"),
    LYJTZYSJ(4,"旅游交通资源数据"),
    LYTCJCSJ(5,"旅游停车基础数据"),
    LYSHZYJCSJ(6,"旅游商户资源基础数据"),
    SLYJJCSJ(7,"涉旅硬件基础数据"),
    SLDMTK(8,"涉旅多媒体库"),
    CYSSSJ(9,"产业实时数据"),
    YKSJ(10,"游客数据"),
    PWSJ(11,"票务数据"),
    LYXMSJ(12,"旅游项目数据"),
    LYZXSJ(13,"旅游咨询数据"),
    LYZWSJ(14,"旅游政务数据"),
    HYJGSJ(15,"行业监管数据"),
    CYJYSJ(16,"产业经营数据"),
    YYSSJ(17,"运营商数据"),
    XMTSJ(18,"新媒体数据"),
    OTASJ(19,"OTA数据"),
    LYDSSJ(20,"旅游电商数据"),
    LYDTSJ(21,"旅游地图数据"),
    JJRZT(22,"节假日专题"),
    TSZT(23,"投诉专题"),
    YQZT(24,"舆情专题"),
    ;

    private Integer index;
    private String title;

    private static final Map<Integer,DataTypeEnum> INDEX_MAP =
            Collections.unmodifiableMap(Arrays.stream(DataTypeEnum.values()).collect(Collectors.toMap(DataTypeEnum::getIndex, Function.identity())));

    public Integer getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    DataTypeEnum(Integer index, String title) {
        this.index = index;
        this.title = title;
    }

    public static DataTypeEnum random(){
        return DataTypeEnum.values()[ThreadLocalRandom.current().nextInt(0, DataTypeEnum.values().length)];
    }

    public static Map<Integer, DataTypeEnum> getIndexMap() {
        return INDEX_MAP;
    }

    public static boolean isIndexExists(Integer index){
        return getIndexMap().containsKey(index);
    }
}
