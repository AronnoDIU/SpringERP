package com.springerp.repository;

import com.springerp.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByBudgetCode(String budgetCode);

    Page<Budget> findByAccountId(Long accountId, Pageable pageable);

    Page<Budget> findByDepartmentId(Long departmentId, Pageable pageable);

    List<Budget> findByBudgetPeriodAndStatus(LocalDate budgetPeriod, Budget.Status status);

    Page<Budget> findByStatus(Budget.Status status, Pageable pageable);

    Page<Budget> findByBudgetPeriod(LocalDate budgetPeriod, Pageable pageable);

    List<Budget> findByIsRecurringAndStatus(Boolean isRecurring, Budget.Status status);

    Page<Budget> findByTenantId(Long tenantId, Pageable pageable);
}

