package com.springerp.context;

/**
 * Tenant context holder for multi-tenancy support.
 * Stores the current tenant ID in thread-local storage.
 */
public class TenantContext {

    private static final ThreadLocal<Long> tenantId = new ThreadLocal<>();

    public static void setTenantId(Long id) {
        tenantId.set(id);
    }

    public static Long getTenantId() {
        return tenantId.get();
    }

    public static void clear() {
        tenantId.remove();
    }

    public static boolean isSet() {
        return tenantId.get() != null;
    }
}

