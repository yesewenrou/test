package net.cdsunrise.hy.lyyt.cache;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Objects;

/**
 * @author lijiafeng
 * @date 2020/2/14 12:12
 */
public interface RedisCacheConfigAdapter {

    /**
     * 获取缓存名称
     *
     * @return 结果
     */
    String getCacheName();

    /**
     * 获取缓存有效期
     *
     * @return 结果
     */
    default Duration getTtl() {
        return Duration.ZERO;
    }

    /**
     * 获取缓存key前缀
     *
     * @return 结果
     */
    default String getKeyPrefix() {
        String cacheName = Objects.requireNonNull(getCacheName());
        return cacheName + "_";
    }

    /**
     * 获取缓存配置
     *
     * @return 结果
     */
    default RedisCacheConfiguration getCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .prefixKeysWith(getKeyPrefix())
                .entryTtl(getTtl())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class)));
    }
}
