package net.cdsunrise.hy.lyyt.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author YQ on 2019/11/6.
 */
@AllArgsConstructor
public enum ImportantRoadSection {
    /**
     * 三宝镇河口大桥
     */
    KK02("KK-02", "三宝镇河口大桥", "4d7e293ab5a248978b56132cfedd55aa"),
    KK03("KK-03", "G351国道", "44a8264ff3294bf8a561ca8950659151"),
    KK04("KK-04","遂洪高速洪雅北出口","f282fe0bc0e94a7fbde5186cabf51399"),
    KK05("KK-05","止戈镇乐雅高速出口","6673d73f117b49eaa26d6c63802694b2"),
    KK06("KK-06","槽鱼滩镇竹箐关","02cc52a4fb2447ee91433e1efcee4ee4"),
    KK07("KK-07","东岳镇乐雅高速出口","6673d73f117b49eaa26d6c63802694b2"),
    KK08("KK-08","西环线牌坊坝路口","ed0fce27da2646f5b876fbc2a1d3e8cc"),
    KK09("KK-09","瓦屋山镇青龙村","145ea229b9c14117baab905ea65fc8b3"),
    KK11("KK-11","华生温泉酒店路口","6ce5a2da3979422f9d042b76c2628516")
    ;

    @Getter
    private String code;
    @Getter
    private String name;
    @Getter
    private String deviceCode;

    public static List<ImportantRoadSectionClazz> list() {
        return Arrays.stream(ImportantRoadSection.values()).map(importantRoadSection -> {
            ImportantRoadSectionClazz importantRoadSectionClazz = new ImportantRoadSectionClazz();
            importantRoadSectionClazz.setCode(importantRoadSection.getCode());
            importantRoadSectionClazz.setDeviceCode(importantRoadSection.getDeviceCode());
            importantRoadSectionClazz.setName(importantRoadSection.getName());
            return importantRoadSectionClazz;
        }).collect(Collectors.toList());
    }

    @Data
    public static class ImportantRoadSectionClazz {
        private String code;
        private String name;
        private String deviceCode;
    }
}
