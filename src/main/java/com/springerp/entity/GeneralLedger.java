package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * General Ledger entry representing double-entry bookkeeping transactions.
 * Each transaction has at least one debit and one credit entry.
 */
@Entity
@Table(name = "general_ledger", indexes = {
        @Index(name = "idx_account_id", columnList = "account_id"),
        @Index(name = "idx_transaction_date", columnList = "transaction_date"),
        @Index(name = "idx_journal_entry_id", columnList = "journal_entry_id"),
        @Index(name = "idx_posting_date", columnList = "posting_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralLedger extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private ChartOfAccounts account;

    @Column(name = "journal_entry_id", nullable = false)
    private Long journalEntryId;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "debit_amount", precision = 19, scale = 2)
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Column(name = "credit_amount", precision = 19, scale = 2)
    private BigDecimal creditAmount = BigDecimal.ZERO;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "posting_date", nullable = false)
    private LocalDate postingDate;

    @Column(name = "entry_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EntryType entryType; // JOURNAL, INVOICE, BILL, PAYMENT, etc.

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT; // DRAFT, POSTED, REVERSED

    @Column(name = "memo")
    private String memo;

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum EntryType {
        JOURNAL, INVOICE, BILL, PAYMENT, ADJUSTMENT, DEPRECIATION, ACCRUAL
    }

    public enum Status {
        DRAFT, POSTED, REVERSED
    }
}

