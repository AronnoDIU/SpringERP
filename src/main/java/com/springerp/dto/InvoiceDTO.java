package com.springerp.dto;

import com.springerp.entity.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDTO {
    private Long id;
    private Long companyId;
    private String companyName;
    private String invoiceNumber;
    private Long orderId;
    private Long customerId;
    private String customerName;
    private LocalDateTime invoiceDate;
    private LocalDateTime dueDate;
    private InvoiceStatus status;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String billingAddress;
    private String shippingAddress;
    private String paymentTerms;
    private String notes;
    private List<InvoiceItemDTO> items;
    private String createdByUsername;
    private String updatedByUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
