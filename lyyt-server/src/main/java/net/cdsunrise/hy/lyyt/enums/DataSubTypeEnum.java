package net.cdsunrise.hy.lyyt.enums;

import lombok.AllArgsConstructor;

/**
 * 数据子类型枚举
 */
@AllArgsConstructor
public enum DataSubTypeEnum {

    /**
     * 空
     */
    NULL("", "空"),

    /**
     * 涉旅资源
     */
    BOUTIQUE_SCENIC("boutiqueScenic", "精品景区"),
    CULTURAL_PROTECTION_POINT("culturalProtectionPoint", "文物保护点"),
    SHOP_PLACE("shop", "购物场所"),
    FEATURED_GOODS("featuredGoods", "特色商品"),
    ENTERTAINMENT("entertainment", "娱乐场所"),
    CATERING("catering", "餐饮行业"),
    ACCOMMODATION("accommodation", "酒店住宿"),
    TRAVEL_AGENCY("travelAgency", "旅行社"),
    TRAVEL_RELATED_ENTERPRISES("travelRelatedEnterprises", "涉旅企业"),
    PROJECT_BUILD("projectBuild", "重点项目建设"),

    /**
     * 商户
     */
    TOURIST_MERCHANT("touristMerchant", "旅游商户"),

    /**
     * 公共资源
     */
    TOURIST_TOILET("touristToilet", "旅游厕所"),
    REPAIR_SHOP("autoRepairShop", "汽车修理厂"),
    GAS_STATION("gasStation", "加油站"),
    PHARMACY("pharmacy", "药房药店"),
    HOSPITAL("hospital", "医院"),
    BANK("bank", "银行/ATM"),
    POLICE_STATION("policeStation", "派出所"),

    /**
     * 智能设备
     */
    JUNCTION("junction", "卡口监控"),
    YDP("ydp", "LED信息屏"),

    /**
     * 交通资源
     */
    PARKING_LOT_RESOURCES("parkingLotResources", "停车场"),
    TOURIST_ROAD_RESOURCES("touristRoadResources", "旅游干道"),
    ;

    /**
     * 类型
     */
    private String type;
    /**
     * 名称
     */
    private String name;


    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }


}
