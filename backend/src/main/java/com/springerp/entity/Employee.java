package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Employee entity extending User for HR management.
 */
@Entity
@Table(name = "employees", indexes = {
        @Index(name = "idx_employee_id", columnList = "employee_id"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_department_id", columnList = "department_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee extends BaseEntity {

    @Column(name = "employee_id", nullable = false, unique = true)
    private String employeeId;

    @Column(name = "user_id", nullable = false)
    private Long userId; // References User

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "national_id")
    private String nationalId;

    @Column(name = "passport_number")
    private String passportNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "designation")
    private String designation;

    @Column(name = "employment_type")
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType; // FULL_TIME, PART_TIME, CONTRACT, INTERN

    @Column(name = "employment_status")
    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus = EmploymentStatus.ACTIVE; // ACTIVE, ON_LEAVE, TERMINATED, RETIRED

    @Column(name = "date_of_joining", nullable = false)
    private LocalDate dateOfJoining;

    @Column(name = "date_of_termination")
    private LocalDate dateOfTermination;

    @Column(name = "base_salary", precision = 19, scale = 2)
    private BigDecimal baseSalary;

    @Column(name = "salary_currency")
    private String salaryCurrency = "USD";

    @Column(name = "salary_frequency")
    @Enumerated(EnumType.STRING)
    private SalaryFrequency salaryFrequency = SalaryFrequency.MONTHLY;

    @Column(name = "reporting_manager_id")
    private Long reportingManagerId; // References another Employee

    @Column(name = "office_location")
    private String officeLocation;

    @Column(name = "employee_photo_url")
    private String employeePhotoUrl;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "tenant_id")
    private Long tenantId;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Leave> leaves = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Salary> salaries = new ArrayList<>();

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum EmploymentType {
        FULL_TIME, PART_TIME, CONTRACT, INTERN, CONSULTANT, TEMPORARY
    }

    public enum EmploymentStatus {
        ACTIVE, ON_LEAVE, SUSPENDED, TERMINATED, RETIRED
    }

    public enum SalaryFrequency {
        DAILY, WEEKLY, BIWEEKLY, MONTHLY, QUARTERLY, ANNUALLY
    }
}

