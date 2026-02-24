package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Budget entity for financial planning and forecasting.
 */
@Entity
@Table(name = "budgets", indexes = {
        @Index(name = "idx_account_id", columnList = "account_id"),
        @Index(name = "idx_budget_period", columnList = "budget_period"),
        @Index(name = "idx_department_id", columnList = "department_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget extends BaseEntity {

    @Column(name = "budget_name", nullable = false)
    private String budgetName;

    @Column(name = "budget_code", nullable = false, unique = true)
    private String budgetCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private ChartOfAccounts account;

    @Column(name = "department_id")
    private Long departmentId; // References Department

    @Column(name = "budget_period", nullable = false)
    private LocalDate budgetPeriod; // Start of period (e.g., 2024-01-01)

    @Column(name = "budget_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal budgetAmount;

    @Column(name = "actual_amount", precision = 19, scale = 2)
    private BigDecimal actualAmount = BigDecimal.ZERO;

    @Column(name = "variance", precision = 19, scale = 2)
    private BigDecimal variance = BigDecimal.ZERO;

    @Column(name = "variance_percentage", precision = 5, scale = 2)
    private BigDecimal variancePercentage = BigDecimal.ZERO;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT; // DRAFT, APPROVED, ACTIVE, CLOSED, ARCHIVED

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @Column(name = "budget_notes")
    private String budgetNotes;

    @Column(name = "is_recurring", nullable = false)
    private Boolean isRecurring = false;

    @Column(name = "alert_threshold", precision = 5, scale = 2)
    private BigDecimal alertThreshold; // Percentage

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum Status {
        DRAFT, SUBMITTED, APPROVED, ACTIVE, CLOSED, ARCHIVED
    }
}

