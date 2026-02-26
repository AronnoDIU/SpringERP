package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Asset entity for managing fixed assets and equipment.
 */
@Entity
@Table(name = "assets", indexes = {
        @Index(name = "idx_asset_code", columnList = "asset_code"),
        @Index(name = "idx_asset_type", columnList = "asset_type"),
        @Index(name = "idx_acquisition_date", columnList = "acquisition_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset extends BaseEntity {

    @Column(name = "asset_code", nullable = false, unique = true)
    private String assetCode;

    @Column(name = "asset_name", nullable = false)
    private String assetName;

    @Column(name = "asset_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssetType assetType; // PROPERTY, MACHINERY, VEHICLE, FURNITURE, EQUIPMENT, COMPUTER

    @Column(name = "category")
    private String category;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "acquisition_date", nullable = false)
    private LocalDate acquisitionDate;

    @Column(name = "acquisition_cost", precision = 19, scale = 2, nullable = false)
    private BigDecimal acquisitionCost;

    @Column(name = "salvage_value", precision = 19, scale = 2)
    private BigDecimal salvageValue = BigDecimal.ZERO;

    @Column(name = "useful_life_years")
    private Integer usefulLifeYears;

    @Column(name = "depreciation_method")
    @Enumerated(EnumType.STRING)
    private DepreciationMethod depreciationMethod; // STRAIGHT_LINE, DIMINISHING_VALUE, UNITS_OF_PRODUCTION

    @Column(name = "accumulated_depreciation", precision = 19, scale = 2)
    private BigDecimal accumulatedDepreciation = BigDecimal.ZERO;

    @Column(name = "book_value", precision = 19, scale = 2)
    private BigDecimal bookValue;

    @Column(name = "currency", length = 3)
    private String currency = "USD";

    @Column(name = "location")
    private String location;

    @Column(name = "responsible_person_id")
    private Long responsiblePersonId; // Employee ID

    @Column(name = "asset_status")
    @Enumerated(EnumType.STRING)
    private AssetStatus assetStatus = AssetStatus.IN_SERVICE; // IN_SERVICE, IDLE, DISPOSED, UNDER_REPAIR

    @Column(name = "disposal_date")
    private LocalDate disposalDate;

    @Column(name = "disposal_value", precision = 19, scale = 2)
    private BigDecimal disposalValue;

    @Column(name = "disposal_method")
    private String disposalMethod;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "model_number")
    private String modelNumber;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "warranty_expiry_date")
    private LocalDate warrantyExpiryDate;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum AssetType {
        PROPERTY, MACHINERY, VEHICLE, FURNITURE, EQUIPMENT, COMPUTER, INTANGIBLE
    }

    public enum DepreciationMethod {
        STRAIGHT_LINE, DIMINISHING_VALUE, UNITS_OF_PRODUCTION, DOUBLE_DECLINING_BALANCE
    }

    public enum AssetStatus {
        IN_SERVICE, IDLE, UNDER_REPAIR, DISPOSED, RETIRED, LOST
    }
}

