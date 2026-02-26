package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Inventory Item entity for tracking stock levels and inventory movements.
 */
@Entity
@Table(name = "inventory_items", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_warehouse_id", columnList = "warehouse_id"),
        @Index(name = "idx_sku", columnList = "sku")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem extends BaseEntity {

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "quantity_on_hand", nullable = false)
    private Integer quantityOnHand = 0;

    @Column(name = "quantity_reserved")
    private Integer quantityReserved = 0;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable = 0;

    @Column(name = "reorder_level", nullable = false)
    private Integer reorderLevel = 10;

    @Column(name = "reorder_quantity")
    private Integer reorderQuantity = 50;

    @Column(name = "unit_cost", precision = 19, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "last_counted_at")
    private LocalDateTime lastCountedAt;

    @Column(name = "last_movement_date")
    private LocalDateTime lastMovementDate;

    @Column(name = "location")
    private String location; // Physical location in warehouse

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "tenant_id")
    private Long tenantId;

    public Integer getAvailableQuantity() {
        return quantityOnHand - quantityReserved;
    }

    public Boolean isLowStock() {
        return getAvailableQuantity() <= reorderLevel;
    }
}

