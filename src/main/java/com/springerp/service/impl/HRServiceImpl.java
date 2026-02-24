package com.springerp.service.impl;

import com.springerp.entity.*;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.repository.*;
import com.springerp.service.HRService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for HR operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HRServiceImpl implements HRService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;
    private final SalaryRepository salaryRepository;

    // Department Management
    @Override
    @Transactional
    public Department createDepartment(Department department) {
        log.info("Creating department: {}", department.getDepartmentCode());
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public Department updateDepartment(Long id, Department department) {
        log.info("Updating department: {}", id);
        return departmentRepository.findById(id)
                .map(existing -> {
                    existing.setDepartmentName(department.getDepartmentName());
                    existing.setDescription(department.getDescription());
                    existing.setBudget(department.getBudget());
                    return departmentRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Department> getDepartment(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Department> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    // Employee Management
    @Override
    @Transactional
    public Employee createEmployee(Employee employee) {
        log.info("Creating employee: {}", employee.getEmployeeId());
        employee.setIsActive(true);
        employee.setEmploymentStatus(Employee.EmploymentStatus.ACTIVE);
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee updateEmployee(Long id, Employee employee) {
        log.info("Updating employee: {}", id);
        return employeeRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(employee.getFirstName());
                    existing.setLastName(employee.getLastName());
                    existing.setEmail(employee.getEmail());
                    existing.setDesignation(employee.getDesignation());
                    existing.setBaseSalary(employee.getBaseSalary());
                    return employeeRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployee(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> getEmployeesByDepartment(Long departmentId, Pageable pageable) {
        return employeeRepository.findByDepartmentId(departmentId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> getEmployeesByStatus(Employee.EmploymentStatus status, Pageable pageable) {
        return employeeRepository.findByEmploymentStatus(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getReportingEmployees(Long managerId) {
        return employeeRepository.findByReportingManagerId(managerId);
    }

    // Attendance Management
    @Override
    @Transactional
    public Attendance recordAttendance(Attendance attendance) {
        log.info("Recording attendance for employee: {}", attendance.getEmployee().getId());
        return attendanceRepository.save(attendance);
    }

    @Override
    @Transactional
    public Attendance updateAttendance(Long id, Attendance attendance) {
        log.info("Updating attendance: {}", id);
        return attendanceRepository.findById(id)
                .map(existing -> {
                    existing.setAttendanceStatus(attendance.getAttendanceStatus());
                    existing.setCheckInTime(attendance.getCheckInTime());
                    existing.setCheckOutTime(attendance.getCheckOutTime());
                    existing.setWorkingHours(attendance.getWorkingHours());
                    return attendanceRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id: " + id));
    }

    @Override
    @Transactional
    public Attendance approveAttendance(Long id, Long approverId) {
        log.info("Approving attendance: {}", id);
        return attendanceRepository.findById(id)
                .map(existing -> {
                    existing.setIsApproved(true);
                    existing.setApprovedBy(approverId);
                    return attendanceRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Attendance> getAttendance(Long id) {
        return attendanceRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getEmployeeAttendance(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByEmployeeIdAndAttendanceDateBetween(employeeId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attendance> getAllAttendance(Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long getPresenceCount(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return getEmployeeAttendance(employeeId, startDate, endDate).stream()
                .filter(a -> a.getAttendanceStatus() == Attendance.AttendanceStatus.PRESENT)
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getAbsenceCount(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return getEmployeeAttendance(employeeId, startDate, endDate).stream()
                .filter(a -> a.getAttendanceStatus() == Attendance.AttendanceStatus.ABSENT)
                .count();
    }

    // Leave Management
    @Override
    @Transactional
    public Leave applyLeave(Leave leave) {
        log.info("Applying leave for employee: {}", leave.getEmployee().getId());
        leave.setStatus(Leave.LeaveStatus.PENDING);
        return leaveRepository.save(leave);
    }

    @Override
    @Transactional
    public Leave approveLeave(Long id, Long approverId, String comments) {
        log.info("Approving leave: {}", id);
        return leaveRepository.findById(id)
                .map(existing -> {
                    existing.setStatus(Leave.LeaveStatus.APPROVED);
                    existing.setApprovedBy(approverId);
                    existing.setApprovalComments(comments);
                    existing.setApprovalDate(LocalDate.now());
                    return leaveRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found with id: " + id));
    }

    @Override
    @Transactional
    public Leave rejectLeave(Long id, Long approverId, String comments) {
        log.info("Rejecting leave: {}", id);
        return leaveRepository.findById(id)
                .map(existing -> {
                    existing.setStatus(Leave.LeaveStatus.REJECTED);
                    existing.setApprovedBy(approverId);
                    existing.setApprovalComments(comments);
                    existing.setApprovalDate(LocalDate.now());
                    return leaveRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found with id: " + id));
    }

    @Override
    @Transactional
    public Leave cancelLeave(Long id) {
        log.info("Cancelling leave: {}", id);
        return leaveRepository.findById(id)
                .map(existing -> {
                    existing.setStatus(Leave.LeaveStatus.CANCELLED);
                    return leaveRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Leave> getLeave(Long id) {
        return leaveRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Leave> getEmployeeLeaves(Long employeeId, Pageable pageable) {
        return leaveRepository.findByEmployeeId(employeeId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Leave> getLeavesForApproval(Long managerId, Pageable pageable) {
        return leaveRepository.findByStatus(Leave.LeaveStatus.PENDING, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getRemainingLeaveBalance(Long employeeId, Leave.LeaveType leaveType) {
        long totalTaken = leaveRepository.countByEmployeeIdAndLeaveType(employeeId, leaveType);
        return Math.max(0, 30 - (int) totalTaken); // Assuming 30 days per leave type annually
    }

    // Salary Management
    @Override
    @Transactional
    public Salary createSalary(Salary salary) {
        log.info("Creating salary for employee: {}", salary.getEmployee().getId());
        salary.setPaymentStatus(Salary.PaymentStatus.PENDING);
        return salaryRepository.save(salary);
    }

    @Override
    @Transactional
    public Salary updateSalary(Long id, Salary salary) {
        log.info("Updating salary: {}", id);
        return salaryRepository.findById(id)
                .map(existing -> {
                    if (existing.getPaymentStatus() == Salary.PaymentStatus.PENDING) {
                        existing.setBasicSalary(salary.getBasicSalary());
                        existing.setTotalAllowances(salary.getTotalAllowances());
                        existing.setTotalDeductions(salary.getTotalDeductions());
                    }
                    return salaryRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Salary not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Salary> getSalary(Long id) {
        return salaryRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Salary> getEmployeeSalaryForMonth(Long employeeId, LocalDate month) {
        return salaryRepository.findByEmployeeIdAndSalaryMonth(employeeId, month);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Salary> getEmployeeSalaries(Long employeeId, Pageable pageable) {
        return salaryRepository.findByEmployeeId(employeeId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Salary> getPendingSalaries(Pageable pageable) {
        return salaryRepository.findByPaymentStatus(Salary.PaymentStatus.PENDING, pageable);
    }

    @Override
    @Transactional
    public Salary processSalary(Long salaryId) {
        log.info("Processing salary: {}", salaryId);
        return salaryRepository.findById(salaryId)
                .map(salary -> {
                    salary.setPaymentStatus(Salary.PaymentStatus.PROCESSED);
                    salary.setPaymentDate(LocalDate.now());
                    return salaryRepository.save(salary);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Salary not found with id: " + salaryId));
    }

    @Override
    @Transactional
    public List<Salary> processBulkSalary(LocalDate salaryMonth) {
        log.info("Processing bulk salary for month: {}", salaryMonth);
        Page<Salary> salaries = salaryRepository.findByPaymentStatus(Salary.PaymentStatus.PENDING, Pageable.unpaged());
        return salaries.getContent().stream()
                .map(salary -> processSalary(salary.getId()))
                .toList();
    }

    // Delete operations
    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Deleting employee: {}", id);
        employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        log.info("Deleting department: {}", id);
        departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAttendance(Long id) {
        log.info("Deleting attendance: {}", id);
        attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id: " + id));
        attendanceRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteLeave(Long id) {
        log.info("Deleting leave: {}", id);
        leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found with id: " + id));
        leaveRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteSalary(Long id) {
        log.info("Deleting salary: {}", id);
        salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary not found with id: " + id));
        salaryRepository.deleteById(id);
    }
}
