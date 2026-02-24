package com.springerp.repository;

import com.springerp.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);

    Page<Attendance> findByEmployeeId(Long employeeId, Pageable pageable);

    List<Attendance> findByEmployeeIdAndAttendanceDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    Page<Attendance> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Attendance> findByAttendanceStatus(Attendance.AttendanceStatus status, Pageable pageable);

    Page<Attendance> findByIsApproved(Boolean isApproved, Pageable pageable);

    Page<Attendance> findByTenantId(Long tenantId, Pageable pageable);

    long countByEmployeeIdAndAttendanceStatus(Long employeeId, Attendance.AttendanceStatus status);
}

