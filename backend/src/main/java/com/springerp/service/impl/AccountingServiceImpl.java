package com.springerp.service.impl;

import com.springerp.entity.*;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.repository.*;
import com.springerp.service.AccountingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for accounting operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountingServiceImpl implements AccountingService {

    private final ChartOfAccountsRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final BudgetRepository budgetRepository;

    @Override
    @Transactional
    public ChartOfAccounts createAccount(ChartOfAccounts account) {
        log.info("Creating account: {}", account.getAccountCode());
        account.setIsActive(true);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public ChartOfAccounts updateAccount(Long id, ChartOfAccounts account) {
        log.info("Updating account: {}", id);
        return accountRepository.findById(id)
                .map(existing -> {
                    existing.setAccountName(account.getAccountName());
                    existing.setAccountCategory(account.getAccountCategory());
                    existing.setDescription(account.getDescription());
                    existing.setIsActive(account.getIsActive());
                    return accountRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChartOfAccounts> getAccount(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChartOfAccounts> getAccountByCode(String code) {
        return accountRepository.findByAccountCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChartOfAccounts> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChartOfAccounts> getAccountsByType(ChartOfAccounts.AccountType type, Pageable pageable) {
        return accountRepository.findByAccountType(type, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChartOfAccounts> getLowStockAccounts() {
        return accountRepository.findAll().stream()
                .filter(acc -> acc.getIsActive() != null && acc.getIsActive())
                .toList();
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        log.info("Deleting account: {}", id);
        accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional
    public JournalEntry createJournalEntry(JournalEntry entry) {
        log.info("Creating journal entry: {}", entry.getEntryNumber());
        entry.setStatus(JournalEntry.Status.DRAFT);
        entry.setApprovalStatus(JournalEntry.ApprovalStatus.PENDING);
        return journalEntryRepository.save(entry);
    }

    @Override
    @Transactional
    public JournalEntry updateJournalEntry(Long id, JournalEntry entry) {
        log.info("Updating journal entry: {}", id);
        return journalEntryRepository.findById(id)
                .map(existing -> {
                    if (existing.getStatus() == JournalEntry.Status.DRAFT) {
                        existing.setDescription(entry.getDescription());
                        existing.setJournalDate(entry.getJournalDate());
                        existing.setIsBalanced(entry.getIsBalanced());
                    }
                    return journalEntryRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Journal entry not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JournalEntry> getJournalEntry(Long id) {
        return journalEntryRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JournalEntry> getJournalEntryByNumber(String entryNumber) {
        return journalEntryRepository.findByEntryNumber(entryNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JournalEntry> getAllJournalEntries(Pageable pageable) {
        return journalEntryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JournalEntry> getJournalEntriesByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return journalEntryRepository.findByJournalDateBetween(startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JournalEntry> getJournalEntriesByStatus(JournalEntry.Status status, Pageable pageable) {
        return journalEntryRepository.findByStatus(status, pageable);
    }

    @Override
    @Transactional
    public JournalEntry approveJournalEntry(Long id, String comments) {
        log.info("Approving journal entry: {}", id);
        return journalEntryRepository.findById(id)
                .map(entry -> {
                    entry.setApprovalStatus(JournalEntry.ApprovalStatus.APPROVED);
                    entry.setApprovalNotes(comments);
                    return journalEntryRepository.save(entry);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Journal entry not found with id: " + id));
    }

    @Override
    @Transactional
    public JournalEntry rejectJournalEntry(Long id, String comments) {
        log.info("Rejecting journal entry: {}", id);
        return journalEntryRepository.findById(id)
                .map(entry -> {
                    entry.setApprovalStatus(JournalEntry.ApprovalStatus.REJECTED);
                    entry.setApprovalNotes(comments);
                    entry.setStatus(JournalEntry.Status.DRAFT);
                    return journalEntryRepository.save(entry);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Journal entry not found with id: " + id));
    }

    @Override
    @Transactional
    public GeneralLedger postJournalEntry(Long journalEntryId) {
        log.info("Posting journal entry to general ledger: {}", journalEntryId);
        JournalEntry entry = journalEntryRepository.findById(journalEntryId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal entry not found with id: " + journalEntryId));

        if (entry.getApprovalStatus() != JournalEntry.ApprovalStatus.APPROVED) {
            throw new IllegalStateException("Journal entry must be approved before posting");
        }

        entry.setStatus(JournalEntry.Status.POSTED);
        journalEntryRepository.save(entry);

        return generalLedgerRepository.findByJournalEntryId(journalEntryId).stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No general ledger entries found for journal entry id: " + journalEntryId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GeneralLedger> getGeneralLedgerByAccount(Long accountId, Pageable pageable) {
        return generalLedgerRepository.findByAccountId(accountId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GeneralLedger> getAccountTransactions(Long accountId, LocalDate startDate, LocalDate endDate) {
        return generalLedgerRepository.findAccountTransactions(accountId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(Long accountId) {
        ChartOfAccounts account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        BigDecimal debits = generalLedgerRepository.getTotalDebits(accountId);
        BigDecimal credits = generalLedgerRepository.getTotalCredits(accountId);

        debits = debits != null ? debits : BigDecimal.ZERO;
        credits = credits != null ? credits : BigDecimal.ZERO;

        if (account.getNormalBalance() == ChartOfAccounts.BalanceSide.DEBIT) {
            return debits.subtract(credits);
        } else {
            return credits.subtract(debits);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getTrialBalance(LocalDate asOfDate) {
        log.info("Generating trial balance as of: {}", asOfDate);
        return List.of(); // Implementation requires custom query
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getBalanceSheet(LocalDate asOfDate) {
        log.info("Generating balance sheet as of: {}", asOfDate);
        return List.of(); // Implementation requires custom query
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getIncomeStatement(LocalDate startDate, LocalDate endDate) {
        log.info("Generating income statement from {} to {}", startDate, endDate);
        return List.of(); // Implementation requires custom query
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalAssets(LocalDate asOfDate) {
        return BigDecimal.ZERO; // Implementation required
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalLiabilities(LocalDate asOfDate) {
        return BigDecimal.ZERO; // Implementation required
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalEquity(LocalDate asOfDate) {
        return BigDecimal.ZERO; // Implementation required
    }

    @Override
    @Transactional
    public void deleteJournalEntry(Long id) {
        log.info("Deleting journal entry: {}", id);
        journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal entry not found with id: " + id));
        journalEntryRepository.deleteById(id);
    }
}
