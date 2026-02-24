package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Workflow Definition entity for defining business process workflows.
 */
@Entity
@Table(name = "workflow_definitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowDefinition extends BaseEntity {

    @Column(name = "workflow_name", nullable = false, unique = true)
    private String workflowName;

    @Column(name = "workflow_code", nullable = false, unique = true)
    private String workflowCode;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "process_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProcessType processType; // INVOICE_APPROVAL, PO_APPROVAL, PAYMENT_APPROVAL, LEAVE_APPROVAL

    @Column(name = "workflow_definition", columnDefinition = "LONGTEXT")
    private String workflowDefinition; // JSON representation of workflow structure

    @Column(name = "version")
    private Integer version = 1;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "requires_approval", nullable = false)
    private Boolean requiresApproval = true;

    @Column(name = "max_approval_levels")
    private Integer maxApprovalLevels = 1;

    @Column(name = "timeout_days")
    private Integer timeoutDays;

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum ProcessType {
        INVOICE_APPROVAL, PO_APPROVAL, PAYMENT_APPROVAL, LEAVE_APPROVAL,
        EXPENSE_APPROVAL, ASSET_APPROVAL, DOCUMENT_APPROVAL, CUSTOM
    }
}

