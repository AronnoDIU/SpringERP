package com.springerp.repository;

import com.springerp.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeId(String employeeId);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByUserId(Long userId);

    Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);

    List<Employee> findByReportingManagerId(Long reportingManagerId);

    Page<Employee> findByEmploymentStatus(Employee.EmploymentStatus status, Pageable pageable);

    Page<Employee> findByIsActive(Boolean isActive, Pageable pageable);

    List<Employee> findByEmploymentStatusOrderByEmployeeId(Employee.EmploymentStatus status);

    Page<Employee> findByTenantId(Long tenantId, Pageable pageable);

    long countByEmploymentStatus(Employee.EmploymentStatus status);
}

