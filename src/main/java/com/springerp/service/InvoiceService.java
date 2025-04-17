package com.springerp.service;

import com.springerp.dto.InvoiceDTO;
import com.springerp.entity.InvoiceStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InvoiceService {
    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);
    InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO);
    InvoiceDTO getInvoiceById(Long id);
    List<InvoiceDTO> getAllInvoices(Pageable pageable);
    void deleteInvoice(Long id);
    List<InvoiceDTO> getInvoicesByStatus(InvoiceStatus status);
    InvoiceDTO updateInvoiceStatus(Long id, InvoiceStatus status);
    byte[] generateInvoicePdf(Long id);
}
