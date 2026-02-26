package com.springerp.repository;

import com.springerp.entity.CompanyUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long> {

    Optional<CompanyUser> findByUserIdAndCompanyId(Long userId, Long companyId);

    Page<CompanyUser> findByCompanyId(Long companyId, Pageable pageable);

    Page<CompanyUser> findByUserId(Long userId, Pageable pageable);

    List<CompanyUser> findByUserIdAndIsActive(Long userId, Boolean isActive);

    Page<CompanyUser> findByCompanyIdAndIsActive(Long companyId, Boolean isActive, Pageable pageable);

    @Query("SELECT cu FROM CompanyUser cu WHERE cu.user.id = :userId AND cu.isActive = true")
    List<CompanyUser> findActiveCompaniesByUser(Long userId);

    @Query("SELECT cu FROM CompanyUser cu WHERE cu.company.id = :companyId AND cu.role.id = :roleId")
    List<CompanyUser> findByCompanyAndRole(Long companyId, Long roleId);

    long countByCompanyId(Long companyId);

    long countByCompanyIdAndIsActive(Long companyId, Boolean isActive);

    boolean existsByUserIdAndCompanyId(Long userId, Long companyId);

    List<CompanyUser> findByIsCompanyAdmin(Boolean isCompanyAdmin);

    List<CompanyUser> findByCompanyIdAndIsCompanyAdmin(Long companyId, Boolean isCompanyAdmin);
}

