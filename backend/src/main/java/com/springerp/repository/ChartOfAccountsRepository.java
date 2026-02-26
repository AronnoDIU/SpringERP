package com.springerp.repository;

import com.springerp.entity.ChartOfAccounts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChartOfAccountsRepository extends JpaRepository<ChartOfAccounts, Long> {

    Optional<ChartOfAccounts> findByAccountCode(String accountCode);

    Page<ChartOfAccounts> findByAccountType(ChartOfAccounts.AccountType accountType, Pageable pageable);

    List<ChartOfAccounts> findByParentAccountId(Long parentAccountId);

    Page<ChartOfAccounts> findByIsActive(Boolean isActive, Pageable pageable);

    List<ChartOfAccounts> findByAccountNameContainingIgnoreCase(String accountName);

    Page<ChartOfAccounts> findByTenantId(Long tenantId, Pageable pageable);
}

