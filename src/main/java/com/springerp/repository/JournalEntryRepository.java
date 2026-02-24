package com.springerp.repository;

import com.springerp.entity.JournalEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    Optional<JournalEntry> findByEntryNumber(String entryNumber);

    Page<JournalEntry> findByJournalDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<JournalEntry> findByStatus(JournalEntry.Status status, Pageable pageable);

    Page<JournalEntry> findByApprovalStatus(JournalEntry.ApprovalStatus approvalStatus, Pageable pageable);

    Page<JournalEntry> findByApproverId(Long approverId, Pageable pageable);

    Page<JournalEntry> findByIsBalanced(Boolean isBalanced, Pageable pageable);

    Page<JournalEntry> findByTenantId(Long tenantId, Pageable pageable);

    long countByStatus(JournalEntry.Status status);
}

