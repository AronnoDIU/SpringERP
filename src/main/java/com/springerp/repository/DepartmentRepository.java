package com.springerp.repository;

import com.springerp.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByDepartmentCode(String departmentCode);

    Optional<Department> findByDepartmentName(String departmentName);

    List<Department> findByParentDepartmentId(Long parentDepartmentId);

    List<Department> findByManagerId(Long managerId);

    Page<Department> findByIsActive(Boolean isActive, Pageable pageable);

    Page<Department> findByTenantId(Long tenantId, Pageable pageable);
}

