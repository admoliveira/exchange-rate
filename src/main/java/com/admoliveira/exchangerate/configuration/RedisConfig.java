package com.admoliveira.exchangerate.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
@EnableConfigurationProperties(RedisConfigConfigProperties.class)
public class RedisConfig {

    private final RedisConfigConfigProperties configProperties;

    public RedisConfig(final RedisConfigConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Bean
    public RedisCacheManager cacheManager(final RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .withCacheConfiguration("rates",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(configProperties.rates().timeToLive()))
                .withCacheConfiguration("rate-limiter",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(configProperties.rateLimiter().timeToLive()))
                .build();
    }

    @Bean
    public RedisTemplate<String, Long> redisTemplate(final RedisConnectionFactory factory) {
        final RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }
}
