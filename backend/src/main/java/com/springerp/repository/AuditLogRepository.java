package com.springerp.repository;

import com.springerp.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByEntityType(String entityType, Pageable pageable);

    Page<AuditLog> findByEntityId(Long entityId, Pageable pageable);

    Page<AuditLog> findByUserId(Long userId, Pageable pageable);

    Page<AuditLog> findByAction(String action, Pageable pageable);

    List<AuditLog> findByEntityTypeAndEntityIdOrderByTimestampDesc(String entityType, Long entityId);

    Page<AuditLog> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    Page<AuditLog> findByTenantId(Long tenantId, Pageable pageable);
}

