package com.springerp.service;

import com.springerp.entity.WorkflowDefinition;
import com.springerp.entity.WorkflowInstance;
import com.springerp.entity.WorkflowApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for workflow management and approvals.
 */
public interface WorkflowService {

    // Workflow Definition Management
    WorkflowDefinition createWorkflowDefinition(WorkflowDefinition definition);
    WorkflowDefinition updateWorkflowDefinition(Long id, WorkflowDefinition definition);
    Optional<WorkflowDefinition> getWorkflowDefinition(Long id);
    Optional<WorkflowDefinition> getWorkflowDefinitionByCode(String code);
    Page<WorkflowDefinition> getAllWorkflowDefinitions(Pageable pageable);
    Page<WorkflowDefinition> getWorkflowsByType(WorkflowDefinition.ProcessType type, Pageable pageable);

    // Workflow Instance Management
    WorkflowInstance initiateWorkflow(WorkflowDefinition definition, String entityType, Long entityId, Long initiatedBy);
    Optional<WorkflowInstance> getWorkflowInstance(Long id);
    Optional<WorkflowInstance> getWorkflowInstanceByNumber(String instanceNumber);
    Page<WorkflowInstance> getAllWorkflowInstances(Pageable pageable);
    Page<WorkflowInstance> getPendingWorkflows(Pageable pageable);
    Page<WorkflowInstance> getWorkflowsForApprover(Long approverId, Pageable pageable);
    Page<WorkflowInstance> getWorkflowsByEntity(String entityType, Long entityId, Pageable pageable);

    // Workflow Approval
    WorkflowApproval approveWorkflowStep(Long workflowInstanceId, Long approverId, String comments);
    WorkflowApproval rejectWorkflowStep(Long workflowInstanceId, Long approverId, String comments);
    WorkflowApproval delegateApproval(Long approvalId, Long delegatedTo);

    // Workflow Status
    WorkflowInstance completeWorkflow(Long workflowInstanceId);
    WorkflowInstance rejectWorkflow(Long workflowInstanceId, String reason);
    WorkflowInstance cancelWorkflow(Long workflowInstanceId, String reason);
    WorkflowInstance escalateWorkflow(Long workflowInstanceId);

    // Approval Tracking
    Page<WorkflowApproval> getWorkflowApprovals(Long workflowInstanceId, Pageable pageable);
    List<WorkflowApproval> getPendingApprovals(Long approverId);
    WorkflowApproval getApprovalAtStep(Long workflowInstanceId, Integer stepNumber);

    // Workflow Notifications
    void sendWorkflowNotifications(WorkflowInstance instance);
    void sendApprovalReminders();
    void handleExpiredWorkflows();

    // Reporting
    List<Object[]> getWorkflowMetrics(WorkflowDefinition.ProcessType type);
    int getAverageApprovalTime(WorkflowDefinition.ProcessType type);

    void deleteWorkflowDefinition(Long id);
    void deleteWorkflowInstance(Long id);
}

