package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    
    private String orderNumber;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
    
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<PurchaseOrderItem> items = new ArrayList<>();
    
    private String deliveryAddress;
    private LocalDateTime expectedDeliveryDate;
    private String paymentStatus;
    private String paymentTerms;
    
    private String approvedBy;
    private LocalDateTime approvalDate;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
