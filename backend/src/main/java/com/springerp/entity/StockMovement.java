package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Stock Movement entity for tracking inventory transactions.
 * Records all stock increases, decreases, and adjustments.
 */
@Entity
@Table(name = "stock_movements", indexes = {
        @Index(name = "idx_inventory_item_id", columnList = "inventory_item_id"),
        @Index(name = "idx_movement_date", columnList = "movement_date"),
        @Index(name = "idx_movement_type", columnList = "movement_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;

    @Column(name = "movement_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementType movementType; // PURCHASE, SALE, ADJUSTMENT, TRANSFER, RETURN, DAMAGE

    @Column(name = "quantity_moved", nullable = false)
    private Integer quantityMoved;

    @Column(name = "unit_price", precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_value", precision = 19, scale = 2)
    private BigDecimal totalValue;

    @Column(name = "movement_date", nullable = false)
    private LocalDate movementDate;

    @Column(name = "reference_document")
    private String referenceDocument; // Invoice, PO, etc.

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "from_location")
    private String fromLocation;

    @Column(name = "to_location")
    private String toLocation;

    @Column(name = "reason")
    private String reason;

    @Column(name = "notes", columnDefinition = "LONGTEXT")
    private String notes;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING; // PENDING, APPROVED, POSTED

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum MovementType {
        PURCHASE, SALE, ADJUSTMENT, TRANSFER, RETURN, DAMAGE, WASTE, SAMPLE, SHRINKAGE
    }

    public enum Status {
        PENDING, APPROVED, POSTED, REJECTED
    }
}

