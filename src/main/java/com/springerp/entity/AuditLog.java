package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Audit Log entity for tracking all entity changes in the system.
 * Provides comprehensive audit trail for compliance and troubleshooting.
 */
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_entity_id", columnList = "entity_id"),
        @Index(name = "idx_entity_type", columnList = "entity_type"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_timestamp", columnList = "timestamp")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "action", nullable = false)
    private String action; // CREATE, UPDATE, DELETE

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Lob
    @Column(name = "old_values", columnDefinition = "LONGTEXT")
    private String oldValues; // JSON format

    @Lob
    @Column(name = "new_values", columnDefinition = "LONGTEXT")
    private String newValues; // JSON format

    @Column(name = "change_description", length = 1000)
    private String changeDescription;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "request_id")
    private String requestId;
}

