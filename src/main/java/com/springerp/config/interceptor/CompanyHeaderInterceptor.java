package com.springerp.config.interceptor;

import com.springerp.config.context.CompanyContextHolder;
import com.springerp.config.security.CustomHeaders;
import com.springerp.exception.InvalidHeaderException;
import com.springerp.exception.MissingHeaderException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class CompanyHeaderInterceptor implements HandlerInterceptor {

    private final CompanyContextHolder companyContextHolder;

    public CompanyHeaderInterceptor(CompanyContextHolder companyContextHolder) {
        this.companyContextHolder = companyContextHolder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String companyId = request.getHeader(CustomHeaders.COMPANY_ID);
        if (StringUtils.isBlank(companyId)) {
            throw new MissingHeaderException("Company ID header is required");
        }
        
        try {
            companyContextHolder.setCompanyId(Long.parseLong(companyId));
        } catch (NumberFormatException e) {
            throw new InvalidHeaderException("Invalid company ID format");
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        companyContextHolder.clear();
    }
}
