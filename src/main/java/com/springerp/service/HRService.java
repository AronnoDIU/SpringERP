package com.springerp.service;

import com.springerp.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for HR operations.
 */
public interface HRService {

    // Department Management
    Department createDepartment(Department department);
    Department updateDepartment(Long id, Department department);
    Optional<Department> getDepartment(Long id);
    Page<Department> getAllDepartments(Pageable pageable);

    // Employee Management
    Employee createEmployee(Employee employee);
    Employee updateEmployee(Long id, Employee employee);
    Optional<Employee> getEmployee(Long id);
    Optional<Employee> getEmployeeByEmployeeId(String employeeId);
    Optional<Employee> getEmployeeByEmail(String email);
    Page<Employee> getAllEmployees(Pageable pageable);
    Page<Employee> getEmployeesByDepartment(Long departmentId, Pageable pageable);
    Page<Employee> getEmployeesByStatus(Employee.EmploymentStatus status, Pageable pageable);
    List<Employee> getReportingEmployees(Long managerId);

    // Attendance Management
    Attendance recordAttendance(Attendance attendance);
    Attendance updateAttendance(Long id, Attendance attendance);
    Attendance approveAttendance(Long id, Long approverId);
    Optional<Attendance> getAttendance(Long id);
    List<Attendance> getEmployeeAttendance(Long employeeId, LocalDate startDate, LocalDate endDate);
    Page<Attendance> getAllAttendance(Pageable pageable);
    long getPresenceCount(Long employeeId, LocalDate startDate, LocalDate endDate);
    long getAbsenceCount(Long employeeId, LocalDate startDate, LocalDate endDate);

    // Leave Management
    Leave applyLeave(Leave leave);
    Leave approveLeave(Long id, Long approverId, String comments);
    Leave rejectLeave(Long id, Long approverId, String comments);
    Leave cancelLeave(Long id);
    Optional<Leave> getLeave(Long id);
    Page<Leave> getEmployeeLeaves(Long employeeId, Pageable pageable);
    Page<Leave> getLeavesForApproval(Long managerId, Pageable pageable);
    int getRemainingLeaveBalance(Long employeeId, Leave.LeaveType leaveType);

    // Salary Management
    Salary createSalary(Salary salary);
    Salary updateSalary(Long id, Salary salary);
    Optional<Salary> getSalary(Long id);
    Optional<Salary> getEmployeeSalaryForMonth(Long employeeId, LocalDate month);
    Page<Salary> getEmployeeSalaries(Long employeeId, Pageable pageable);
    Page<Salary> getPendingSalaries(Pageable pageable);
    Salary processSalary(Long salaryId);
    List<Salary> processBulkSalary(LocalDate salaryMonth);

    void deleteEmployee(Long id);
    void deleteDepartment(Long id);
    void deleteAttendance(Long id);
    void deleteLeave(Long id);
    void deleteSalary(Long id);
}

