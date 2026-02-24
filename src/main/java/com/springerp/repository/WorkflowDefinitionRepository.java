package com.springerp.repository;

import com.springerp.entity.WorkflowDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkflowDefinitionRepository extends JpaRepository<WorkflowDefinition, Long> {

    Optional<WorkflowDefinition> findByWorkflowCode(String workflowCode);

    Optional<WorkflowDefinition> findByWorkflowName(String workflowName);

    Page<WorkflowDefinition> findByProcessType(WorkflowDefinition.ProcessType processType, Pageable pageable);

    Page<WorkflowDefinition> findByIsActive(Boolean isActive, Pageable pageable);

    Page<WorkflowDefinition> findByTenantId(Long tenantId, Pageable pageable);
}

