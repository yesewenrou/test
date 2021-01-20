package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lijiafeng 2021/01/14 18:46
 */
public enum ComplaintImportanceEnum {

    LOWER(9, "较低"),
    NORMAL(10, "一般"),
    HIGHER(11, "较高");

    private static final Map<Integer, ComplaintImportanceEnum> importanceMap = Arrays.stream(values())
            .collect(Collectors.toMap(ComplaintImportanceEnum::getImportance, Function.identity()));

    /**
     * 重要性
     */
    private final Integer importance;

    /**
     * 重要性描述
     */
    private final String ImportanceDesc;

    ComplaintImportanceEnum(Integer importance, String importanceDesc) {
        this.importance = importance;
        ImportanceDesc = importanceDesc;
    }

    public static ComplaintImportanceEnum getByImportance(Integer importance) {
        return importanceMap.get(importance);
    }

    public Integer getImportance() {
        return importance;
    }

    public String getImportanceDesc() {
        return ImportanceDesc;
    }
}
