package net.cdsunrise.hy.lyyt.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: suzhouhe @Date: 2020/3/7 15:59 @Description: 路段监控点映射
 */
public class RoadSectionMonitorUtil {

    private static final int MAP_SIZE = 10;

    /**
     * 路段监控点映射map  [sectionId -> deviceCode]
     */
    public static final Map<String, String> ROAD_SECTION_MONITOR_SITE = new HashMap<String, String>(MAP_SIZE) {
        {
            //kk-01  monitorName:余坪镇白马村  roadSectionName:洪瓦路（东岳收费站-雅东路口）
            put("24-1", "398ddf2673d241d7bc48ee6c43ccfb35");
            //kk-02  monitorName:将军镇河口大桥  roadSectionName:成渝环线高速（三宝镇政府-魏河村）
            put("6-1", "b94364c7f1f74582a7970232bedb74d3");
            //kk-03  monitorName:G351国道王沟  roadSectionName:G351国道王沟路段（G351国道丹棱交界-G351国道王沟路口）
            put("27-1", "59764f601d5545bca162026a08a8d534");
            //kk-04  monitorName:广洪高速洪雅北出口  roadSectionName:大峨眉国际旅游西环线（瓦屋山大道-大峨眉国际旅游西环线）
            put("22-1", "cb68cf7a9cf444988af4ddef583d8b4f");
            //kk-05  monitorName:乐雅高速洪雅南出口  roadSectionName:玉屏北街（花溪大桥口-柳江古镇旅游景区）
            put("23-1","61c5c68af5554834b6e4c004def3598d");
            //kk-06  monitorName:槽鱼滩镇竹箐关  roadSectionName:槽鱼滩至东岳（自雅路-交警大队槽渔滩景区中队）
            put("2-1", "66bdedf035884af18ec5759ee3f16f42");
            //kk-07  monitorName:乐雅高速洪雅南出口  roadSectionName:九盛大道一段-洪州大桥（九盛大道一段-洪州大桥-止戈枢纽）
            put("4-2", "A0007");
            //kk-08  monitorName:西环线牌坊坝路口  roadSectionName:西环线牌坊坝路口（天宫路口-牌坊坝）
            put("26-1", "ac0f3c7beae1448a8c54cb6fefc75c29");
            //kk-09  monitorName:瓦屋山镇青龙村  roadSectionName:洪瓦路（吴庄中学-瓦屋山生态老腊肉养殖基地）
            put("11-1", "A0009");
            //kk-10  monitorName:瓦屋山镇丁湾路口  roadSectionName:雅赵路（雅赵路入县-白岩圷）
            put("13-1", "A0010");
            //kk-11  monitorName:七里坪镇华生温泉酒店路口  roadSectionName:峨洪路（云廊组团-峨眉半山七里坪）
            put("18-3", "1b291cb2ed52422d9b547e76f468aa6d");
        }
    };
}
