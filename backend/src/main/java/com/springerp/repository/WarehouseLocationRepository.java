package com.springerp.repository;

import com.springerp.entity.WarehouseLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseLocationRepository extends JpaRepository<WarehouseLocation, Long> {

    Optional<WarehouseLocation> findByLocationCode(String locationCode);

    Page<WarehouseLocation> findByWarehouseId(Long warehouseId, Pageable pageable);

    Page<WarehouseLocation> findByLocationType(WarehouseLocation.LocationType locationType, Pageable pageable);

    Page<WarehouseLocation> findByIsAvailable(Boolean isAvailable, Pageable pageable);

    List<WarehouseLocation> findByWarehouseIdAndIsAvailable(Long warehouseId, Boolean isAvailable);

    List<WarehouseLocation> findByWarehouseIdAndAisle(Long warehouseId, String aisle);

    Page<WarehouseLocation> findByTenantId(Long tenantId, Pageable pageable);

    long countByWarehouseIdAndIsAvailable(Long warehouseId, Boolean isAvailable);
}

