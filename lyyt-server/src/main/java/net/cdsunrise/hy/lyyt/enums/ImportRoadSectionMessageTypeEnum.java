package net.cdsunrise.hy.lyyt.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 重点路段消息类型枚举
 * @author LiuYin
 */
public enum ImportRoadSectionMessageTypeEnum {

    /** 更新*/
    UPDATE("update"),
    /** 删除*/
    DELETE("delete"),
    ;
    private String type;


    private static final Map<String, ImportRoadSectionMessageTypeEnum> TYPE_MAP =
            Arrays.stream(ImportRoadSectionMessageTypeEnum.values()).collect(Collectors.toMap(ImportRoadSectionMessageTypeEnum::getType, Function.identity()));

    ImportRoadSectionMessageTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static Map<String, ImportRoadSectionMessageTypeEnum> getTypeMap() {
        return TYPE_MAP;
    }

    public static ImportRoadSectionMessageTypeEnum getByType(String type){
        return getTypeMap().get(type);
    }

}
