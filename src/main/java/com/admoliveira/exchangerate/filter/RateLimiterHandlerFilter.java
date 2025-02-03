package com.admoliveira.exchangerate.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@Slf4j
@Component
@EnableConfigurationProperties(RateLimiterHandlerFilterConfigProperties.class)
public class RateLimiterHandlerFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, Long> redisTemplate;
    private final Duration window;
    private final long windowMillis;
    private final int maxRequestsPerWindow;


    public RateLimiterHandlerFilter(final RedisTemplate<String, Long> redisTemplate,
                                    final RateLimiterHandlerFilterConfigProperties configProperties) {
        this.redisTemplate = redisTemplate;
        this.window = configProperties.window();
        this.windowMillis = window.toMillis();
        this.maxRequestsPerWindow = configProperties.maxRequestsPerWindow();
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        long slot = System.currentTimeMillis() / windowMillis;

        final String key = String.format("rl_%s_%s", request.getRemoteAddr(), slot);

        final Long redisCount = redisTemplate.opsForValue().increment(key);

        long count = redisCount == null ? 0 : redisCount;
        log.info("Rate limit for {}: {} ", key, count);
        if ( count <= 1) {
            redisTemplate.expire(key, window);
        }
        if (count >= maxRequestsPerWindow) {
            response.setStatus(TOO_MANY_REQUESTS.value());
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
