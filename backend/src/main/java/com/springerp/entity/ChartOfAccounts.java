package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Chart of Accounts entity representing the account structure in accounting.
 * Follows the double-entry bookkeeping principle.
 */
@Entity
@Table(name = "chart_of_accounts", indexes = {
        @Index(name = "idx_account_code", columnList = "account_code"),
        @Index(name = "idx_account_type", columnList = "account_type"),
        @Index(name = "idx_parent_account", columnList = "parent_account_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartOfAccounts extends BaseEntity {

    @Column(name = "account_code", nullable = false, unique = true)
    private String accountCode; // e.g., 1000, 2000, 3000

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType; // ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE

    @Column(name = "account_category")
    private String accountCategory; // Current Asset, Fixed Asset, etc.

    @Column(name = "description")
    private String description;

    @Column(name = "normal_balance")
    @Enumerated(EnumType.STRING)
    private BalanceSide normalBalance; // DEBIT or CREDIT

    @Column(name = "current_balance", precision = 19, scale = 2)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id")
    private ChartOfAccounts parentAccount;

    @OneToMany(mappedBy = "parentAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChartOfAccounts> subAccounts = new ArrayList<>();

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum AccountType {
        ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE
    }

    public enum BalanceSide {
        DEBIT, CREDIT
    }
}

