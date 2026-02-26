package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Leave entity for managing employee leave requests and balances.
 */
@Entity
@Table(name = "leaves", indexes = {
        @Index(name = "idx_employee_id", columnList = "employee_id"),
        @Index(name = "idx_leave_start_date", columnList = "leave_start_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Leave extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "leave_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LeaveType leaveType; // CASUAL, SICK, EARNED, UNPAID, MATERNITY, PATERNITY

    @Column(name = "leave_start_date", nullable = false)
    private LocalDate leaveStartDate;

    @Column(name = "leave_end_date", nullable = false)
    private LocalDate leaveEndDate;

    @Column(name = "number_of_days", nullable = false)
    private Integer numberOfDays;

    @Column(name = "half_day", nullable = false)
    private Boolean halfDay = false;

    @Column(name = "half_day_type")
    @Enumerated(EnumType.STRING)
    private HalfDayType halfDayType; // FIRST_HALF, SECOND_HALF

    @Column(name = "reason", columnDefinition = "LONGTEXT")
    private String reason;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LeaveStatus status = LeaveStatus.PENDING; // PENDING, APPROVED, REJECTED, CANCELLED

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @Column(name = "approval_comments")
    private String approvalComments;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = true;

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum LeaveType {
        CASUAL, SICK, EARNED, UNPAID, MATERNITY, PATERNITY, BEREAVEMENT, COMPENSATORY, SABBATICAL
    }

    public enum HalfDayType {
        FIRST_HALF, SECOND_HALF
    }

    public enum LeaveStatus {
        PENDING, APPROVED, REJECTED, CANCELLED, WITHDRAWN
    }
}

