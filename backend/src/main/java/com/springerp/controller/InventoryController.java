package com.springerp.controller;
import com.springerp.entity.InventoryItem;
import com.springerp.entity.StockMovement;
import com.springerp.entity.Warehouse;
import com.springerp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {
    private final InventoryService inventoryService;
    // ─── Inventory Items ────────────────────────────────────────────────────
    @GetMapping("/items")
    public ResponseEntity<Page<InventoryItem>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(inventoryService.getAllInventoryItems(PageRequest.of(page, size)));
    }
    @GetMapping("/items/{id}")
    public ResponseEntity<InventoryItem> getItemById(@PathVariable Long id) {
        return inventoryService.getInventoryItem(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/items/sku/{sku}")
    public ResponseEntity<InventoryItem> getItemBySku(@PathVariable String sku) {
        return inventoryService.getInventoryItemBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/items/low-stock")
    public ResponseEntity<List<InventoryItem>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }
    @GetMapping("/items/critical-stock")
    public ResponseEntity<List<InventoryItem>> getCriticalStockItems() {
        return ResponseEntity.ok(inventoryService.getCriticalStockItems());
    }
    @PostMapping("/items")
    public ResponseEntity<InventoryItem> createItem(@RequestBody InventoryItem item) {
        return new ResponseEntity<>(inventoryService.createInventoryItem(item), HttpStatus.CREATED);
    }
    @PutMapping("/items/{id}")
    public ResponseEntity<InventoryItem> updateItem(@PathVariable Long id, @RequestBody InventoryItem item) {
        return ResponseEntity.ok(inventoryService.updateInventoryItem(id, item));
    }
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        inventoryService.deleteInventoryItem(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/items/{id}/available")
    public ResponseEntity<Integer> getAvailableStock(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getAvailableStock(id));
    }
    @PostMapping("/items/{id}/adjust")
    public ResponseEntity<StockMovement> adjustStock(
            @PathVariable Long id,
            @RequestParam int quantityChange,
            @RequestParam String reason) {
        return ResponseEntity.ok(inventoryService.adjustStock(id, quantityChange, reason));
    }
    // ─── Stock Movements ────────────────────────────────────────────────────
    @GetMapping("/movements")
    public ResponseEntity<Page<StockMovement>> getAllMovements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(inventoryService.getStockMovements(PageRequest.of(page, size)));
    }
    @PostMapping("/movements")
    public ResponseEntity<StockMovement> recordMovement(@RequestBody StockMovement movement) {
        return new ResponseEntity<>(inventoryService.recordStockMovement(movement), HttpStatus.CREATED);
    }
    @PutMapping("/movements/{id}/approve")
    public ResponseEntity<StockMovement> approveMovement(
            @PathVariable Long id,
            @RequestParam Long approverId) {
        return ResponseEntity.ok(inventoryService.approveStockMovement(id, approverId));
    }
    @PutMapping("/movements/{id}/reject")
    public ResponseEntity<StockMovement> rejectMovement(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(inventoryService.rejectStockMovement(id, reason));
    }
    // ─── Warehouses ──────────────────────────────────────────────────────────
    @GetMapping("/warehouses")
    public ResponseEntity<Page<Warehouse>> getAllWarehouses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(inventoryService.getAllWarehouses(PageRequest.of(page, size)));
    }
    @PostMapping("/warehouses")
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody Warehouse warehouse) {
        return new ResponseEntity<>(inventoryService.createWarehouse(warehouse), HttpStatus.CREATED);
    }
}
