package net.cdsunrise.hy.lyyt.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: suzhouhe @Date: 2020/1/18 17:46 @Description:
 */
public class CameraNameUtil {

    /**
     *  摄像头名称映射 Map[本地名称 -> 远程名称]
     */
    public static final Map<String, String> REAL_REMOTE = new HashMap<String, String>() {{
        put("余坪镇白马村", "余坪镇白马村");
        put("将军镇河口大桥", "将军镇河口大桥");
        put("G351国道王沟", "G351国道王沟");
        put("广洪高速洪雅北出口", "广洪高速洪雅北出口主道");
        put("西环线牌坊坝路口", "西环线牌坊坝路口");
        put("槽渔滩镇竹菁关", "槽渔滩镇竹菁关");
        put("乐雅高速洪雅南出口", "乐雅高速洪雅南出口");
        put("瓦屋山镇丁湾路口", "瓦屋山镇丁湾路口");
        put("瓦屋山镇青龙村", "瓦屋山镇青龙村");
        put("七里坪镇华生温泉酒店路口", "七里坪镇华生温泉酒店路口");
        put("东岳镇乐雅高速出口", "东岳镇乐雅高速出口");
    }};

    public static String getRealName(String value) {
        Set set = REAL_REMOTE.entrySet();
        for (Object o : set) {
            Map.Entry entry = (Map.Entry) o;
            if (entry.getValue().equals(value)) {
                return (String) entry.getKey();
            }
        }
        return null;
    }
}
