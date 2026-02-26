package com.springerp.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to extract and set tenant ID from request headers.
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantIdHeader = request.getHeader(TENANT_HEADER);

        if (tenantIdHeader != null && !tenantIdHeader.isEmpty()) {
            try {
                Long tenantId = Long.parseLong(tenantIdHeader);
                TenantContext.setTenantId(tenantId);
            } catch (NumberFormatException e) {
                // Invalid tenant ID format, will use default
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
    }
}

