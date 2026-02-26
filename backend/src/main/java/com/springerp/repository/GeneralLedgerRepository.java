package com.springerp.repository;

import com.springerp.entity.GeneralLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GeneralLedgerRepository extends JpaRepository<GeneralLedger, Long> {

    Page<GeneralLedger> findByAccountId(Long accountId, Pageable pageable);

    Page<GeneralLedger> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<GeneralLedger> findByJournalEntryId(Long journalEntryId);

    Page<GeneralLedger> findByStatus(GeneralLedger.Status status, Pageable pageable);

    @Query("SELECT gl FROM GeneralLedger gl WHERE gl.account.id = :accountId AND gl.transactionDate BETWEEN :startDate AND :endDate")
    List<GeneralLedger> findAccountTransactions(@Param("accountId") Long accountId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(gl.debitAmount) FROM GeneralLedger gl WHERE gl.account.id = :accountId AND gl.status = 'POSTED'")
    java.math.BigDecimal getTotalDebits(@Param("accountId") Long accountId);

    @Query("SELECT SUM(gl.creditAmount) FROM GeneralLedger gl WHERE gl.account.id = :accountId AND gl.status = 'POSTED'")
    java.math.BigDecimal getTotalCredits(@Param("accountId") Long accountId);

    Page<GeneralLedger> findByTenantId(Long tenantId, Pageable pageable);
}

