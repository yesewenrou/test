package net.cdsunrise.hy.lyyt.config;

import net.cdsunrise.hy.lyyt.cache.RedisCacheEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @ClassName RedisConfiguration
 * @Description
 * @Author LiuYin
 * @Date 2019/11/5 17:57
 */
@Configuration
public class RedisConfiguration {

    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory connectionFactory) {
        final StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory);
        builder.withInitialCacheConfigurations(RedisCacheEnum.getConfigurationMap());
        return builder.build();
    }
}
