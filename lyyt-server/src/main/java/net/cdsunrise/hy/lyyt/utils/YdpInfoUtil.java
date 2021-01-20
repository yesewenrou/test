package net.cdsunrise.hy.lyyt.utils;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * @ClassName YdpInfoUtil
 * @Description 诱导屏工具类
 * @Author LiuYin
 * @Date 2019/12/20 17:07
 */
public class YdpInfoUtil {

    /** 默认状态*/
    private static final String DEFAULT_STATUS = "离线";

    /**
     * redis 模板
     */
    private static StringRedisTemplate stringRedisTemplate;

    /**
     * redis 中的key(hash)
     * 诱导屏信息-名称
     */
    private static final String KEY_YDP_NAME = "LYXXFB_YDP_NAME";
    /**
     * redis 中的key(hash)
     * 诱导屏信息-状态
     */
    private static final String KEY_YDP_STATUS = "LYXXFB_YDP_STATUS";


    /**
     * 更新诱导屏名称
     * @param id 诱导屏id
     * @param name 名称
     */
    public static void updateName(Long id, String name){
        checkId(id);
        checkString(name);
        getStringHashOperations().put(getKeyYdpName(), convertToNameField(id),name);
    }

    /**
     * 更新诱导屏状态
     * @param id 诱导屏id
     * @param netStatus 状态
     */
    public static void updateStatus(Long id, String netStatus){
        checkId(id);
        checkString(netStatus);
        getStringHashOperations().put(getKeyYdpStatus(), getStatusField(id), netStatus);
    }


    /**
     * 获取所有的诱导屏名称
     * @return &lt;id,名称&gt;
     */
    public static Map<String,String> getAllName(){
        return getStringHashOperations().entries(getKeyYdpName());
    }

    /**
     * 获取所有的诱导屏状态
     * @return &lt;id,状态&gt;
     */
    public static Map<String, String> getAllStatus() {
        return getStringHashOperations().entries(getKeyYdpStatus());
    }


    /**
     * 根据id获取诱导屏名称
     * @param id 诱导屏id
     * @return 名称
     */
    public static String getNameById(Long id){
        checkId(id);
        return getStringHashOperations().get(getKeyYdpName(), convertToNameField(id));
    }

    /**
     * 根据id获取诱导屏状态
     * @param id 诱导屏id
     * @return 状态（通常为“离线”或“在线”）
     */
    public  static String getStatusById(Long id){
        checkId(id);
        return getStringHashOperations().get(getKeyYdpStatus(), getStatusField(id));
    }


    /**
     * 获取hash操作器
     * @return hash操作器
     */
    private static HashOperations<String,String,String>  getStringHashOperations(){
        return getStringRedisTemplate().opsForHash();
    }

    /**
     * 校验id
     * @param id 诱导屏id
     */
    private static void checkId(Long id){
        Objects.requireNonNull(id, "id is null");
    }

    /**
     * 校验字符串
     * @param str 字符串
     */
    private static void checkString(String str){
        Objects.requireNonNull(str, "name or status is null");
    }

    /**
     * 转换成和名称相关的，在hash中的field
     * @param id 诱导屏id
     * @return field
     */
    private static String convertToNameField(Long id){
        return String.valueOf(id);
    }

    /**
     * 转换成和状态相关的，在hash中的field
     * @param id 诱导屏id
     * @return field
     */
    private static String getStatusField(Long id){
        return String.valueOf(id);
    }


    /**
     * 设置redis的模版（通常在系统初始化时进行）
     * @param stringRedisTemplate redis模板（string，string）
     */
    public static void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        if(Objects.isNull(getStringRedisTemplate())){
            YdpInfoUtil.stringRedisTemplate = stringRedisTemplate;
        }
    }


    private static StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    private static String getKeyYdpName() {
        return KEY_YDP_NAME;
    }

    private static String getKeyYdpStatus() {
        return KEY_YDP_STATUS;
    }

    /**
     * 获取默认的诱导屏状态
     * @return 默认状态
     */
    public static String getDefaultStatus() {
        return DEFAULT_STATUS;
    }
}
