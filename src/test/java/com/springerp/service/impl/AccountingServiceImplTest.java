package com.springerp.service.impl;

import com.springerp.entity.ChartOfAccounts;
import com.springerp.entity.GeneralLedger;
import com.springerp.entity.JournalEntry;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.repository.BudgetRepository;
import com.springerp.repository.ChartOfAccountsRepository;
import com.springerp.repository.GeneralLedgerRepository;
import com.springerp.repository.JournalEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountingServiceImplTest {

    @Mock
    private ChartOfAccountsRepository accountRepository;

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Mock
    private GeneralLedgerRepository generalLedgerRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private AccountingServiceImpl service;

    private ChartOfAccounts account;
    private JournalEntry journalEntry;

    @BeforeEach
    void setUp() {
        account = ChartOfAccounts.builder()
                .accountCode("1000")
                .accountName("Cash")
                .accountType(ChartOfAccounts.AccountType.ASSET)
                .normalBalance(ChartOfAccounts.BalanceSide.DEBIT)
                .isActive(true)
                .build();
        account.setId(1L);

        journalEntry = JournalEntry.builder()
                .entryNumber("JE-001")
                .description("Test entry")
                .journalDate(LocalDate.now())
                .status(JournalEntry.Status.DRAFT)
                .approvalStatus(JournalEntry.ApprovalStatus.PENDING)
                .isBalanced(true)
                .build();
        journalEntry.setId(10L);
    }

    @Test
    void createAccount_forcesIsActiveToTrue() {
        account.setIsActive(false);
        when(accountRepository.save(any(ChartOfAccounts.class))).thenReturn(account);

        service.createAccount(account);

        ArgumentCaptor<ChartOfAccounts> captor = ArgumentCaptor.forClass(ChartOfAccounts.class);
        verify(accountRepository).save(captor.capture());
        assertThat(captor.getValue().getIsActive()).isTrue();
    }

    @Test
    void createAccount_returnsPersistedAccount() {
        when(accountRepository.save(any(ChartOfAccounts.class))).thenReturn(account);

        ChartOfAccounts result = service.createAccount(account);

        assertThat(result).isEqualTo(account);
    }

    @Test
    void updateAccount_updatesNameCategoryDescriptionAndActiveStatus() {
        ChartOfAccounts incoming = ChartOfAccounts.builder()
                .accountName("Petty Cash")
                .accountCategory("Current Asset")
                .description("Small cash fund")
                .isActive(false)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(ChartOfAccounts.class))).thenAnswer(inv -> inv.getArgument(0));

        ChartOfAccounts result = service.updateAccount(1L, incoming);

        assertThat(result.getAccountName()).isEqualTo("Petty Cash");
        assertThat(result.getAccountCategory()).isEqualTo("Current Asset");
        assertThat(result.getDescription()).isEqualTo("Small cash fund");
        assertThat(result.getIsActive()).isFalse();
    }

    @Test
    void updateAccount_withNonExistentId_throwsResourceNotFoundException() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateAccount(99L, account))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteAccount_deletesExistingAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        service.deleteAccount(1L);

        verify(accountRepository).deleteById(1L);
    }

    @Test
    void deleteAccount_withNonExistentId_throwsResourceNotFoundException() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteAccount(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(accountRepository, never()).deleteById(any());
    }

    @Test
    void getLowStockAccounts_returnsOnlyActiveAccounts() {
        ChartOfAccounts inactive = ChartOfAccounts.builder().accountCode("9999").isActive(false).build();
        ChartOfAccounts nullActive = ChartOfAccounts.builder().accountCode("8888").isActive(null).build();

        when(accountRepository.findAll()).thenReturn(List.of(account, inactive, nullActive));

        List<ChartOfAccounts> result = service.getLowStockAccounts();

        assertThat(result).containsExactly(account);
    }

    @Test
    void getLowStockAccounts_returnsEmptyWhenAllInactive() {
        account.setIsActive(false);
        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<ChartOfAccounts> result = service.getLowStockAccounts();

        assertThat(result).isEmpty();
    }

    @Test
    void createJournalEntry_setsStatusToDraftAndApprovalToPending() {
        journalEntry.setStatus(JournalEntry.Status.POSTED);
        journalEntry.setApprovalStatus(JournalEntry.ApprovalStatus.APPROVED);
        when(journalEntryRepository.save(any(JournalEntry.class))).thenReturn(journalEntry);

        service.createJournalEntry(journalEntry);

        ArgumentCaptor<JournalEntry> captor = ArgumentCaptor.forClass(JournalEntry.class);
        verify(journalEntryRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(JournalEntry.Status.DRAFT);
        assertThat(captor.getValue().getApprovalStatus()).isEqualTo(JournalEntry.ApprovalStatus.PENDING);
    }

    @Test
    void updateJournalEntry_whenDraft_updatesDescriptionDateAndBalance() {
        journalEntry.setStatus(JournalEntry.Status.DRAFT);

        JournalEntry update = JournalEntry.builder()
                .description("Updated description")
                .journalDate(LocalDate.of(2026, 1, 15))
                .isBalanced(true)
                .build();

        when(journalEntryRepository.findById(10L)).thenReturn(Optional.of(journalEntry));
        when(journalEntryRepository.save(any(JournalEntry.class))).thenAnswer(inv -> inv.getArgument(0));

        JournalEntry result = service.updateJournalEntry(10L, update);

        assertThat(result.getDescription()).isEqualTo("Updated description");
        assertThat(result.getJournalDate()).isEqualTo(LocalDate.of(2026, 1, 15));
        assertThat(result.getIsBalanced()).isTrue();
    }

    @Test
    void updateJournalEntry_whenNotDraft_doesNotUpdateFields() {
        journalEntry.setStatus(JournalEntry.Status.POSTED);
        journalEntry.setDescription("Original");

        JournalEntry update = JournalEntry.builder()
                .description("Should not be applied")
                .journalDate(LocalDate.of(2026, 6, 1))
                .build();

        when(journalEntryRepository.findById(10L)).thenReturn(Optional.of(journalEntry));
        when(journalEntryRepository.save(any(JournalEntry.class))).thenAnswer(inv -> inv.getArgument(0));

        JournalEntry result = service.updateJournalEntry(10L, update);

        assertThat(result.getDescription()).isEqualTo("Original");
    }

    @Test
    void updateJournalEntry_withNonExistentId_throwsResourceNotFoundException() {
        when(journalEntryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateJournalEntry(99L, journalEntry))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void approveJournalEntry_setsApprovedStatusAndStoresNotes() {
        when(journalEntryRepository.findById(10L)).thenReturn(Optional.of(journalEntry));
        when(journalEntryRepository.save(any(JournalEntry.class))).thenAnswer(inv -> inv.getArgument(0));

        JournalEntry result = service.approveJournalEntry(10L, "Looks correct");

        assertThat(result.getApprovalStatus()).isEqualTo(JournalEntry.ApprovalStatus.APPROVED);
        assertThat(result.getApprovalNotes()).isEqualTo("Looks correct");
    }

    @Test
    void approveJournalEntry_withNonExistentId_throwsResourceNotFoundException() {
        when(journalEntryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.approveJournalEntry(99L, "notes"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void rejectJournalEntry_setsRejectedStatusRestoresDraftAndStoresNotes() {
        journalEntry.setStatus(JournalEntry.Status.POSTED);
        when(journalEntryRepository.findById(10L)).thenReturn(Optional.of(journalEntry));
        when(journalEntryRepository.save(any(JournalEntry.class))).thenAnswer(inv -> inv.getArgument(0));

        JournalEntry result = service.rejectJournalEntry(10L, "Missing attachment");

        assertThat(result.getApprovalStatus()).isEqualTo(JournalEntry.ApprovalStatus.REJECTED);
        assertThat(result.getStatus()).isEqualTo(JournalEntry.Status.DRAFT);
        assertThat(result.getApprovalNotes()).isEqualTo("Missing attachment");
    }

    @Test
    void rejectJournalEntry_withNonExistentId_throwsResourceNotFoundException() {
        when(journalEntryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.rejectJournalEntry(99L, "reason"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void postJournalEntry_whenApproved_setsStatusToPostedAndReturnsLedgerEntry() {
        journalEntry.setApprovalStatus(JournalEntry.ApprovalStatus.APPROVED);

        GeneralLedger ledger = new GeneralLedger();
        when(journalEntryRepository.findById(10L)).thenReturn(Optional.of(journalEntry));
        when(journalEntryRepository.save(any(JournalEntry.class))).thenReturn(journalEntry);
        when(generalLedgerRepository.findByJournalEntryId(10L)).thenReturn(List.of(ledger));

        GeneralLedger result = service.postJournalEntry(10L);

        assertThat(journalEntry.getStatus()).isEqualTo(JournalEntry.Status.POSTED);
        assertThat(result).isEqualTo(ledger);
    }

    @Test
    void postJournalEntry_whenNotApproved_throwsIllegalStateException() {
        journalEntry.setApprovalStatus(JournalEntry.ApprovalStatus.PENDING);
        when(journalEntryRepository.findById(10L)).thenReturn(Optional.of(journalEntry));

        assertThatThrownBy(() -> service.postJournalEntry(10L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("approved");
    }

    @Test
    void postJournalEntry_whenApprovedButNoLedgerEntries_throwsResourceNotFoundException() {
        journalEntry.setApprovalStatus(JournalEntry.ApprovalStatus.APPROVED);
        when(journalEntryRepository.findById(10L)).thenReturn(Optional.of(journalEntry));
        when(journalEntryRepository.save(any(JournalEntry.class))).thenReturn(journalEntry);
        when(generalLedgerRepository.findByJournalEntryId(10L)).thenReturn(List.of());

        assertThatThrownBy(() -> service.postJournalEntry(10L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void postJournalEntry_withNonExistentId_throwsResourceNotFoundException() {
        when(journalEntryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.postJournalEntry(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteJournalEntry_deletesExistingEntry() {
        when(journalEntryRepository.findById(10L)).thenReturn(Optional.of(journalEntry));

        service.deleteJournalEntry(10L);

        verify(journalEntryRepository).deleteById(10L);
    }

    @Test
    void deleteJournalEntry_withNonExistentId_throwsResourceNotFoundException() {
        when(journalEntryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteJournalEntry(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(journalEntryRepository, never()).deleteById(any());
    }

    @Test
    void getAccountBalance_forDebitNormalAccount_returnsDebitMinusCredit() {
        account.setNormalBalance(ChartOfAccounts.BalanceSide.DEBIT);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(generalLedgerRepository.getTotalDebits(1L)).thenReturn(new BigDecimal("1500.00"));
        when(generalLedgerRepository.getTotalCredits(1L)).thenReturn(new BigDecimal("500.00"));

        BigDecimal balance = service.getAccountBalance(1L);

        assertThat(balance).isEqualByComparingTo("1000.00");
    }

    @Test
    void getAccountBalance_forCreditNormalAccount_returnsCreditMinusDebit() {
        account.setNormalBalance(ChartOfAccounts.BalanceSide.CREDIT);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(generalLedgerRepository.getTotalDebits(1L)).thenReturn(new BigDecimal("300.00"));
        when(generalLedgerRepository.getTotalCredits(1L)).thenReturn(new BigDecimal("800.00"));

        BigDecimal balance = service.getAccountBalance(1L);

        assertThat(balance).isEqualByComparingTo("500.00");
    }

    @Test
    void getAccountBalance_treatsNullDebitsAndCreditsAsZero() {
        account.setNormalBalance(ChartOfAccounts.BalanceSide.DEBIT);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(generalLedgerRepository.getTotalDebits(1L)).thenReturn(null);
        when(generalLedgerRepository.getTotalCredits(1L)).thenReturn(null);

        BigDecimal balance = service.getAccountBalance(1L);

        assertThat(balance).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void getAccountBalance_withNonExistentAccount_throwsResourceNotFoundException() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getAccountBalance(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getTrialBalance_returnsEmptyList() {
        assertThat(service.getTrialBalance(LocalDate.now())).isEmpty();
    }

    @Test
    void getBalanceSheet_returnsEmptyList() {
        assertThat(service.getBalanceSheet(LocalDate.now())).isEmpty();
    }

    @Test
    void getIncomeStatement_returnsEmptyList() {
        assertThat(service.getIncomeStatement(LocalDate.now().minusMonths(1), LocalDate.now())).isEmpty();
    }

    @Test
    void getTotalAssets_returnsZero() {
        assertThat(service.getTotalAssets(LocalDate.now())).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void getTotalLiabilities_returnsZero() {
        assertThat(service.getTotalLiabilities(LocalDate.now())).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void getTotalEquity_returnsZero() {
        assertThat(service.getTotalEquity(LocalDate.now())).isEqualByComparingTo(BigDecimal.ZERO);
    }
}

