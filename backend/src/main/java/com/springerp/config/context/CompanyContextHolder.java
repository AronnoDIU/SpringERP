package com.springerp.config.context;

import org.springframework.stereotype.Component;

@Component
public class CompanyContextHolder {
    private static final ThreadLocal<Long> COMPANY_CONTEXT = new ThreadLocal<>();

    public void setCompanyId(Long companyId) {
        COMPANY_CONTEXT.set(companyId);
    }

    public Long getCompanyId() {
        Long companyId = COMPANY_CONTEXT.get();
        if (companyId == null) {
            throw new IllegalStateException("Company ID not set in current context");
        }
        return companyId;
    }

    public void clear() {
        COMPANY_CONTEXT.remove();
    }
}
