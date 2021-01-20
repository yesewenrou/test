package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lijiafeng
 * @date 2020/1/17 11:22
 */
@AllArgsConstructor
@Getter
public enum TalentResourceTypeEnum {

    /**
     * 人才类型
     */
    NARRATOR("030001", "中英文讲解员"),
    WORKER("030002", "旅游从业人员");

    private String code;
    private String name;

    public static TalentResourceTypeEnum getByCode(String code) {
        for (TalentResourceTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
