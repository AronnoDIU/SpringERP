package com.springerp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for Asset
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDTO {
    private Long id;
    private String assetCode;
    private String assetName;
    private String assetType;
    private String category;
    private String description;
    private BigDecimal acquisitionCost;
    private BigDecimal salvageValue;
    private Integer usefulLifeYears;
    private String depreciationMethod;
    private BigDecimal bookValue;
    private String assetStatus;
    private String location;
    private Long responsiblePersonId;
}

