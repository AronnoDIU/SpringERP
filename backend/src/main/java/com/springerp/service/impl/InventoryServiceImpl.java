package com.springerp.service.impl;

import com.springerp.entity.InventoryItem;
import com.springerp.entity.StockMovement;
import com.springerp.entity.Warehouse;
import com.springerp.repository.InventoryItemRepository;
import com.springerp.repository.StockMovementRepository;
import com.springerp.repository.WarehouseRepository;
import com.springerp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for inventory management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final StockMovementRepository stockMovementRepository;
    private final WarehouseRepository warehouseRepository;

    // Inventory Item Management
    @Override
    public InventoryItem createInventoryItem(InventoryItem item) {
        log.info("Creating inventory item: {}", item.getSku());
        item.setIsActive(true);
        item.setQuantityAvailable(item.getQuantityOnHand() - item.getQuantityReserved());
        return inventoryItemRepository.save(item);
    }

    @Override
    public InventoryItem updateInventoryItem(Long id, InventoryItem item) {
        log.info("Updating inventory item: {}", id);
        return inventoryItemRepository.findById(id)
                .map(existing -> {
                    existing.setReorderLevel(item.getReorderLevel());
                    existing.setReorderQuantity(item.getReorderQuantity());
                    existing.setUnitCost(item.getUnitCost());
                    existing.setLocation(item.getLocation());
                    return inventoryItemRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Inventory item not found: " + id));
    }

    @Override
    public Optional<InventoryItem> getInventoryItem(Long id) {
        return inventoryItemRepository.findById(id);
    }

    @Override
    public Optional<InventoryItem> getInventoryItemBySku(String sku) {
        return inventoryItemRepository.findBySku(sku);
    }

    @Override
    public Page<InventoryItem> getAllInventoryItems(Pageable pageable) {
        return inventoryItemRepository.findAll(pageable);
    }

    @Override
    public Page<InventoryItem> getInventoryItemsByWarehouse(Long warehouseId, Pageable pageable) {
        return inventoryItemRepository.findByWarehouseId(warehouseId, pageable);
    }

    @Override
    public List<InventoryItem> getLowStockItems() {
        log.info("Retrieving low stock items");
        return inventoryItemRepository.findLowStockItems();
    }

    @Override
    public List<InventoryItem> getCriticalStockItems() {
        log.info("Retrieving critical stock items");
        return inventoryItemRepository.findByQuantityAvailableLessThanEqualOrderByQuantityAvailable(5);
    }

    // Stock Movement
    @Override
    public StockMovement recordStockMovement(StockMovement movement) {
        log.info("Recording stock movement for inventory item: {}", movement.getInventoryItem().getId());
        movement.setStatus(StockMovement.Status.PENDING);
        movement.setMovementDate(java.time.LocalDate.now());
        return stockMovementRepository.save(movement);
    }

    @Override
    public StockMovement approveStockMovement(Long movementId, Long approverId) {
        log.info("Approving stock movement: {}", movementId);
        return stockMovementRepository.findById(movementId)
                .map(movement -> {
                    movement.setStatus(StockMovement.Status.POSTED);
                    movement.setApprovedBy(approverId);
                    movement.setApprovalDate(java.time.LocalDate.now());

                    // Update inventory
                    InventoryItem item = movement.getInventoryItem();
                    if (movement.getMovementType() == StockMovement.MovementType.PURCHASE) {
                        item.setQuantityOnHand(item.getQuantityOnHand() + movement.getQuantityMoved());
                    } else if (movement.getMovementType() == StockMovement.MovementType.SALE) {
                        item.setQuantityOnHand(item.getQuantityOnHand() - movement.getQuantityMoved());
                    }
                    item.setLastMovementDate(LocalDateTime.now());
                    item.setQuantityAvailable(item.getQuantityOnHand() - item.getQuantityReserved());
                    inventoryItemRepository.save(item);

                    return stockMovementRepository.save(movement);
                })
                .orElseThrow(() -> new RuntimeException("Stock movement not found: " + movementId));
    }

    @Override
    public StockMovement rejectStockMovement(Long movementId, String reason) {
        log.info("Rejecting stock movement: {}", movementId);
        return stockMovementRepository.findById(movementId)
                .map(movement -> {
                    movement.setStatus(StockMovement.Status.REJECTED);
                    movement.setReason(reason);
                    return stockMovementRepository.save(movement);
                })
                .orElseThrow(() -> new RuntimeException("Stock movement not found: " + movementId));
    }

    @Override
    public Page<StockMovement> getStockMovements(Pageable pageable) {
        return stockMovementRepository.findAll(pageable);
    }

    @Override
    public List<StockMovement> getMovementsByProduct(Long productId) {
        return List.of(); // Implementation required
    }

    @Override
    public List<StockMovement> getMovementsByInventoryItem(Long inventoryItemId) {
        return stockMovementRepository.findByInventoryItemId(inventoryItemId, Pageable.unpaged()).getContent();
    }

    // Stock Adjustment
    @Override
    public StockMovement adjustStock(Long inventoryItemId, Integer quantityChange, String reason) {
        log.info("Adjusting stock for inventory item: {}", inventoryItemId);
        InventoryItem item = inventoryItemRepository.findById(inventoryItemId)
                .orElseThrow(() -> new RuntimeException("Inventory item not found: " + inventoryItemId));

        StockMovement movement = StockMovement.builder()
                .inventoryItem(item)
                .movementType(StockMovement.MovementType.ADJUSTMENT)
                .quantityMoved(Math.abs(quantityChange))
                .reason(reason)
                .status(StockMovement.Status.PENDING)
                .build();

        return recordStockMovement(movement);
    }

    @Override
    public StockMovement transferStock(Long fromInventoryItemId, Long toInventoryItemId, Integer quantity, String reason) {
        log.info("Transferring stock from {} to {}", fromInventoryItemId, toInventoryItemId);
        InventoryItem fromItem = inventoryItemRepository.findById(fromInventoryItemId)
                .orElseThrow(() -> new RuntimeException("From inventory item not found: " + fromInventoryItemId));

        StockMovement movement = StockMovement.builder()
                .inventoryItem(fromItem)
                .movementType(StockMovement.MovementType.TRANSFER)
                .quantityMoved(quantity)
                .reason(reason)
                .status(StockMovement.Status.PENDING)
                .build();

        return recordStockMovement(movement);
    }

    // Stock Levels
    @Override
    public Integer getAvailableStock(Long inventoryItemId) {
        return inventoryItemRepository.findById(inventoryItemId)
                .map(InventoryItem::getQuantityAvailable)
                .orElse(0);
    }

    @Override
    public Integer getReservedStock(Long inventoryItemId) {
        return inventoryItemRepository.findById(inventoryItemId)
                .map(InventoryItem::getQuantityReserved)
                .orElse(0);
    }

    @Override
    public void updateAvailableStock(Long inventoryItemId) {
        inventoryItemRepository.findById(inventoryItemId)
                .ifPresent(item -> {
                    item.setQuantityAvailable(item.getQuantityOnHand() - item.getQuantityReserved());
                    inventoryItemRepository.save(item);
                });
    }

    // Warehouse Management
    @Override
    public Warehouse createWarehouse(Warehouse warehouse) {
        log.info("Creating warehouse: {}", warehouse.getWarehouseCode());
        warehouse.setIsActive(true);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse updateWarehouse(Long id, Warehouse warehouse) {
        log.info("Updating warehouse: {}", id);
        return warehouseRepository.findById(id)
                .map(existing -> {
                    existing.setWarehouseName(warehouse.getWarehouseName());
                    existing.setAddress(warehouse.getAddress());
                    existing.setCity(warehouse.getCity());
                    return warehouseRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Warehouse not found: " + id));
    }

    @Override
    public Optional<Warehouse> getWarehouse(Long id) {
        return warehouseRepository.findById(id);
    }

    @Override
    public Page<Warehouse> getAllWarehouses(Pageable pageable) {
        return warehouseRepository.findAll(pageable);
    }

    // Stock Counting (Physical Inventory)
    @Override
    public StockMovement recordPhysicalCount(Long inventoryItemId, Integer countedQuantity, String notes) {
        log.info("Recording physical count for inventory item: {}", inventoryItemId);
        InventoryItem item = inventoryItemRepository.findById(inventoryItemId)
                .orElseThrow(() -> new RuntimeException("Inventory item not found: " + inventoryItemId));

        int variance = countedQuantity - item.getQuantityOnHand();

        StockMovement movement = StockMovement.builder()
                .inventoryItem(item)
                .movementType(StockMovement.MovementType.ADJUSTMENT)
                .quantityMoved(Math.abs(variance))
                .reason("Physical count variance: " + variance)
                .notes(notes)
                .status(StockMovement.Status.PENDING)
                .build();

        item.setLastCountedAt(LocalDateTime.now());
        inventoryItemRepository.save(item);

        return recordStockMovement(movement);
    }

    @Override
    public void deleteInventoryItem(Long id) {
        log.info("Deleting inventory item: {}", id);
        inventoryItemRepository.deleteById(id);
    }
}

