package com.springerp.repository;

import com.springerp.entity.WorkflowInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, Long> {

    Optional<WorkflowInstance> findByInstanceNumber(String instanceNumber);

    Page<WorkflowInstance> findByWorkflowDefinitionId(Long workflowDefinitionId, Pageable pageable);

    Page<WorkflowInstance> findByEntityIdAndEntityType(Long entityId, String entityType, Pageable pageable);

    Page<WorkflowInstance> findByStatus(WorkflowInstance.Status status, Pageable pageable);

    Page<WorkflowInstance> findByCurrentApproverId(Long approverId, Pageable pageable);

    Page<WorkflowInstance> findByInitiatedBy(Long initiatedBy, Pageable pageable);

    List<WorkflowInstance> findByDueDateBeforeAndStatus(LocalDateTime dueDate, WorkflowInstance.Status status);

    Page<WorkflowInstance> findByPriority(WorkflowInstance.Priority priority, Pageable pageable);

    Page<WorkflowInstance> findByTenantId(Long tenantId, Pageable pageable);

    long countByStatus(WorkflowInstance.Status status);
}

