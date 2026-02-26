package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Warehouse entity for managing different storage locations.
 */
@Entity
@Table(name = "warehouses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse extends BaseEntity {

    @Column(name = "warehouse_name", nullable = false, unique = true)
    private String warehouseName;

    @Column(name = "warehouse_code", nullable = false, unique = true)
    private String warehouseCode;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "total_capacity")
    private Integer totalCapacity;

    @Column(name = "current_utilization")
    private Integer currentUtilization = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "warehouse_type")
    @Enumerated(EnumType.STRING)
    private WarehouseType warehouseType; // PRIMARY, SECONDARY, RETURN, DISTRIBUTION

    @Column(name = "tenant_id")
    private Long tenantId;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseLocation> locations = new ArrayList<>();

    public enum WarehouseType {
        PRIMARY, SECONDARY, RETURN, DISTRIBUTION, TRANSIT
    }
}

