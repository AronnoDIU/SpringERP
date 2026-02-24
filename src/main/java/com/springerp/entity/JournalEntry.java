package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Journal Entry entity for recording accounting transactions.
 * Each journal entry represents a complete transaction that must balance.
 */
@Entity
@Table(name = "journal_entries", indexes = {
        @Index(name = "idx_journal_date", columnList = "journal_date"),
        @Index(name = "idx_entry_number", columnList = "entry_number"),
        @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry extends BaseEntity {

    @Column(name = "entry_number", nullable = false, unique = true)
    private String entryNumber;

    @Column(name = "journal_date", nullable = false)
    private LocalDate journalDate;

    @Column(name = "posting_date")
    private LocalDate postingDate;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "total_debit", precision = 19, scale = 2)
    private BigDecimal totalDebit = BigDecimal.ZERO;

    @Column(name = "total_credit", precision = 19, scale = 2)
    private BigDecimal totalCredit = BigDecimal.ZERO;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT; // DRAFT, POSTED, APPROVED, REVERSED

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING; // PENDING, APPROVED, REJECTED

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "approval_notes")
    private String approvalNotes;

    @Column(name = "memo")
    private String memo;

    @Column(name = "attachment_url")
    private String attachmentUrl;

    @Column(name = "is_balanced", nullable = false)
    private Boolean isBalanced = false;

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum Status {
        DRAFT, POSTED, APPROVED, REVERSED
    }

    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED
    }
}

