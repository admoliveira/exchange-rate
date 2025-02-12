package com.admoliveira.exchangerate.filter;

import com.admoliveira.exchangerate.model.RateLimitStatus;
import com.admoliveira.exchangerate.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
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
    protected void doFilterInternal(@NonNull  final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {

        final RateLimitStatus rateLimitStatus = service.limit(request);

        response.addHeader("X-Rate-Limit-Limit", String.valueOf(rateLimitStatus.limit()));
        response.addHeader("X-Rate-Limit-Remaining", String.valueOf(rateLimitStatus.remaining()));
        response.addHeader("X-Rate-Limit-Reset", String.valueOf(rateLimitStatus.reset()));

        if (rateLimitStatus.isRequestAllowed()) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(TOO_MANY_REQUESTS.value());
        }
    }
}
