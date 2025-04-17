package com.springerp.service;

import com.springerp.exception.ResourceNotFoundException;
import com.springerp.entity.Supplier;
import com.springerp.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class SupplierService {
    private final SupplierRepository supplierRepository;
    
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }
    
    public Supplier createSupplier(Supplier supplier) {
        log.info("Creating new supplier: {}", supplier.getName());
        return supplierRepository.save(supplier);
    }
    
    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
            
        supplier.setName(supplierDetails.getName());
        supplier.setEmail(supplierDetails.getEmail());
        supplier.setPhone(supplierDetails.getPhone());
        supplier.setAddress(supplierDetails.getAddress());
        supplier.setCompanyName(supplierDetails.getCompanyName());
        
        log.info("Updating supplier with id: {}", id);
        return supplierRepository.save(supplier);
    }
    
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier with id: {}", id);
        supplierRepository.deleteById(id);
    }
    
    public Supplier getSupplier(Long id) {
        return supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
    }
    
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    
    public List<Supplier> searchSuppliersByName(String name) {
        return supplierRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Supplier> searchSuppliersByCompany(String companyName) {
        return supplierRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }
    
    public Optional<Supplier> findSupplierByEmail(String email) {
        return supplierRepository.findByEmail(email);
    }
}
