package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Workflow Instance entity for tracking execution of workflow processes.
 */
@Entity
@Table(name = "workflow_instances", indexes = {
        @Index(name = "idx_workflow_definition_id", columnList = "workflow_definition_id"),
        @Index(name = "idx_entity_id", columnList = "entity_id"),
        @Index(name = "idx_current_step", columnList = "current_step"),
        @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowInstance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_definition_id")
    private WorkflowDefinition workflowDefinition;

    @Column(name = "instance_number", nullable = false, unique = true)
    private String instanceNumber;

    @Column(name = "entity_type", nullable = false)
    private String entityType; // INVOICE, PO, PAYMENT, LEAVE, etc.

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "initiated_by", nullable = false)
    private Long initiatedBy;

    @Column(name = "initiated_at", nullable = false)
    private LocalDateTime initiatedAt;

    @Column(name = "current_step", nullable = false)
    private Integer currentStep = 0;

    @Column(name = "current_approver_id")
    private Long currentApproverId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.IN_PROGRESS; // IN_PROGRESS, APPROVED, REJECTED, CANCELLED, EXPIRED

    @Column(name = "approval_comments", columnDefinition = "LONGTEXT")
    private String approvalComments;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM; // LOW, MEDIUM, HIGH, URGENT

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum Status {
        PENDING, IN_PROGRESS, APPROVED, REJECTED, CANCELLED, EXPIRED, ON_HOLD
    }

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }
}

