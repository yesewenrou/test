package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.config;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.cache.RedisCacheEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author lijiafeng
 * @date 2019/10/23 13:49
 */
@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory);
        builder.withInitialCacheConfigurations(RedisCacheEnum.getConfigurationMap());
        return builder.build();
    }
}
