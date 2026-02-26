package com.springerp.service.impl;

import com.springerp.dto.CompanyDTO;
import com.springerp.entity.Company;
import com.springerp.repository.CompanyRepository;
import com.springerp.repository.CompanyUserRepository;
import com.springerp.service.CompanyManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of multi-company management service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompanyManagementServiceImpl implements CompanyManagementService {

    private final CompanyRepository companyRepository;
    private final CompanyUserRepository companyUserRepository;

    @Override
    public Company createCompany(CompanyDTO dto, Long parentCompanyId) {
        log.info("Creating company: {}", dto.getCompanyCode());

        Company company = new Company();
        company.setCompanyName(dto.getCompanyName());
        company.setCompanyCode(dto.getCompanyCode());
        company.setRegistrationNumber(dto.getRegistrationNumber());
        company.setTaxId(dto.getTaxId());
        company.setAddress(dto.getAddress());
        company.setCity(dto.getCity());
        company.setState(dto.getState());
        company.setPostalCode(dto.getPostalCode());
        company.setCountry(dto.getCountry());
        company.setPhone(dto.getPhone());
        company.setEmail(dto.getEmail());
        company.setWebsite(dto.getWebsite());
        company.setCurrency(dto.getCurrency() != null ? dto.getCurrency() : "USD");
        company.setStatus("ACTIVE");
        company.setSubscriptionTier(dto.getSubscriptionTier() != null ? dto.getSubscriptionTier() : "PROFESSIONAL");

        if (parentCompanyId != null) {
            Company parentCompany = companyRepository.findById(parentCompanyId)
                    .orElseThrow(() -> new RuntimeException("Parent company not found: " + parentCompanyId));
            company.setParentCompany(parentCompany);
        }

        return companyRepository.save(company);
    }

    @Override
    public Company updateCompany(Long companyId, CompanyDTO dto) {
        log.info("Updating company: {}", companyId);

        return companyRepository.findById(companyId)
                .map(company -> {
                    company.setCompanyName(dto.getCompanyName());
                    company.setAddress(dto.getAddress());
                    company.setCity(dto.getCity());
                    company.setState(dto.getState());
                    company.setPostalCode(dto.getPostalCode());
                    company.setCountry(dto.getCountry());
                    company.setPhone(dto.getPhone());
                    company.setEmail(dto.getEmail());
                    company.setWebsite(dto.getWebsite());
                    company.setLogoUrl(dto.getLogoUrl());
                    return companyRepository.save(company);
                })
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));
    }

    @Override
    public Optional<Company> getCompany(Long companyId) {
        return companyRepository.findById(companyId);
    }

    @Override
    public Page<Company> getAllCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    @Override
    public Page<Company> searchCompanies(String searchTerm, Pageable pageable) {
        Page<Company> allCompanies = companyRepository.findAll(pageable);
        return allCompanies;
    }

    @Override
    public void deleteCompany(Long companyId) {
        log.info("Deleting company: {}", companyId);
        companyRepository.deleteById(companyId);
    }

    @Override
    public Company activateCompany(Long companyId) {
        log.info("Activating company: {}", companyId);
        return companyRepository.findById(companyId)
                .map(company -> {
                    company.setStatus("ACTIVE");
                    return companyRepository.save(company);
                })
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));
    }

    @Override
    public Company deactivateCompany(Long companyId) {
        log.info("Deactivating company: {}", companyId);
        return companyRepository.findById(companyId)
                .map(company -> {
                    company.setStatus("INACTIVE");
                    return companyRepository.save(company);
                })
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));
    }

    @Override
    public Company suspendCompany(Long companyId, String reason) {
        log.info("Suspending company: {} - Reason: {}", companyId, reason);
        return companyRepository.findById(companyId)
                .map(company -> {
                    company.setStatus("SUSPENDED");
                    return companyRepository.save(company);
                })
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));
    }

    @Override
    public List<Company> getCompanyHierarchy(Long companyId) {
        return companyRepository.findAll().stream()
                .filter(c -> c.getId().equals(companyId) ||
                           (c.getParentCompany() != null && c.getParentCompany().getId().equals(companyId)))
                .toList();
    }

    @Override
    public List<Company> getSubsidiaries(Long parentCompanyId) {
        return companyRepository.findAll().stream()
                .filter(c -> c.getParentCompany() != null && c.getParentCompany().getId().equals(parentCompanyId))
                .toList();
    }

    @Override
    public List<Company> getParentChain(Long companyId) {
        List<Company> chain = new java.util.ArrayList<>();
        Optional<Company> currentOpt = companyRepository.findById(companyId);

        while (currentOpt.isPresent()) {
            Company current = currentOpt.get();
            chain.add(current);
            currentOpt = Optional.ofNullable(current.getParentCompany());
        }

        return chain;
    }

    @Override
    public Company updateCompanyConfig(Long companyId, String configJson) {
        return companyRepository.findById(companyId)
                .map(company -> {
                    company.setConfig(configJson);
                    return companyRepository.save(company);
                })
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));
    }

    @Override
    public String getCompanyConfig(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));
        return company.getConfig();
    }

    @Override
    public long getTotalActiveUsers(Long companyId) {
        return 0L; // TODO: Implement user counting
    }

    @Override
    public long getTotalApiCalls(Long companyId) {
        // TODO: Implement API call tracking
        return 0L;
    }

    @Override
    public long getTotalDataSize(Long companyId) {
        // TODO: Implement data size calculation
        return 0L;
    }

    @Override
    public List<Long> getCompanyUserIds(Long companyId) {
        return new java.util.ArrayList<>(); // TODO: Implement user ID retrieval
    }

    @Override
    public boolean isUserInCompany(Long userId, Long companyId) {
        return false; // TODO: Implement user company check
    }
}

