package com.admoliveira.exchangerate.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheConfigProperties.class)
public class CacheConfig {

    public static final String RATES_CACHE_NAME = "rates";
    public static final String RATE_LIMITER_CACHE_NAME = "rate-limiter";
    private final CacheConfigProperties configProperties;

    public CacheConfig(final CacheConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Bean
    public RedisCacheManager cacheManager(final RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .withCacheConfiguration(RATES_CACHE_NAME,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(configProperties.rates().timeToLive()))
                .withCacheConfiguration(RATE_LIMITER_CACHE_NAME,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(configProperties.rateLimiter().timeToLive()))
                .build();
    }


}
