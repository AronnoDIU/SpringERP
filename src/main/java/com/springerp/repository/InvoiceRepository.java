package com.springerp.repository;

import com.springerp.entity.Invoice;
import com.springerp.entity.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByStatus(InvoiceStatus status);
    List<Invoice> findByOrderId(Long orderId);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
