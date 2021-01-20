package net.cdsunrise.hy.lyyt.utils;

import net.cdsunrise.common.utility.AssertUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @ClassName ConvertUtil
 * @Description
 * @Author LiuYin
 * @Date 2020/1/13 20:01
 */
public class ConvertUtil {


    public static <A,B,C> Map<A,C> convertMap(Map<A,B> map, Function<B,C> valueConverter){
        if(Objects.isNull(map) || map.isEmpty()){
            return new HashMap<>(0);
        }
        AssertUtil.notNull(valueConverter, () -> new RuntimeException("value converter is null"));
        final HashMap<A, C> resultMap = new HashMap<>(map.size());
        map.forEach((k,v) -> resultMap.put(k, valueConverter.apply(v)));
        return resultMap;
    }

    public static <A,B,C,D> Map<C,D> convertMap(Map<A,B> map, Function<A,C> keyConverter, Function<B,D> valueConverter){
        if(Objects.isNull(map) || map.isEmpty()){
            return new HashMap<>(0);
        }
        AssertUtil.notNull(keyConverter, () -> new RuntimeException("key converter is null"));
        AssertUtil.notNull(valueConverter, () -> new RuntimeException("value converter is null"));

        final HashMap<C, D> resultMap = new HashMap<>(map.size());
        map.forEach((k,v) -> resultMap.put(keyConverter.apply(k), valueConverter.apply(v)));
        return resultMap;
    }


}
