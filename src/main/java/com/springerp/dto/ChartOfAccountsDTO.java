package com.springerp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Chart of Accounts
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartOfAccountsDTO {
    private Long id;
    private String accountCode;
    private String accountName;
    private String accountType;
    private String accountCategory;
    private String description;
    private String normalBalance;
    private BigDecimal currentBalance;
    private Boolean isActive;
    private Long parentAccountId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

