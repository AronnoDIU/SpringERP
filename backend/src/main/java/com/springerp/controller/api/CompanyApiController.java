package com.springerp.controller.api;

import com.springerp.entity.Company;
import com.springerp.service.CompanyManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * REST API for company management (for admin panel and third-party integrations).
 */
@RestController
@RequestMapping("/api/v1/admin/companies")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CompanyApiController {

    private final CompanyManagementService companyService;

    @GetMapping
    public ResponseEntity<Page<Company>> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                companyService.getAllCompanies(PageRequest.of(page, size))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        return companyService.getCompany(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<Map<String, Object>> getCompanyStatistics(@PathVariable Long id) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("companyId", id);
        stats.put("activeUsers", companyService.getTotalActiveUsers(id));
        stats.put("totalApiCalls", companyService.getTotalApiCalls(id));
        stats.put("totalDataSize", companyService.getTotalDataSize(id));

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{id}/hierarchy")
    public ResponseEntity<?> getCompanyHierarchy(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("company", companyService.getCompany(id));
        response.put("hierarchy", companyService.getCompanyHierarchy(id));
        response.put("subsidiaries", companyService.getSubsidiaries(id));
        response.put("parentChain", companyService.getParentChain(id));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Company> activateCompany(@PathVariable Long id) {
        try {
            Company company = companyService.activateCompany(id);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Company> deactivateCompany(@PathVariable Long id) {
        try {
            Company company = companyService.deactivateCompany(id);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/suspend")
    public ResponseEntity<Company> suspendCompany(
            @PathVariable Long id,
            @RequestParam String reason) {
        try {
            Company company = companyService.suspendCompany(id, reason);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Company>> searchCompanies(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                companyService.searchCompanies(query, PageRequest.of(page, size))
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Company>> getCompaniesByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Company> companies = companyService.getAllCompanies(PageRequest.of(page, size))
                .stream()
                .filter(c -> status.equalsIgnoreCase(c.getStatus()))
                .toList();

        return ResponseEntity.ok(companies);
    }
}

