package com.springerp.config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class RateLimitInterceptor implements HandlerInterceptor {
    private static final String RATE_LIMIT_REMAINING_HEADER = "X-Rate-Limit-Remaining";
    private static final String RATE_LIMIT_LIMIT_HEADER = "X-Rate-Limit-Limit";
    private final Bucket bucket;

    public RateLimitInterceptor(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, 
                           @NonNull HttpServletResponse response, 
                           @NonNull Object handler) {
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        
        if (probe.isConsumed()) {
            response.setHeader(RATE_LIMIT_REMAINING_HEADER, String.valueOf(probe.getRemainingTokens()));
            response.setHeader(RATE_LIMIT_LIMIT_HEADER, "100");
            return true;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader(HttpHeaders.RETRY_AFTER, String.valueOf(probe.getNanosToWaitForRefill() / 1_000_000_000));
        return false;
    }
}
