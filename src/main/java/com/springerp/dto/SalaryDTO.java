package com.springerp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for Salary
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate salaryMonth;
    private BigDecimal basicSalary;
    private BigDecimal grossSalary;
    private BigDecimal netSalary;
    private BigDecimal totalAllowances;
    private BigDecimal totalDeductions;
    private BigDecimal incomeTax;
    private String paymentStatus;
    private String paymentMethod;
    private LocalDate paymentDate;
}

