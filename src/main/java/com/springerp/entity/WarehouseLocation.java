package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Warehouse Location entity for tracking storage locations within a warehouse.
 */
@Entity
@Table(name = "warehouse_locations", indexes = {
        @Index(name = "idx_warehouse_id", columnList = "warehouse_id"),
        @Index(name = "idx_location_code", columnList = "location_code")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseLocation extends BaseEntity {

    @Column(name = "location_code", nullable = false)
    private String locationCode; // e.g., AISLE-1-SHELF-2-BIN-3

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "aisle")
    private String aisle;

    @Column(name = "rack")
    private String rack;

    @Column(name = "shelf")
    private String shelf;

    @Column(name = "bin")
    private String bin;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "current_quantity")
    private Integer currentQuantity = 0;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "location_type")
    @Enumerated(EnumType.STRING)
    private LocationType locationType; // STANDARD, HAZMAT, CLIMATE, SECURE, QUARANTINE

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum LocationType {
        STANDARD, HAZMAT, CLIMATE_CONTROLLED, SECURE, QUARANTINE, RECEIVING, SHIPPING
    }
}

