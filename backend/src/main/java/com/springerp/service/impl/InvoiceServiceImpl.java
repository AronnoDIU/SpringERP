package com.springerp.service.impl;

import com.springerp.dto.InvoiceDTO;
import com.springerp.entity.Company;
import com.springerp.entity.Invoice;
import com.springerp.entity.InvoiceItem;
import com.springerp.entity.InvoiceStatus;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.repository.CompanyRepository;
import com.springerp.repository.InvoiceRepository;
import com.springerp.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public InvoiceDTO createInvoice(Long companyId, InvoiceDTO invoiceDTO) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
        Invoice invoice = modelMapper.map(invoiceDTO, Invoice.class);
        invoice.setInvoiceNumber(generateInvoiceNumber(companyId));
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.setCompany(company);
        calculateTotals(invoice);
        return modelMapper.map(invoiceRepository.save(invoice), InvoiceDTO.class);
    }

    @Override
    @Transactional
    public InvoiceDTO updateInvoice(Long companyId, Long id, InvoiceDTO invoiceDTO) {
        Invoice existing = invoiceRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        modelMapper.map(invoiceDTO, existing);
        calculateTotals(existing);
        return modelMapper.map(invoiceRepository.save(existing), InvoiceDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceDTO getInvoiceById(Long companyId, Long id) {
        return modelMapper.map(
                invoiceRepository.findByCompanyIdAndId(companyId, id)
                        .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id)),
                InvoiceDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> getAllInvoices(Long companyId, Pageable pageable) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
        return invoiceRepository.findByCompany(company, pageable)
                .map(invoice -> modelMapper.map(invoice, InvoiceDTO.class));
    }

    @Override
    @Transactional
    public void deleteInvoice(Long companyId, Long id) {
        Invoice invoice = invoiceRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        invoiceRepository.delete(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> getInvoicesByStatus(Long companyId, InvoiceStatus status) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
        return invoiceRepository.findByCompanyAndStatus(company, status).stream()
                .map(inv -> modelMapper.map(inv, InvoiceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InvoiceDTO updateInvoiceStatus(Long companyId, Long id, InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        invoice.setStatus(status);
        return modelMapper.map(invoiceRepository.save(invoice), InvoiceDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> getInvoicesByCustomer(Long companyId, Long customerId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
        return invoiceRepository.findByCompanyAndCustomerId(company, customerId).stream()
                .map(inv -> modelMapper.map(inv, InvoiceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> getInvoicesByOrder(Long companyId, Long orderId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
        return invoiceRepository.findByCompanyAndOrderId(company, orderId).stream()
                .map(inv -> modelMapper.map(inv, InvoiceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public byte[] generateInvoicePdf(Long companyId, Long id) {
        throw new UnsupportedOperationException("PDF generation is not yet implemented");
    }

    private String generateInvoiceNumber(Long companyId) {
        return String.format("INV-%d-%d", companyId, System.currentTimeMillis());
    }

    private void calculateTotals(Invoice invoice) {
        if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
            invoice.setSubtotal(BigDecimal.ZERO);
            invoice.setTaxAmount(BigDecimal.ZERO);
            invoice.setTotalAmount(BigDecimal.ZERO);
            return;
        }
        BigDecimal subtotal = invoice.getItems().stream()
                .map(InvoiceItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal taxAmount = subtotal.multiply(new BigDecimal("0.10"));
        invoice.setSubtotal(subtotal);
        invoice.setTaxAmount(taxAmount);
        invoice.setTotalAmount(subtotal.add(taxAmount));
    }
}

