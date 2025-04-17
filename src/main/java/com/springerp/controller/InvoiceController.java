package com.springerp.controller;

import com.springerp.exception.ErrorResponse;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.dto.InvoiceDTO;
import com.springerp.entity.InvoiceStatus;
import com.springerp.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
@Slf4j
public class InvoiceController {
    
    private final InvoiceService invoiceService;
    
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceDTO createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.createInvoice(invoiceDTO);
    }
    
    @PutMapping("/{id}")
    public InvoiceDTO updateInvoice(@PathVariable Long id, @Valid @RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.updateInvoice(id, invoiceDTO);
    }
    
    @GetMapping("/{id}")
    public InvoiceDTO getInvoice(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id);
    }
    
    @GetMapping
    public List<InvoiceDTO> getAllInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return invoiceService.getAllInvoices(pageable);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
    }
    
    @GetMapping("/status/{status}")
    public List<InvoiceDTO> getInvoicesByStatus(@PathVariable InvoiceStatus status) {
        return invoiceService.getInvoicesByStatus(status);
    }
    
    @PatchMapping("/{id}/status")
    public InvoiceDTO updateInvoiceStatus(
            @PathVariable Long id,
            @RequestParam InvoiceStatus status) {
        return invoiceService.updateInvoiceStatus(id, status);
    }
    
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generateInvoicePdf(@PathVariable Long id) {
        byte[] pdfContent = invoiceService.generateInvoicePdf(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("invoice-" + id + ".pdf")
                .build());
        
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}
