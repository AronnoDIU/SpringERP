package com.springerp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for Attendance
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate attendanceDate;
    private String checkInTime;
    private String checkOutTime;
    private String attendanceStatus;
    private Double workingHours;
    private Double overtimeHours;
    private String remarks;
    private Boolean isApproved;
}

