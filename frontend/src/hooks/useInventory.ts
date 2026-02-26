import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'react-toastify';
import { inventoryApi, type InventoryItem, type StockMovement } from '../api/inventory.api';

const INVENTORY_KEY = 'inventory-items';
const MOVEMENTS_KEY = 'stock-movements';
const WAREHOUSES_KEY = 'warehouses';

export function useInventoryItems(page = 0, size = 20) {
  return useQuery({
    queryKey: [INVENTORY_KEY, page, size],
    queryFn: () => inventoryApi.getItems(page, size),
  });
}

export function useLowStockItems() {
  return useQuery({
    queryKey: [INVENTORY_KEY, 'low-stock'],
    queryFn: inventoryApi.getLowStockItems,
  });
}

export function useCriticalStockItems() {
  return useQuery({
    queryKey: [INVENTORY_KEY, 'critical-stock'],
    queryFn: inventoryApi.getCriticalStockItems,
  });
}

export function useCreateInventoryItem() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (data: Partial<InventoryItem>) => inventoryApi.createItem(data),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [INVENTORY_KEY] });
      toast.success('Inventory item created');
    },
    onError: () => toast.error('Failed to create inventory item'),
  });
}

export function useUpdateInventoryItem() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: Partial<InventoryItem> }) =>
      inventoryApi.updateItem(id, data),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [INVENTORY_KEY] });
      toast.success('Inventory item updated');
    },
    onError: () => toast.error('Failed to update inventory item'),
  });
}

export function useDeleteInventoryItem() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => inventoryApi.deleteItem(id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [INVENTORY_KEY] });
      toast.success('Inventory item deleted');
    },
    onError: () => toast.error('Failed to delete inventory item'),
  });
}

export function useAdjustStock() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ inventoryItemId, quantityChange, reason }: {
      inventoryItemId: number;
      quantityChange: number;
      reason: string;
    }) => inventoryApi.adjustStock(inventoryItemId, quantityChange, reason),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [INVENTORY_KEY] });
      qc.invalidateQueries({ queryKey: [MOVEMENTS_KEY] });
      toast.success('Stock adjusted successfully');
    },
    onError: () => toast.error('Failed to adjust stock'),
  });
}

export function useStockMovements(page = 0, size = 20) {
  return useQuery({
    queryKey: [MOVEMENTS_KEY, page, size],
    queryFn: () => inventoryApi.getMovements(page, size),
  });
}

export function useRecordStockMovement() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (data: Partial<StockMovement>) => inventoryApi.recordMovement(data),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [MOVEMENTS_KEY] });
      qc.invalidateQueries({ queryKey: [INVENTORY_KEY] });
      toast.success('Stock movement recorded');
    },
    onError: () => toast.error('Failed to record stock movement'),
  });
}

export function useApproveStockMovement() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, approverId }: { id: number; approverId: number }) =>
      inventoryApi.approveMovement(id, approverId),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [MOVEMENTS_KEY] });
      qc.invalidateQueries({ queryKey: [INVENTORY_KEY] });
      toast.success('Stock movement approved');
    },
    onError: () => toast.error('Failed to approve stock movement'),
  });
}

export function useWarehouses(page = 0, size = 20) {
  return useQuery({
    queryKey: [WAREHOUSES_KEY, page, size],
    queryFn: () => inventoryApi.getWarehouses(page, size),
  });
}

