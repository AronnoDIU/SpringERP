package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Permission entity for granular access control.
 */
@Entity
@Table(name = "permissions", indexes = {
        @Index(name = "idx_resource_action", columnList = "resource,action")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends BaseEntity {

    @Column(name = "permission_code", nullable = false, unique = true)
    private String permissionCode;

    @Column(name = "resource", nullable = false)
    private String resource; // e.g., "INVOICE", "EMPLOYEE", "REPORT"

    @Column(name = "action", nullable = false)
    private String action; // e.g., "CREATE", "READ", "UPDATE", "DELETE", "APPROVE"

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category; // e.g., "FINANCIAL", "HR", "INVENTORY"

    @Column(name = "is_system_permission", nullable = false)
    private Boolean isSystemPermission = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public String getDisplayName() {
        return resource + ":" + action;
    }
}

