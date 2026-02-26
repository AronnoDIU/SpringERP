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
@Slf4j
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public Supplier createSupplier(Supplier supplier) {
        log.info("Creating new supplier: {}", supplier.getName());
        return supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
        Supplier supplier = supplierRepository.findById(id)
            .filter(s -> !Boolean.TRUE.equals(s.getIsDeleted()))
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        supplier.setName(supplierDetails.getName());
        supplier.setEmail(supplierDetails.getEmail());
        supplier.setPhone(supplierDetails.getPhone());
        supplier.setAddress(supplierDetails.getAddress());
        supplier.setCompanyName(supplierDetails.getCompanyName());

        log.info("Updating supplier with id: {}", id);
        return supplierRepository.save(supplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .filter(s -> !Boolean.TRUE.equals(s.getIsDeleted()))
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        supplier.softDelete();
        supplierRepository.save(supplier);
        log.info("Soft-deleted supplier with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Supplier getSupplier(Long id) {
        return supplierRepository.findById(id)
            .filter(s -> !Boolean.TRUE.equals(s.getIsDeleted()))
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findByIsDeletedFalse();
    }

    @Transactional(readOnly = true)
    public List<Supplier> searchSuppliersByName(String name) {
        return supplierRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Supplier> searchSuppliersByCompany(String companyName) {
        return supplierRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }

    @Transactional(readOnly = true)
    public Optional<Supplier> findSupplierByEmail(String email) {
        return supplierRepository.findByEmail(email);
    }
}
