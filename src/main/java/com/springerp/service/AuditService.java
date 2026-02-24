package com.springerp.service;

import com.springerp.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for audit logging and compliance tracking.
 */
public interface AuditService {

    // Audit Log Recording
    AuditLog recordAudit(String entityType, Long entityId, String action, String userName, Long userId);
    AuditLog recordAuditWithDetails(String entityType, Long entityId, String action, Long userId, String userName,
                                     String oldValues, String newValues, String ipAddress);

    // Audit Log Retrieval
    Optional<AuditLog> getAuditLog(Long id);
    Page<AuditLog> getAuditsByEntity(String entityType, Long entityId, Pageable pageable);
    Page<AuditLog> getAuditsByUser(Long userId, Pageable pageable);
    Page<AuditLog> getAuditsByAction(String action, Pageable pageable);
    Page<AuditLog> getAuditsByDateRange(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    List<AuditLog> getEntityChangeHistory(String entityType, Long entityId);

    // Compliance & Reporting
    long countAuditsSince(LocalDateTime timestamp);
    List<String> getActionsPerformedByUser(Long userId, int limit);
    List<AuditLog> getAuditsByEntityTypeAndDateRange(String entityType, LocalDateTime startTime, LocalDateTime endTime);

    // Audit Trail Export
    List<AuditLog> exportAuditTrail(LocalDateTime startTime, LocalDateTime endTime, String entityType);
    byte[] generateAuditReport(LocalDateTime startTime, LocalDateTime endTime);

    // Audit Cleanup (for retention policies)
    void deleteOldAuditLogs(int daysToRetain);
}

