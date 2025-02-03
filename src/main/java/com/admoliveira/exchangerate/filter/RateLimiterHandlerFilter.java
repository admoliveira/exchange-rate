package com.admoliveira.exchangerate.filter;

import com.admoliveira.exchangerate.model.RateLimitBucket;
import com.admoliveira.exchangerate.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@Slf4j
@Component
public class RateLimiterHandlerFilter extends OncePerRequestFilter {

    private final RateLimiterService service;

    public RateLimiterHandlerFilter(final RateLimiterService service) {
        this.service = service;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final RateLimitBucket bucketResult = service.limit(request);

        response.addHeader("X-Rate-Limit-Limit", String.valueOf(bucketResult.limit()));
        response.addHeader("X-Rate-Limit-Remaining", String.valueOf(bucketResult.remaining()));
        response.addHeader("X-Rate-Limit-Reset", String.valueOf(bucketResult.reset()));

        if (bucketResult.isRequestAllowed()) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(TOO_MANY_REQUESTS.value());
        }
    }
}
