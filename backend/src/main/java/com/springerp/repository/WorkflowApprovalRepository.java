package com.springerp.repository;

import com.springerp.entity.WorkflowApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkflowApprovalRepository extends JpaRepository<WorkflowApproval, Long> {

    Page<WorkflowApproval> findByWorkflowInstanceId(Long workflowInstanceId, Pageable pageable);

    Page<WorkflowApproval> findByApproverId(Long approverId, Pageable pageable);

    Page<WorkflowApproval> findByApprovalStatus(WorkflowApproval.ApprovalStatus status, Pageable pageable);

    List<WorkflowApproval> findByWorkflowInstanceIdAndStepNumber(Long workflowInstanceId, Integer stepNumber);

    Page<WorkflowApproval> findByDueDateBeforeAndApprovalStatus(LocalDateTime dueDate, WorkflowApproval.ApprovalStatus status, Pageable pageable);

    List<WorkflowApproval> findByDelegatedTo(Long delegatedTo);

    Page<WorkflowApproval> findByTenantId(Long tenantId, Pageable pageable);

    long countByApprovalStatus(WorkflowApproval.ApprovalStatus status);
}

