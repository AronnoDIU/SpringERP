package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Tenant entity for multi-tenancy support.
 * Represents separate business entities/organizations in the system.
 */
@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_name", nullable = false, unique = true)
    private String tenantName;

    @Column(name = "tenant_identifier", nullable = false, unique = true)
    private String tenantIdentifier;

    @Column(name = "description")
    private String description;

    @Column(name = "admin_email", nullable = false)
    private String adminEmail;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "subscription_tier")
    private String subscriptionTier; // BASIC, PROFESSIONAL, ENTERPRISE

    @Column(name = "max_users")
    private Integer maxUsers = 10;

    @Column(name = "max_storage_gb")
    private Integer maxStorageGB = 100;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "trial_end_date")
    private LocalDateTime trialEndDate;

    @Column(name = "contract_end_date")
    private LocalDateTime contractEndDate;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

