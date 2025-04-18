package com.springerp.repository;

import com.springerp.entity.Invoice;
import com.springerp.entity.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByCompanyIdAndStatus(Long companyId, InvoiceStatus status);
    List<Invoice> findByCompanyIdAndCustomerId(Long companyId, Long customerId);
    List<Invoice> findByCompanyIdAndOrderId(Long companyId, Long orderId);
    Optional<Invoice> findByCompanyIdAndInvoiceNumber(Long companyId, String invoiceNumber);
    Optional<Invoice> findByCompanyIdAndId(Long companyId, Long id);
    Page<Invoice> findByCompanyId(Long companyId, Pageable pageable);
}
