package com.admoliveira.exchangerate.filter;

import com.admoliveira.exchangerate.model.RateLimitStatus;
import com.admoliveira.exchangerate.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateLimiterHandlerFilterTest {

    @InjectMocks
    private RateLimiterHandlerFilter filter;

    @Mock
    private RateLimiterService rateLimiterService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private MockHttpServletRequest request;

    @Mock
    private MockHttpServletResponse response;


    @Test
    void allowedRequest() throws ServletException, IOException {
        final RateLimitStatus rateLimitStatus = new RateLimitStatus(100, 99, 12345L, true);
        when(rateLimiterService.limit(request)).thenReturn(rateLimitStatus);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).addHeader("X-Rate-Limit-Limit", "100");
        verify(response).addHeader("X-Rate-Limit-Remaining", "99");
        verify(response).addHeader("X-Rate-Limit-Reset", "12345");

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void blockedRequest() throws ServletException, IOException {
        final RateLimitStatus rateLimitStatus = new RateLimitStatus(100, 0, 12345L, false);
        when(rateLimiterService.limit(request)).thenReturn(rateLimitStatus);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).addHeader("X-Rate-Limit-Limit", "100");
        verify(response).addHeader("X-Rate-Limit-Remaining", "0");
        verify(response).addHeader("X-Rate-Limit-Reset", "12345");

        verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        verify(filterChain, never()).doFilter(request, response);
    }
}
