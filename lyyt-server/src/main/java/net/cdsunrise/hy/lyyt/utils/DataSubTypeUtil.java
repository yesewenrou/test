package net.cdsunrise.hy.lyyt.utils;

import net.cdsunrise.hy.lyyt.enums.DataSubTypeEnum;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据二级分类工具
 * @author LiuYin 2020/2/5
 */
public class DataSubTypeUtil {


    /**
     * 名称对应子类型map
     */
    public static final Map<String, String> SUB_TYPE_NAME_MAP = Collections.unmodifiableMap(initMap());


    /**
     * 通过子类型name（中文）获取子类型type（英文）
     * @param name 类型名称，通常是中文
     * @return name
     */
    public static String getTypeByName(String name){
        return SUB_TYPE_NAME_MAP.get(name);
    }



    private static Map<String,String> initMap(){
        Map<String,String> map = new HashMap<>();
        map.put("餐饮", DataSubTypeEnum.CATERING.getType());
        map.put("娱乐",DataSubTypeEnum.ENTERTAINMENT.getType());
        map.put("住宿",DataSubTypeEnum.ACCOMMODATION.getType());
        map.put("购物",DataSubTypeEnum.SHOP_PLACE.getType());
        return map;
    }

    public static Map<String, String> getSubTypeNameMap() {
        return SUB_TYPE_NAME_MAP;
    }
}
