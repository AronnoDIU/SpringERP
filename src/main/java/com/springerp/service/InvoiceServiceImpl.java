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
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        Invoice invoice = modelMapper.map(invoiceDTO, Invoice.class);
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setStatus(InvoiceStatus.DRAFT);
        
        calculateTotals(invoice);
        
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return modelMapper.map(savedInvoice, InvoiceDTO.class);
    }
    
    @Override
    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        Invoice existingInvoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        
        modelMapper.map(invoiceDTO, existingInvoice);
        calculateTotals(existingInvoice);
        
        Invoice updatedInvoice = invoiceRepository.save(existingInvoice);
        return modelMapper.map(updatedInvoice, InvoiceDTO.class);
    }
    
    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        return modelMapper.map(invoice, InvoiceDTO.class);
    }
    
    @Override
    public List<InvoiceDTO> getAllInvoices(Pageable pageable) {
        return invoiceRepository.findAll(pageable)
            .getContent()
            .stream()
            .map(invoice -> modelMapper.map(invoice, InvoiceDTO.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        invoiceRepository.delete(invoice);
    }
    
    @Override
    public List<InvoiceDTO> getInvoicesByStatus(InvoiceStatus status) {
        return invoiceRepository.findByStatus(status)
            .stream()
            .map(invoice -> modelMapper.map(invoice, InvoiceDTO.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public InvoiceDTO updateInvoiceStatus(Long id, InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        
        invoice.setStatus(status);
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return modelMapper.map(updatedInvoice, InvoiceDTO.class);
    }
    
    @Override
    public byte[] generateInvoicePdf(Long id) {
        // Implement PDF generation logic here
        throw new UnsupportedOperationException("PDF generation not implemented yet");
    }
    
    private String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis();
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
