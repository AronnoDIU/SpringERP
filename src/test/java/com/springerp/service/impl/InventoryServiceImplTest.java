package com.springerp.service.impl;

import com.springerp.entity.InventoryItem;
import com.springerp.entity.StockMovement;
import com.springerp.entity.Warehouse;
import com.springerp.repository.InventoryItemRepository;
import com.springerp.repository.StockMovementRepository;
import com.springerp.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock private InventoryItemRepository inventoryItemRepository;
    @Mock private StockMovementRepository stockMovementRepository;
    @Mock private WarehouseRepository warehouseRepository;

    @InjectMocks
    private InventoryServiceImpl service;

    private InventoryItem inventoryItem;
    private Warehouse warehouse;
    private StockMovement stockMovement;

    @BeforeEach
    void setUp() {
        inventoryItem = new InventoryItem();
        inventoryItem.setId(1L);
        inventoryItem.setSku("SKU-001");
        inventoryItem.setQuantityOnHand(100);
        inventoryItem.setQuantityReserved(10);
        inventoryItem.setQuantityAvailable(90);
        inventoryItem.setReorderLevel(20);
        inventoryItem.setReorderQuantity(50);
        inventoryItem.setUnitCost(new BigDecimal("15.00"));
        inventoryItem.setIsActive(true);

        warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setWarehouseCode("WH-001");
        warehouse.setWarehouseName("Main Warehouse");
        warehouse.setAddress("100 Warehouse Ave");
        warehouse.setCity("Chicago");
        warehouse.setIsActive(true);

        stockMovement = new StockMovement();
        stockMovement.setId(1L);
        stockMovement.setInventoryItem(inventoryItem);
        stockMovement.setMovementType(StockMovement.MovementType.PURCHASE);
        stockMovement.setQuantityMoved(50);
        stockMovement.setStatus(StockMovement.Status.PENDING);
    }

    // ─── Inventory Item ───────────────────────────────────────────────────────

    @Test
    void createInventoryItem_setsIsActiveTrueAndComputesQuantityAvailable() {
        inventoryItem.setIsActive(false);
        inventoryItem.setQuantityAvailable(0);
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);

        service.createInventoryItem(inventoryItem);

        ArgumentCaptor<InventoryItem> captor = ArgumentCaptor.forClass(InventoryItem.class);
        verify(inventoryItemRepository).save(captor.capture());
        assertThat(captor.getValue().getIsActive()).isTrue();
        assertThat(captor.getValue().getQuantityAvailable())
                .isEqualTo(inventoryItem.getQuantityOnHand() - inventoryItem.getQuantityReserved());
    }

    @Test
    void createInventoryItem_savesAndReturnsItem() {
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);

        InventoryItem result = service.createInventoryItem(inventoryItem);

        assertThat(result).isEqualTo(inventoryItem);
    }

    @Test
    void updateInventoryItem_updatesReorderAndCostFields() {
        InventoryItem incoming = new InventoryItem();
        incoming.setReorderLevel(30);
        incoming.setReorderQuantity(75);
        incoming.setUnitCost(new BigDecimal("18.50"));
        incoming.setLocation("Aisle 3");

        when(inventoryItemRepository.findById(1L)).thenReturn(Optional.of(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenAnswer(inv -> inv.getArgument(0));

        InventoryItem result = service.updateInventoryItem(1L, incoming);

        assertThat(result.getReorderLevel()).isEqualTo(30);
        assertThat(result.getReorderQuantity()).isEqualTo(75);
        assertThat(result.getUnitCost()).isEqualByComparingTo("18.50");
        assertThat(result.getLocation()).isEqualTo("Aisle 3");
    }

    @Test
    void updateInventoryItem_withNonExistentId_throwsRuntimeException() {
        when(inventoryItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateInventoryItem(99L, inventoryItem))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getInventoryItem_returnsExistingItem() {
        when(inventoryItemRepository.findById(1L)).thenReturn(Optional.of(inventoryItem));

        Optional<InventoryItem> result = service.getInventoryItem(1L);

        assertThat(result).contains(inventoryItem);
    }

    @Test
    void getInventoryItem_withNonExistentId_returnsEmpty() {
        when(inventoryItemRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<InventoryItem> result = service.getInventoryItem(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void getInventoryItemBySku_delegatesToRepository() {
        when(inventoryItemRepository.findBySku("SKU-001")).thenReturn(Optional.of(inventoryItem));

        Optional<InventoryItem> result = service.getInventoryItemBySku("SKU-001");

        assertThat(result).contains(inventoryItem);
    }

    @Test
    void getLowStockItems_delegatesToRepository() {
        when(inventoryItemRepository.findLowStockItems()).thenReturn(List.of(inventoryItem));

        List<InventoryItem> result = service.getLowStockItems();

        assertThat(result).containsExactly(inventoryItem);
        verify(inventoryItemRepository).findLowStockItems();
    }

    @Test
    void getCriticalStockItems_delegatesToRepository() {
        when(inventoryItemRepository.findByQuantityAvailableLessThanEqualOrderByQuantityAvailable(5))
                .thenReturn(List.of(inventoryItem));

        List<InventoryItem> result = service.getCriticalStockItems();

        assertThat(result).containsExactly(inventoryItem);
    }

    @Test
    void deleteInventoryItem_delegatesToRepository() {
        service.deleteInventoryItem(1L);

        verify(inventoryItemRepository).deleteById(1L);
    }

    // ─── Stock Movement ───────────────────────────────────────────────────────

    @Test
    void recordStockMovement_setsStatusPending() {
        stockMovement.setStatus(null);
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(stockMovement);

        service.recordStockMovement(stockMovement);

        ArgumentCaptor<StockMovement> captor = ArgumentCaptor.forClass(StockMovement.class);
        verify(stockMovementRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(StockMovement.Status.PENDING);
    }

    @Test
    void approveStockMovement_purchase_increasesQuantityOnHand() {
        stockMovement.setMovementType(StockMovement.MovementType.PURCHASE);
        stockMovement.setQuantityMoved(50);

        when(stockMovementRepository.findById(1L)).thenReturn(Optional.of(stockMovement));
        when(stockMovementRepository.save(any(StockMovement.class))).thenAnswer(inv -> inv.getArgument(0));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenAnswer(inv -> inv.getArgument(0));

        service.approveStockMovement(1L, 10L);

        ArgumentCaptor<InventoryItem> captor = ArgumentCaptor.forClass(InventoryItem.class);
        verify(inventoryItemRepository).save(captor.capture());
        assertThat(captor.getValue().getQuantityOnHand()).isEqualTo(150); // 100 + 50
    }

    @Test
    void approveStockMovement_sale_decreasesQuantityOnHand() {
        stockMovement.setMovementType(StockMovement.MovementType.SALE);
        stockMovement.setQuantityMoved(30);

        when(stockMovementRepository.findById(1L)).thenReturn(Optional.of(stockMovement));
        when(stockMovementRepository.save(any(StockMovement.class))).thenAnswer(inv -> inv.getArgument(0));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenAnswer(inv -> inv.getArgument(0));

        service.approveStockMovement(1L, 10L);

        ArgumentCaptor<InventoryItem> captor = ArgumentCaptor.forClass(InventoryItem.class);
        verify(inventoryItemRepository).save(captor.capture());
        assertThat(captor.getValue().getQuantityOnHand()).isEqualTo(70); // 100 - 30
    }

    @Test
    void approveStockMovement_setsStatusPostedAndApprover() {
        when(stockMovementRepository.findById(1L)).thenReturn(Optional.of(stockMovement));
        when(stockMovementRepository.save(any(StockMovement.class))).thenAnswer(inv -> inv.getArgument(0));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);

        StockMovement result = service.approveStockMovement(1L, 42L);

        assertThat(result.getStatus()).isEqualTo(StockMovement.Status.POSTED);
        assertThat(result.getApprovedBy()).isEqualTo(42L);
    }

    @Test
    void approveStockMovement_withNonExistentId_throwsRuntimeException() {
        when(stockMovementRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.approveStockMovement(99L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void rejectStockMovement_setsStatusRejectedAndReason() {
        when(stockMovementRepository.findById(1L)).thenReturn(Optional.of(stockMovement));
        when(stockMovementRepository.save(any(StockMovement.class))).thenAnswer(inv -> inv.getArgument(0));

        StockMovement result = service.rejectStockMovement(1L, "Duplicate entry");

        assertThat(result.getStatus()).isEqualTo(StockMovement.Status.REJECTED);
        assertThat(result.getReason()).isEqualTo("Duplicate entry");
    }

    @Test
    void rejectStockMovement_withNonExistentId_throwsRuntimeException() {
        when(stockMovementRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.rejectStockMovement(99L, "Reason"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    // ─── Stock Level Queries ──────────────────────────────────────────────────

    @Test
    void getAvailableStock_returnsQuantityAvailable() {
        when(inventoryItemRepository.findById(1L)).thenReturn(Optional.of(inventoryItem));

        Integer result = service.getAvailableStock(1L);

        assertThat(result).isEqualTo(90);
    }

    @Test
    void getAvailableStock_returnsZeroForNonExistentItem() {
        when(inventoryItemRepository.findById(99L)).thenReturn(Optional.empty());

        Integer result = service.getAvailableStock(99L);

        assertThat(result).isZero();
    }

    @Test
    void getReservedStock_returnsQuantityReserved() {
        when(inventoryItemRepository.findById(1L)).thenReturn(Optional.of(inventoryItem));

        Integer result = service.getReservedStock(1L);

        assertThat(result).isEqualTo(10);
    }

    @Test
    void getReservedStock_returnsZeroForNonExistentItem() {
        when(inventoryItemRepository.findById(99L)).thenReturn(Optional.empty());

        Integer result = service.getReservedStock(99L);

        assertThat(result).isZero();
    }

    @Test
    void updateAvailableStock_recalculatesQuantityAvailable() {
        inventoryItem.setQuantityOnHand(120);
        inventoryItem.setQuantityReserved(20);

        when(inventoryItemRepository.findById(1L)).thenReturn(Optional.of(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenAnswer(inv -> inv.getArgument(0));

        service.updateAvailableStock(1L);

        ArgumentCaptor<InventoryItem> captor = ArgumentCaptor.forClass(InventoryItem.class);
        verify(inventoryItemRepository).save(captor.capture());
        assertThat(captor.getValue().getQuantityAvailable()).isEqualTo(100); // 120 - 20
    }

    // ─── Warehouse ────────────────────────────────────────────────────────────

    @Test
    void createWarehouse_setsIsActiveTrueAndSaves() {
        warehouse.setIsActive(false);
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        service.createWarehouse(warehouse);

        ArgumentCaptor<Warehouse> captor = ArgumentCaptor.forClass(Warehouse.class);
        verify(warehouseRepository).save(captor.capture());
        assertThat(captor.getValue().getIsActive()).isTrue();
    }

    @Test
    void updateWarehouse_updatesNameAddressAndCity() {
        Warehouse incoming = new Warehouse();
        incoming.setWarehouseName("Secondary Warehouse");
        incoming.setAddress("200 Storage Blvd");
        incoming.setCity("Dallas");

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(warehouseRepository.save(any(Warehouse.class))).thenAnswer(inv -> inv.getArgument(0));

        Warehouse result = service.updateWarehouse(1L, incoming);

        assertThat(result.getWarehouseName()).isEqualTo("Secondary Warehouse");
        assertThat(result.getAddress()).isEqualTo("200 Storage Blvd");
        assertThat(result.getCity()).isEqualTo("Dallas");
    }

    @Test
    void updateWarehouse_withNonExistentId_throwsRuntimeException() {
        when(warehouseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateWarehouse(99L, warehouse))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void adjustStock_createsAdjustmentMovementForItem() {
        when(inventoryItemRepository.findById(1L)).thenReturn(Optional.of(inventoryItem));
        when(stockMovementRepository.save(any(StockMovement.class))).thenAnswer(inv -> inv.getArgument(0));

        service.adjustStock(1L, -15, "Damaged goods");

        ArgumentCaptor<StockMovement> captor = ArgumentCaptor.forClass(StockMovement.class);
        verify(stockMovementRepository).save(captor.capture());
        assertThat(captor.getValue().getMovementType()).isEqualTo(StockMovement.MovementType.ADJUSTMENT);
        assertThat(captor.getValue().getQuantityMoved()).isEqualTo(15); // abs(-15)
        assertThat(captor.getValue().getReason()).isEqualTo("Damaged goods");
    }

    @Test
    void adjustStock_withNonExistentItem_throwsRuntimeException() {
        when(inventoryItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adjustStock(99L, 10, "Correction"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void transferStock_createsTransferMovementFromSourceItem() {
        when(inventoryItemRepository.findById(1L)).thenReturn(Optional.of(inventoryItem));
        when(stockMovementRepository.save(any(StockMovement.class))).thenAnswer(inv -> inv.getArgument(0));

        service.transferStock(1L, 2L, 25, "Warehouse consolidation");

        ArgumentCaptor<StockMovement> captor = ArgumentCaptor.forClass(StockMovement.class);
        verify(stockMovementRepository).save(captor.capture());
        assertThat(captor.getValue().getMovementType()).isEqualTo(StockMovement.MovementType.TRANSFER);
        assertThat(captor.getValue().getQuantityMoved()).isEqualTo(25);
    }
}

