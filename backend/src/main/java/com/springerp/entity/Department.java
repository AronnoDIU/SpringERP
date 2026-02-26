package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Department entity for organizing employees.
 */
@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department extends BaseEntity {

    @Column(name = "department_code", nullable = false, unique = true)
    private String departmentCode;

    @Column(name = "department_name", nullable = false, unique = true)
    private String departmentName;

    @Column(name = "description")
    private String description;

    @Column(name = "manager_id")
    private Long managerId; // References Employee

    @Column(name = "budget", precision = 19, scale = 2)
    private java.math.BigDecimal budget;

    @Column(name = "cost_center_code")
    private String costCenterCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_department_id")
    private Department parentDepartment;

    @OneToMany(mappedBy = "parentDepartment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Department> subDepartments = new ArrayList<>();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "tenant_id")
    private Long tenantId;
}

