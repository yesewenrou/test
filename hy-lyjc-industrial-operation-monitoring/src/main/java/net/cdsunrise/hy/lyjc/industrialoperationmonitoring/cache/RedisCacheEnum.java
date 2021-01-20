package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.cache;

import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lijiafeng
 * @date 2020/2/14 13:05
 */
public enum RedisCacheEnum implements RedisCacheConfigAdapter {

    /**
     * 旅游监测
     */
    LYJC;

    @Override
    public String getCacheName() {
        return this.name();
    }

    public static Map<String, RedisCacheConfiguration> getConfigurationMap() {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>(values().length);
        for (RedisCacheEnum value : values()) {
            configurationMap.put(value.getCacheName(), value.getCacheConfig());
        }
        return configurationMap;
    }
}
