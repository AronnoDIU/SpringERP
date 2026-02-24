package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * CompanyUser junction entity for many-to-many relationship between User and Company with role assignment.
 */
@Entity
@Table(name = "company_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "company_id"})
}, indexes = {
        @Index(name = "idx_company_user_user", columnList = "user_id"),
        @Index(name = "idx_company_user_company", columnList = "company_id"),
        @Index(name = "idx_company_user_role", columnList = "role_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyUser extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "is_company_admin", nullable = false)
    private Boolean isCompanyAdmin = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "notes")
    private String notes;

    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED, INVITED, ARCHIVED
    }
}

