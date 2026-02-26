package com.springerp.util;

import org.springframework.stereotype.Component;

/**
 * Utility class for generating unique identifiers and codes.
 */
@Component
public class CodeGeneratorUtil {

    /**
     * Generate employee ID in format EMP-YYYY-0001
     */
    public String generateEmployeeId(int sequenceNumber) {
        return String.format("EMP-%d-%05d", java.time.LocalDate.now().getYear(), sequenceNumber);
    }

    /**
     * Generate department code in format DEPT-0001
     */
    public String generateDepartmentCode(int sequenceNumber) {
        return String.format("DEPT-%04d", sequenceNumber);
    }

    /**
     * Generate asset code in format ASSET-YYYY-0001
     */
    public String generateAssetCode(int sequenceNumber) {
        return String.format("ASSET-%d-%05d", java.time.LocalDate.now().getYear(), sequenceNumber);
    }

    /**
     * Generate account code in format ACC-0000
     */
    public String generateAccountCode(int sequenceNumber) {
        return String.format("ACC-%04d", sequenceNumber);
    }

    /**
     * Generate journal entry number in format JE-YYYYMMDD-0001
     */
    public String generateJournalEntryNumber(int sequenceNumber) {
        String dateStr = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        return String.format("JE-%s-%04d", dateStr, sequenceNumber);
    }

    /**
     * Generate warehouse code in format WH-0001
     */
    public String generateWarehouseCode(int sequenceNumber) {
        return String.format("WH-%04d", sequenceNumber);
    }

    /**
     * Generate location code in format LOC-0001
     */
    public String generateLocationCode(int sequenceNumber) {
        return String.format("LOC-%04d", sequenceNumber);
    }

    /**
     * Generate workflow instance number in format WF-YYYYMMDD-0001
     */
    public String generateWorkflowInstanceNumber(int sequenceNumber) {
        String dateStr = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        return String.format("WF-%s-%04d", dateStr, sequenceNumber);
    }

    /**
     * Generate budget code in format BUDGET-YYYY-0001
     */
    public String generateBudgetCode(int sequenceNumber) {
        return String.format("BUDGET-%d-%04d", java.time.LocalDate.now().getYear(), sequenceNumber);
    }
}

