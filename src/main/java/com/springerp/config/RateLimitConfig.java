package com.springerp.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class RateLimitConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(createBucket()))
                .addPathPatterns("/api/v1/**");
    }

    private Bucket createBucket() {
        Duration duration = Duration.ofMinutes(1);
        Refill refill = Refill.greedy(100, duration);
        Bandwidth limit = Bandwidth.classic(100, refill);
        
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
