package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lijiafeng
 * @date 2020/1/16 13:17
 */
@Getter
@AllArgsConstructor
public enum TravelRelatedResourceTypeEnum {

    /**
     * 涉旅资源
     */
    SCENIC("029001", "精品景区",null),
    CULTURAL("029002", "文物保护点",null),
    SHOP("029003", "购物场所","006004"),
    GOODS("029004", "特色商品",null),
    ENTERTAINMENT("029005", "娱乐场所","006003"),
    CATERING("029006", "餐饮行业","006001"),
    ACCOMMODATION("029007", "酒店住宿","006002"),
    AGENCY("029008", "旅行社",null),
    ENTERPRISES("029009", "涉旅企业",null);

    private String code;
    private String name;
    private String merchantType;

    public static TravelRelatedResourceTypeEnum getByCode(String code) {
        for (TravelRelatedResourceTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
