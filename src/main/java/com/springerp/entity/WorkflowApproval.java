package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Workflow Step Approval entity for tracking individual approvals in a workflow.
 */
@Entity
@Table(name = "workflow_approvals", indexes = {
        @Index(name = "idx_workflow_instance_id", columnList = "workflow_instance_id"),
        @Index(name = "idx_approver_id", columnList = "approver_id"),
        @Index(name = "idx_approval_status", columnList = "approval_status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowApproval extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_instance_id")
    private WorkflowInstance workflowInstance;

    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @Column(name = "approver_id", nullable = false)
    private Long approverId;

    @Column(name = "approver_name")
    private String approverName;

    @Column(name = "approval_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING; // PENDING, APPROVED, REJECTED, DELEGATED

    @Column(name = "comments", columnDefinition = "LONGTEXT")
    private String comments;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "delegated_to")
    private Long delegatedTo;

    @Column(name = "delegation_date")
    private LocalDateTime delegationDate;

    @Column(name = "reminder_sent", nullable = false)
    private Boolean reminderSent = false;

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED, DELEGATED, SKIPPED
    }
}

