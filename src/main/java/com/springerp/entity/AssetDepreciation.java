package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Asset Depreciation entity for tracking depreciation of assets over time.
 */
@Entity
@Table(name = "asset_depreciations", indexes = {
        @Index(name = "idx_asset_id", columnList = "asset_id"),
        @Index(name = "idx_depreciation_date", columnList = "depreciation_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDepreciation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @Column(name = "depreciation_date", nullable = false)
    private LocalDate depreciationDate;

    @Column(name = "depreciation_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal depreciationAmount;

    @Column(name = "accumulated_depreciation", precision = 19, scale = 2)
    private BigDecimal accumulatedDepreciation;

    @Column(name = "book_value", precision = 19, scale = 2)
    private BigDecimal bookValue;

    @Column(name = "period")
    private String period; // e.g., "2024-01"

    @Column(name = "depreciation_expense_account_id")
    private Long depreciationExpenseAccountId; // References ChartOfAccounts

    @Column(name = "accumulated_depreciation_account_id")
    private Long accumulatedDepreciationAccountId; // References ChartOfAccounts

    @Column(name = "journal_entry_id")
    private Long journalEntryId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT; // DRAFT, POSTED, REVERSED

    @Column(name = "notes")
    private String notes;

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum Status {
        DRAFT, POSTED, REVERSED
    }
}

