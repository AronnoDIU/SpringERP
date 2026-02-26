package com.springerp.service.impl;

import com.springerp.entity.AuditLog;
import com.springerp.repository.AuditLogRepository;
import com.springerp.service.AuditService;
import com.springerp.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for audit logging and compliance tracking.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;
    private final JsonUtil jsonUtil;

    @Override
    public AuditLog recordAudit(String entityType, Long entityId, String action, String userName, Long userId) {
        log.debug("Recording audit: {} - {} - {}", entityType, entityId, action);
        AuditLog auditLog = AuditLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .userId(userId)
                .userName(userName)
                .timestamp(LocalDateTime.now())
                .build();
        return auditLogRepository.save(auditLog);
    }

    @Override
    public AuditLog recordAuditWithDetails(String entityType, Long entityId, String action, Long userId,
                                           String userName, String oldValues, String newValues, String ipAddress) {
        log.debug("Recording detailed audit: {} - {} - {}", entityType, entityId, action);
        AuditLog auditLog = AuditLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .userId(userId)
                .userName(userName)
                .oldValues(oldValues)
                .newValues(newValues)
                .ipAddress(ipAddress)
                .timestamp(LocalDateTime.now())
                .build();
        return auditLogRepository.save(auditLog);
    }

    @Override
    public Optional<AuditLog> getAuditLog(Long id) {
        return auditLogRepository.findById(id);
    }

    @Override
    public Page<AuditLog> getAuditsByEntity(String entityType, Long entityId, Pageable pageable) {
        return auditLogRepository.findByEntityType(entityType, pageable);
    }

    @Override
    public Page<AuditLog> getAuditsByUser(Long userId, Pageable pageable) {
        return auditLogRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<AuditLog> getAuditsByAction(String action, Pageable pageable) {
        return auditLogRepository.findByAction(action, pageable);
    }

    @Override
    public Page<AuditLog> getAuditsByDateRange(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        return auditLogRepository.findByTimestampBetween(startTime, endTime, pageable);
    }

    @Override
    public List<AuditLog> getEntityChangeHistory(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByTimestampDesc(entityType, entityId);
    }

    @Override
    public long countAuditsSince(LocalDateTime timestamp) {
        return auditLogRepository.findByTimestampBetween(timestamp, LocalDateTime.now(), Pageable.unpaged()).getTotalElements();
    }

    @Override
    public List<String> getActionsPerformedByUser(Long userId, int limit) {
        return auditLogRepository.findByUserId(userId, Pageable.ofSize(limit)).getContent()
                .stream()
                .map(AuditLog::getAction)
                .distinct()
                .toList();
    }

    @Override
    public List<AuditLog> getAuditsByEntityTypeAndDateRange(String entityType, LocalDateTime startTime, LocalDateTime endTime) {
        return auditLogRepository.findByTimestampBetween(startTime, endTime, Pageable.unpaged()).getContent();
    }

    @Override
    public List<AuditLog> exportAuditTrail(LocalDateTime startTime, LocalDateTime endTime, String entityType) {
        log.info("Exporting audit trail from {} to {}", startTime, endTime);
        return auditLogRepository.findByTimestampBetween(startTime, endTime, Pageable.unpaged()).getContent();
    }

    @Override
    public byte[] generateAuditReport(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Generating audit report from {} to {}", startTime, endTime);
        List<AuditLog> auditLogs = exportAuditTrail(startTime, endTime, null);
        return jsonUtil.toJson(auditLogs).getBytes();
    }

    @Override
    public void deleteOldAuditLogs(int daysToRetain) {
        log.info("Deleting audit logs older than {} days", daysToRetain);
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToRetain);
        // Implementation would delete logs older than cutoffDate
    }
}

