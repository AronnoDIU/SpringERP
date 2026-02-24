package com.springerp.service;

import com.springerp.entity.ChartOfAccounts;
import com.springerp.entity.GeneralLedger;
import com.springerp.entity.JournalEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for accounting operations.
 */
public interface AccountingService {

    // Chart of Accounts
    ChartOfAccounts createAccount(ChartOfAccounts account);
    ChartOfAccounts updateAccount(Long id, ChartOfAccounts account);
    Optional<ChartOfAccounts> getAccount(Long id);
    Optional<ChartOfAccounts> getAccountByCode(String code);
    Page<ChartOfAccounts> getAllAccounts(Pageable pageable);
    Page<ChartOfAccounts> getAccountsByType(ChartOfAccounts.AccountType type, Pageable pageable);
    List<ChartOfAccounts> getLowStockAccounts();
    void deleteAccount(Long id);

    // Journal Entry Operations
    JournalEntry createJournalEntry(JournalEntry entry);
    JournalEntry updateJournalEntry(Long id, JournalEntry entry);
    Optional<JournalEntry> getJournalEntry(Long id);
    Optional<JournalEntry> getJournalEntryByNumber(String entryNumber);
    Page<JournalEntry> getAllJournalEntries(Pageable pageable);
    Page<JournalEntry> getJournalEntriesByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<JournalEntry> getJournalEntriesByStatus(JournalEntry.Status status, Pageable pageable);

    // Journal Entry Approval
    JournalEntry approveJournalEntry(Long id, String comments);
    JournalEntry rejectJournalEntry(Long id, String comments);

    // General Ledger Operations
    GeneralLedger postJournalEntry(Long journalEntryId);
    Page<GeneralLedger> getGeneralLedgerByAccount(Long accountId, Pageable pageable);
    List<GeneralLedger> getAccountTransactions(Long accountId, LocalDate startDate, LocalDate endDate);

    // Balance Sheet & Trial Balance
    BigDecimal getAccountBalance(Long accountId);
    List<Object[]> getTrialBalance(LocalDate asOfDate);
    List<Object[]> getBalanceSheet(LocalDate asOfDate);
    List<Object[]> getIncomeStatement(LocalDate startDate, LocalDate endDate);

    // Financial Reports
    BigDecimal getTotalAssets(LocalDate asOfDate);
    BigDecimal getTotalLiabilities(LocalDate asOfDate);
    BigDecimal getTotalEquity(LocalDate asOfDate);

    void deleteJournalEntry(Long id);
}

