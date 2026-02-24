package com.springerp.dto;

import com.springerp.entity.InvoiceStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "Invoice date is required")
    private LocalDateTime invoiceDate;

    private LocalDateTime dueDate;
    private InvoiceStatus status;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;

    @Size(max = 255)
    private String billingAddress;

    @Size(max = 255)
    private String shippingAddress;

    @Size(max = 100)
    private String paymentTerms;

    @Size(max = 500)
    private String notes;

    @Valid
    private List<InvoiceItemDTO> items;

    private String createdByUsername;
    private String updatedByUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
