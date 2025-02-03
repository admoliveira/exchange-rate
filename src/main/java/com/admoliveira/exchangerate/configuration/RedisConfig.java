package com.admoliveira.exchangerate.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    private final Duration ratesTtl;

    public RedisConfig(@Value("${exchange-rate.redis-config.rates.time-to-live}") final Duration ratesTtl) {

        this.ratesTtl = ratesTtl;
    }

    @Bean
    public RedisCacheManager cacheManager(final RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .withCacheConfiguration("rates", RedisCacheConfiguration.defaultCacheConfig().entryTtl(ratesTtl))
                .build();

    }

    @Bean
    public RedisTemplate<String, Long> redisTemplate(final RedisConnectionFactory factory) {
        final RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }
}
