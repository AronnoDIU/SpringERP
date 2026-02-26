package com.springerp.repository;

import com.springerp.entity.Leave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    Page<Leave> findByEmployeeId(Long employeeId, Pageable pageable);

    List<Leave> findByEmployeeIdAndLeaveStartDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    Page<Leave> findByLeaveType(Leave.LeaveType leaveType, Pageable pageable);

    Page<Leave> findByStatus(Leave.LeaveStatus status, Pageable pageable);

    Page<Leave> findByApprovedBy(Long approvedBy, Pageable pageable);

    List<Leave> findByLeaveStartDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, Leave.LeaveStatus status);

    Page<Leave> findByTenantId(Long tenantId, Pageable pageable);

    long countByEmployeeIdAndLeaveType(Long employeeId, Leave.LeaveType leaveType);
}

