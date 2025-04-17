package com.springerp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierInteractionDTO {
    private Long id;
    private String type;
    private String description;
    private LocalDateTime interactionDate;
    private String outcome;
    private String nextSteps;
    private String createdBy;
    private LocalDateTime createdAt;
}
