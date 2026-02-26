package com.springerp.service;

import com.springerp.entity.ReportTemplate;
import com.springerp.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for reporting and business intelligence.
 */
public interface ReportingService {

    // Report Template Management
    ReportTemplate createReportTemplate(ReportTemplate template);
    ReportTemplate updateReportTemplate(Long id, ReportTemplate template);
    Optional<ReportTemplate> getReportTemplate(Long id);
    Optional<ReportTemplate> getReportTemplateByCode(String code);
    Page<ReportTemplate> getAllReportTemplates(Pageable pageable);
    Page<ReportTemplate> getReportsByType(ReportTemplate.ReportType type, Pageable pageable);

    // Report Generation
    byte[] generateReport(Long templateId, Map<String, Object> parameters);
    byte[] generateReportPDF(Long templateId, Map<String, Object> parameters);
    byte[] generateReportExcel(Long templateId, Map<String, Object> parameters);
    List<Map<String, Object>> getReportData(Long templateId, Map<String, Object> parameters);

    // Predefined Reports
    byte[] generateFinancialReport(LocalDate startDate, LocalDate endDate);
    byte[] generateInventoryReport(LocalDate asOfDate);
    byte[] generateSalesReport(LocalDate startDate, LocalDate endDate);
    byte[] generatePurchaseReport(LocalDate startDate, LocalDate endDate);
    byte[] generatePayrollReport(LocalDate payrollMonth);
    byte[] generateAssetReport(LocalDate asOfDate);
    byte[] generateCustomerReport();
    byte[] generateSupplierReport();

    // Budget Management
    Budget createBudget(Budget budget);
    Budget updateBudget(Long id, Budget budget);
    Optional<Budget> getBudget(Long id);
    Page<Budget> getAllBudgets(Pageable pageable);
    Page<Budget> getBudgetsByPeriod(LocalDate period, Pageable pageable);
    Page<Budget> getBudgetsByDepartment(Long departmentId, Pageable pageable);

    // Budget vs Actual Analysis
    Map<String, Object> getBudgetVarianceAnalysis(Long departmentId, LocalDate period);
    List<Object[]> getBudgetVarianceReport(LocalDate period);
    List<Object[]> getDepartmentBudgetAnalysis(Long departmentId);

    // Dashboard Data
    Map<String, Object> getFinancialDashboard(LocalDate asOfDate);
    Map<String, Object> getSalesDashboard(LocalDate startDate, LocalDate endDate);
    Map<String, Object> getOperationalDashboard();
    Map<String, Object> getHRDashboard();

    // Report Scheduling
    ReportTemplate scheduleReport(Long templateId, String frequency, List<String> recipients);
    void executeScheduledReports();

    void deleteReportTemplate(Long id);
    void deleteBudget(Long id);
}

