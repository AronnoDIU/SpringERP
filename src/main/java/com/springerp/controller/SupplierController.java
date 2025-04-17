package com.springerp.controller;

import com.springerp.entity.Supplier;
import com.springerp.dto.SupplierDTO;
import com.springerp.service.SupplierService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/suppliers")
@Slf4j
public class SupplierController {
    private final SupplierService supplierService;
    private final ModelMapper modelMapper;
    
    public SupplierController(SupplierService supplierService, ModelMapper modelMapper) {
        this.supplierService = supplierService;
        this.modelMapper = modelMapper;
    }
    
    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        Supplier supplier = modelMapper.map(supplierDTO, Supplier.class);
        Supplier newSupplier = supplierService.createSupplier(supplier);
        SupplierDTO response = modelMapper.map(newSupplier, SupplierDTO.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Long id, 
                                                     @Valid @RequestBody SupplierDTO supplierDTO) {
        Supplier supplier = modelMapper.map(supplierDTO, Supplier.class);
        Supplier updatedSupplier = supplierService.updateSupplier(id, supplier);
        SupplierDTO response = modelMapper.map(updatedSupplier, SupplierDTO.class);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplier(@PathVariable Long id) {
        Supplier supplier = supplierService.getSupplier(id);
        SupplierDTO response = modelMapper.map(supplier, SupplierDTO.class);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<SupplierDTO> response = suppliers.stream()
            .map(supplier -> modelMapper.map(supplier, SupplierDTO.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<SupplierDTO>> searchSuppliers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String companyName) {
        List<Supplier> suppliers;
        if (name != null) {
            suppliers = supplierService.searchSuppliersByName(name);
        } else if (companyName != null) {
            suppliers = supplierService.searchSuppliersByCompany(companyName);
        } else {
            suppliers = supplierService.getAllSuppliers();
        }
        
        List<SupplierDTO> response = suppliers.stream()
            .map(supplier -> modelMapper.map(supplier, SupplierDTO.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}