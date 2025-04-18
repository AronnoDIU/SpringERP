package com.springerp.repository;

import com.springerp.entity.Company;
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
    List<Invoice> findByCompanyAndStatus(Company company, InvoiceStatus status);
    List<Invoice> findByCompanyAndCustomerId(Company company, Long customerId);
    List<Invoice> findByCompanyAndOrderId(Company company, Long orderId);
    Optional<Invoice> findByCompanyAndInvoiceNumber(Company company, String invoiceNumber);
    Optional<Invoice> findByCompanyIdAndId(Long companyId, Long id);
    Page<Invoice> findByCompany(Company company, Pageable pageable);
    Optional<Invoice> findByCompanyAndId(Company company, Long id);
}
