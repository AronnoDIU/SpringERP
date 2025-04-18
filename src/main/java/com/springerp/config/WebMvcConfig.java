package com.springerp.config;

import com.springerp.config.interceptor.CompanyHeaderInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CompanyHeaderInterceptor companyHeaderInterceptor;

    public WebMvcConfig(CompanyHeaderInterceptor companyHeaderInterceptor) {
        this.companyHeaderInterceptor = companyHeaderInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(companyHeaderInterceptor)
                .addPathPatterns("/api/v1/invoices/**");
    }
}
