package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * MonitorUtil
 * 监控点位工具类（卡口工具类）
 * @author LiuYin
 * @date 2020/4/1 15:55
 */
public class MonitorUtil {

    private static final int MAP_SIZE = 16;

    private static final Map<String,String> nameMap = Collections.unmodifiableMap(initMap());

    public static Map<String,String> getNameMap(){
        return nameMap;
    }

    public static String getNameByMonitorCode(String monitorCode){
        return getNameMap().get(monitorCode);
    }


    private static Map<String,String> initMap(){
        final HashMap<String, String> map = new HashMap<>(MAP_SIZE);
        map.put("KK-01","余坪镇白马村");
        map.put("KK-02","将军镇河口大桥");
        map.put("KK-03","G351国道王沟");
        map.put("KK-04","广洪高速洪雅北出口");
        map.put("KK-05","乐雅高速洪雅南出口");
        map.put("KK-06","槽渔滩镇竹箐关");
        map.put("KK-07","东岳镇乐雅高速出口");
        map.put("KK-08","西环线牌坊坝路口");
        map.put("KK-09","瓦屋山镇青龙村");
        map.put("KK-10","瓦屋山镇丁湾路口");
        map.put("KK-11","七里坪镇华生温泉酒店路口");

        return map;

    }

}
