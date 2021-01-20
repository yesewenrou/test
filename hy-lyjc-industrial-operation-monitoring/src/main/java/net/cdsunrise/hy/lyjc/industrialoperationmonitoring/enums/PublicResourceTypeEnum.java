package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lijiafeng
 * @date 2020/1/16 21:07
 */
@Getter
@AllArgsConstructor
public enum PublicResourceTypeEnum {
    /**
     * 公共资源
     */
    PHARMACY("009001", "药房药店"),
    TOILET("009003", "旅游厕所"),
    BANK("009004", "银行/ATM"),
    HOSPITAL("009005", "医院"),
    POLICE_STATION("009007", "派出所"),
    GAS_STATION("009008", "加油站"),
    AUTO_REPAIR_SHOP("009009", "汽车修理厂");

    private String code;
    private String name;

    public static PublicResourceTypeEnum getByCode(String code) {
        for (PublicResourceTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
