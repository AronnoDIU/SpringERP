package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Salary entity for managing employee payroll.
 */
@Entity
@Table(name = "salaries", indexes = {
        @Index(name = "idx_employee_id", columnList = "employee_id"),
        @Index(name = "idx_salary_month", columnList = "salary_month")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salary extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "salary_month", nullable = false)
    private LocalDate salaryMonth; // First day of the month

    @Column(name = "basic_salary", precision = 19, scale = 2, nullable = false)
    private BigDecimal basicSalary;

    @Column(name = "gross_salary", precision = 19, scale = 2)
    private BigDecimal grossSalary;

    @Column(name = "net_salary", precision = 19, scale = 2)
    private BigDecimal netSalary;

    @Column(name = "total_allowances", precision = 19, scale = 2)
    private BigDecimal totalAllowances = BigDecimal.ZERO;

    @Column(name = "total_deductions", precision = 19, scale = 2)
    private BigDecimal totalDeductions = BigDecimal.ZERO;

    @Column(name = "hra", precision = 19, scale = 2)
    private BigDecimal hra; // House Rent Allowance

    @Column(name = "dearness_allowance", precision = 19, scale = 2)
    private BigDecimal dearness_allowance; // Dearness Allowance

    @Column(name = "conveyance", precision = 19, scale = 2)
    private BigDecimal conveyance;

    @Column(name = "medical_allowance", precision = 19, scale = 2)
    private BigDecimal medicalAllowance;

    @Column(name = "other_allowances", precision = 19, scale = 2)
    private BigDecimal otherAllowances = BigDecimal.ZERO;

    @Column(name = "income_tax", precision = 19, scale = 2)
    private BigDecimal incomeTax = BigDecimal.ZERO;

    @Column(name = "provident_fund", precision = 19, scale = 2)
    private BigDecimal providentFund = BigDecimal.ZERO;

    @Column(name = "insurance", precision = 19, scale = 2)
    private BigDecimal insurance = BigDecimal.ZERO;

    @Column(name = "loan_deduction", precision = 19, scale = 2)
    private BigDecimal loanDeduction = BigDecimal.ZERO;

    @Column(name = "other_deductions", precision = 19, scale = 2)
    private BigDecimal otherDeductions = BigDecimal.ZERO;

    @Column(name = "working_days")
    private Integer workingDays;

    @Column(name = "actual_days_worked")
    private Integer actualDaysWorked;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING; // PENDING, PROCESSED, PAID, FAILED

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // BANK_TRANSFER, CHECK, CASH, DIGITAL

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum PaymentStatus {
        PENDING, APPROVED, PROCESSED, PAID, FAILED, CANCELLED
    }

    public enum PaymentMethod {
        BANK_TRANSFER, CHECK, CASH, DIGITAL_WALLET, CRYPTOCURRENCY
    }
}

