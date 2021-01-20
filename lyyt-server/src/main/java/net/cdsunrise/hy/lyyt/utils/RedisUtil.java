package net.cdsunrise.hy.lyyt.utils;

import org.springframework.data.redis.core.*;

import java.util.Objects;

/**
 * @ClassName RedisUtil
 * @Description
 * @Author LiuYin
 * @Date 2019/12/25 14:11
 */
public class RedisUtil {


    /**
     * redis 模板（在系统初始化时候要加载）
     */
    private static StringRedisTemplate stringRedisTemplate;

    /**
     * 初始化模板（只执行1次）
     * @param template 模板
     */
    public static synchronized void setTemplateOnce(StringRedisTemplate template){
        if(Objects.isNull(stringRedisTemplate)){
            setStringRedisTemplate(template);
        }
    }

    private static void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        RedisUtil.stringRedisTemplate = stringRedisTemplate;
    }

    public static StringRedisTemplate getStringRedisTemplate(){
        return stringRedisTemplate;
    }

    public static HashOperations<String,String,String> hashOperations(){
        return getStringRedisTemplate().opsForHash();
    }

    public static ValueOperations<String,String> valueOperations(){
        return getStringRedisTemplate().opsForValue();
    }

    public static SetOperations<String,String> setOperations(){
        return getStringRedisTemplate().opsForSet();
    }

    public static ZSetOperations<String,String> zSetOperations(){
        return getStringRedisTemplate().opsForZSet();
    }

}
