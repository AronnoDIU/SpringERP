package com.springerp.controller;
import com.springerp.entity.ChartOfAccounts;
import com.springerp.entity.GeneralLedger;
import com.springerp.entity.JournalEntry;
import com.springerp.service.AccountingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/accounting")
@RequiredArgsConstructor
@Slf4j
public class AccountingController {
    private final AccountingService accountingService;
    // ─── Chart of Accounts ──────────────────────────────────────────────────
    @GetMapping("/chart-of-accounts")
    public ResponseEntity<Page<ChartOfAccounts>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(accountingService.getAllAccounts(PageRequest.of(page, size)));
    }
    @GetMapping("/chart-of-accounts/{id}")
    public ResponseEntity<ChartOfAccounts> getAccountById(@PathVariable Long id) {
        return accountingService.getAccount(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/chart-of-accounts")
    public ResponseEntity<ChartOfAccounts> createAccount(@RequestBody ChartOfAccounts account) {
        return new ResponseEntity<>(accountingService.createAccount(account), HttpStatus.CREATED);
    }
    @PutMapping("/chart-of-accounts/{id}")
    public ResponseEntity<ChartOfAccounts> updateAccount(@PathVariable Long id, @RequestBody ChartOfAccounts account) {
        return ResponseEntity.ok(accountingService.updateAccount(id, account));
    }
    @DeleteMapping("/chart-of-accounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountingService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
    // ─── Journal Entries ─────────────────────────────────────────────────────
    @GetMapping("/journal-entries")
    public ResponseEntity<Page<JournalEntry>> getAllJournalEntries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(accountingService.getAllJournalEntries(PageRequest.of(page, size)));
    }
    @GetMapping("/journal-entries/{id}")
    public ResponseEntity<JournalEntry> getJournalEntry(@PathVariable Long id) {
        return accountingService.getJournalEntry(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/journal-entries")
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry entry) {
        return new ResponseEntity<>(accountingService.createJournalEntry(entry), HttpStatus.CREATED);
    }
    @PutMapping("/journal-entries/{id}/post")
    public ResponseEntity<GeneralLedger> postJournalEntry(@PathVariable Long id) {
        return ResponseEntity.ok(accountingService.postJournalEntry(id));
    }
}
