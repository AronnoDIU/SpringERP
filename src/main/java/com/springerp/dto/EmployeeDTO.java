package com.springerp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for Employee
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {
    private Long id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String designation;
    private String department;
    private Long departmentId;
    private String employmentType;
    private String employmentStatus;
    private LocalDate dateOfJoining;
    private BigDecimal baseSalary;
    private Long reportingManagerId;
    private String officeLocation;
    private Boolean isActive;
}

