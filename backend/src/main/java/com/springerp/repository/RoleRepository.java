package com.springerp.repository;

import com.springerp.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleCodeAndCompanyId(String roleCode, Long companyId);

    List<Role> findByCompanyId(Long companyId);

    Page<Role> findByCompanyId(Long companyId, Pageable pageable);

    List<Role> findByCompanyIdAndIsActive(Long companyId, Boolean isActive);

    Optional<Role> findByRoleNameAndCompanyId(String roleName, Long companyId);

    boolean existsByRoleCodeAndCompanyId(String roleCode, Long companyId);

    List<Role> findByIsSystemRole(Boolean isSystemRole);

    Page<Role> findByCompanyIdAndIsActive(Long companyId, Boolean isActive, Pageable pageable);
}

