package com.springerp.repository;

import com.springerp.entity.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findBySku(String sku);

    Page<InventoryItem> findByProductId(Long productId, Pageable pageable);

    Page<InventoryItem> findByWarehouseId(Long warehouseId, Pageable pageable);

    List<InventoryItem> findByQuantityAvailableLessThanEqualOrderByQuantityAvailable(Integer reorderLevel);

    Page<InventoryItem> findByIsActive(Boolean isActive, Pageable pageable);

    @Query("SELECT ii FROM InventoryItem ii WHERE ii.quantityOnHand <= ii.reorderLevel")
    List<InventoryItem> findLowStockItems();

    Page<InventoryItem> findByTenantId(Long tenantId, Pageable pageable);

    long countByQuantityAvailableLessThanEqual(Integer reorderLevel);
}

