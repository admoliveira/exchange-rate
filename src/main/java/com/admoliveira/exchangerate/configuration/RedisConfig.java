package com.admoliveira.exchangerate.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    private final Duration ttl;

    public RedisConfig(@Value("${exchange-rate.redis-config.rates.time-to-live}") final Duration ttl) {
        this.ttl = ttl;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        final RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl);

        return RedisCacheManager.builder(redisConnectionFactory)
                .withCacheConfiguration("rates", RedisCacheConfiguration.defaultCacheConfig().entryTtl(ttl))
                .build();
    }
}
