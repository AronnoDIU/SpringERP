package com.springerp.service;

import com.springerp.dto.InvoiceDTO;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.entity.Invoice;
import com.springerp.entity.InvoiceItem;
import com.springerp.entity.InvoiceStatus;
import com.springerp.repository.InvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
    
    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;
    
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, ModelMapper modelMapper) {
        this.invoiceRepository = invoiceRepository;
        this.modelMapper = modelMapper;
    }
    
    @Override
    public InvoiceDTO createInvoice(Long companyId, InvoiceDTO invoiceDTO) {
        Invoice invoice = modelMapper.map(invoiceDTO, Invoice.class);
        invoice.setInvoiceNumber(generateInvoiceNumber(companyId));
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.setCompanyId(companyId);
        
        calculateTotals(invoice);
        
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return modelMapper.map(savedInvoice, InvoiceDTO.class);
    }

    @Override
    public InvoiceDTO updateInvoice(Long companyId, Long id, InvoiceDTO invoiceDTO) {
        Invoice existingInvoice = invoiceRepository.findByCompanyIdAndId(companyId, id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        
        modelMapper.map(invoiceDTO, existingInvoice);
        calculateTotals(existingInvoice);
        
        Invoice updatedInvoice = invoiceRepository.save(existingInvoice);
        return modelMapper.map(updatedInvoice, InvoiceDTO.class);
    }

    @Override
    public InvoiceDTO getInvoiceById(Long companyId, Long id) {
        Invoice invoice = invoiceRepository.findByCompanyIdAndId(companyId, id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        return modelMapper.map(invoice, InvoiceDTO.class);
    }

    @Override
    public Page<InvoiceDTO> getAllInvoices(Long companyId, Pageable pageable) {
        return invoiceRepository.findByCompanyId(companyId, pageable)
            .map(invoice -> modelMapper.map(invoice, InvoiceDTO.class));
    }

    @Override
    public void deleteInvoice(Long companyId, Long id) {
        Invoice invoice = invoiceRepository.findByCompanyIdAndId(companyId, id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        invoiceRepository.delete(invoice);
    }

    @Override
    public List<InvoiceDTO> getInvoicesByStatus(Long companyId, InvoiceStatus status) {
        return invoiceRepository.findByCompanyIdAndStatus(companyId, status)
            .stream()
            .map(invoice -> modelMapper.map(invoice, InvoiceDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO updateInvoiceStatus(Long companyId, Long id, InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findByCompanyIdAndId(companyId, id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        
        invoice.setStatus(status);
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return modelMapper.map(updatedInvoice, InvoiceDTO.class);
    }

@Override
public List<InvoiceDTO> getInvoicesByCustomer(Long companyId, Long customerId) {
    return invoiceRepository.findByCompanyIdAndCustomerId(companyId, customerId)
        .stream()
        .map(invoice -> modelMapper.map(invoice, InvoiceDTO.class))
        .collect(Collectors.toList());
}

@Override
public List<InvoiceDTO> getInvoicesByOrder(Long companyId, Long orderId) {
    return invoiceRepository.findByCompanyIdAndOrderId(companyId, orderId)
        .stream()
        .map(invoice -> modelMapper.map(invoice, InvoiceDTO.class))
        .collect(Collectors.toList());
}
    @Override
    public byte[] generateInvoicePdf(Long companyId, Long id) {
        // Implement PDF generation logic here
        throw new UnsupportedOperationException("PDF generation not implemented yet");
    }
    
    private String generateInvoiceNumber(Long companyId) {
        return String.format("INV-%d-%d", companyId, System.currentTimeMillis());
    }
    
    private void calculateTotals(Invoice invoice) {
        BigDecimal subtotal = invoice.getItems().stream()
            .map(InvoiceItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal taxAmount = subtotal.multiply(new BigDecimal("0.10")); // 10% tax rate
        BigDecimal totalAmount = subtotal.add(taxAmount);
        
        invoice.setSubtotal(subtotal);
        invoice.setTaxAmount(taxAmount);
        invoice.setTotalAmount(totalAmount);
    }
}
