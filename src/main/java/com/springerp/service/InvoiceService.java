package com.springerp.service;

import com.springerp.dto.InvoiceDTO;
import com.springerp.entity.InvoiceStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InvoiceService {
    InvoiceDTO createInvoice(Long companyId, InvoiceDTO invoiceDTO);
    InvoiceDTO updateInvoice(Long companyId, Long id, InvoiceDTO invoiceDTO);
    InvoiceDTO getInvoiceById(Long companyId, Long id);
    Page<InvoiceDTO> getAllInvoices(Long companyId, Pageable pageable);
    void deleteInvoice(Long companyId, Long id);
    List<InvoiceDTO> getInvoicesByStatus(Long companyId, InvoiceStatus status);
    List<InvoiceDTO> getInvoicesByCustomer(Long companyId, Long customerId);
    List<InvoiceDTO> getInvoicesByOrder(Long companyId, Long orderId);
    InvoiceDTO updateInvoiceStatus(Long companyId, Long id, InvoiceStatus status);
    byte[] generateInvoicePdf(Long companyId, Long id);
}
