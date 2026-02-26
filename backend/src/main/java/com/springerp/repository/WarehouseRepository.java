package com.springerp.repository;

import com.springerp.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByWarehouseCode(String warehouseCode);

    Optional<Warehouse> findByWarehouseName(String warehouseName);

    Page<Warehouse> findByIsActive(Boolean isActive, Pageable pageable);

    Page<Warehouse> findByWarehouseType(Warehouse.WarehouseType warehouseType, Pageable pageable);

    List<Warehouse> findByManagerId(Long managerId);

    Page<Warehouse> findByTenantId(Long tenantId, Pageable pageable);

    long countByIsActive(Boolean isActive);
}

