package com.springerp.service;

import com.springerp.entity.InventoryItem;
import com.springerp.entity.StockMovement;
import com.springerp.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for inventory management.
 */
public interface InventoryService {

    // Inventory Item Management
    InventoryItem createInventoryItem(InventoryItem item);
    InventoryItem updateInventoryItem(Long id, InventoryItem item);
    Optional<InventoryItem> getInventoryItem(Long id);
    Optional<InventoryItem> getInventoryItemBySku(String sku);
    Page<InventoryItem> getAllInventoryItems(Pageable pageable);
    Page<InventoryItem> getInventoryItemsByWarehouse(Long warehouseId, Pageable pageable);
    List<InventoryItem> getLowStockItems();
    List<InventoryItem> getCriticalStockItems();

    // Stock Movement
    StockMovement recordStockMovement(StockMovement movement);
    StockMovement approveStockMovement(Long movementId, Long approverId);
    StockMovement rejectStockMovement(Long movementId, String reason);
    Page<StockMovement> getStockMovements(Pageable pageable);
    List<StockMovement> getMovementsByProduct(Long productId);
    List<StockMovement> getMovementsByInventoryItem(Long inventoryItemId);

    // Stock Adjustment
    StockMovement adjustStock(Long inventoryItemId, Integer quantityChange, String reason);
    StockMovement transferStock(Long fromInventoryItemId, Long toInventoryItemId, Integer quantity, String reason);

    // Stock Levels
    Integer getAvailableStock(Long inventoryItemId);
    Integer getReservedStock(Long inventoryItemId);
    void updateAvailableStock(Long inventoryItemId);

    // Warehouse Management
    Warehouse createWarehouse(Warehouse warehouse);
    Warehouse updateWarehouse(Long id, Warehouse warehouse);
    Optional<Warehouse> getWarehouse(Long id);
    Page<Warehouse> getAllWarehouses(Pageable pageable);

    // Stock Counting (Physical Inventory)
    StockMovement recordPhysicalCount(Long inventoryItemId, Integer countedQuantity, String notes);

    void deleteInventoryItem(Long id);
}

