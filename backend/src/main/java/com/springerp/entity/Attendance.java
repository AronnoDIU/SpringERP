package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Attendance entity for tracking employee attendance.
 */
@Entity
@Table(name = "attendances", indexes = {
        @Index(name = "idx_employee_id", columnList = "employee_id"),
        @Index(name = "idx_attendance_date", columnList = "attendance_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "check_in_time")
    private java.time.LocalTime checkInTime;

    @Column(name = "check_out_time")
    private java.time.LocalTime checkOutTime;

    @Column(name = "attendance_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus; // PRESENT, ABSENT, LATE, EARLY_LEAVE, HALF_DAY

    @Column(name = "working_hours")
    private Double workingHours;

    @Column(name = "overtime_hours")
    private Double overtimeHours = 0.0;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = false;

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, EARLY_LEAVE, HALF_DAY, SICK_LEAVE, ON_LEAVE
    }
}

