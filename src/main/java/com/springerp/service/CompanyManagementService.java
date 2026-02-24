package com.springerp.service;

import com.springerp.dto.CompanyDTO;
import com.springerp.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service for multi-company management.
 */
public interface CompanyManagementService {

    // Company CRUD
    Company createCompany(CompanyDTO dto, Long parentCompanyId);
    Company updateCompany(Long companyId, CompanyDTO dto);
    Optional<Company> getCompany(Long companyId);
    Page<Company> getAllCompanies(Pageable pageable);
    Page<Company> searchCompanies(String searchTerm, Pageable pageable);
    void deleteCompany(Long companyId);

    // Company Status
    Company activateCompany(Long companyId);
    Company deactivateCompany(Long companyId);
    Company suspendCompany(Long companyId, String reason);

    // Company Hierarchy
    List<Company> getCompanyHierarchy(Long companyId);
    List<Company> getSubsidiaries(Long parentCompanyId);
    List<Company> getParentChain(Long companyId);

    // Company Configuration
    Company updateCompanyConfig(Long companyId, String configJson);
    String getCompanyConfig(Long companyId);

    // Company Statistics
    long getTotalActiveUsers(Long companyId);
    long getTotalApiCalls(Long companyId);
    long getTotalDataSize(Long companyId);

    // Company Users
    List<Long> getCompanyUserIds(Long companyId);
    boolean isUserInCompany(Long userId, Long companyId);
}

