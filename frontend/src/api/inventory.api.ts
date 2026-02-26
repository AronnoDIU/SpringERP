import apiClient from './client';
import type { PageResponse } from '../types';

export interface InventoryItem {
  id: number;
  sku: string;
  productId?: number;
  productName?: string;
  warehouseId?: number;
  warehouseName?: string;
  quantityOnHand: number;
  quantityReserved: number;
  quantityAvailable: number;
  reorderLevel: number;
  reorderQuantity: number;
  unitCost?: number;
  location?: string;
  isActive?: boolean;
  lastMovementDate?: string;
}

export interface StockMovement {
  id: number;
  inventoryItemId: number;
  sku?: string;
  movementType: 'PURCHASE' | 'SALE' | 'ADJUSTMENT' | 'TRANSFER' | 'RETURN' | 'DAMAGE';
  quantityMoved: number;
  reason?: string;
  notes?: string;
  status: 'PENDING' | 'APPROVED' | 'POSTED' | 'REJECTED';
  movementDate?: string;
  approvedBy?: number;
}

export interface Warehouse {
  id: number;
  warehouseCode: string;
  warehouseName: string;
  address?: string;
  city?: string;
  isActive?: boolean;
}

export const inventoryApi = {
  // Inventory Items
  getItems: (page = 0, size = 20) =>
    apiClient.get<PageResponse<InventoryItem>>('/inventory/items', { params: { page, size } }).then((r) => r.data),

  getItemById: (id: number) =>
    apiClient.get<InventoryItem>(`/inventory/items/${id}`).then((r) => r.data),

  getItemBySku: (sku: string) =>
    apiClient.get<InventoryItem>(`/inventory/items/sku/${sku}`).then((r) => r.data),

  getLowStockItems: () =>
    apiClient.get<InventoryItem[]>('/inventory/items/low-stock').then((r) => r.data),

  getCriticalStockItems: () =>
    apiClient.get<InventoryItem[]>('/inventory/items/critical-stock').then((r) => r.data),

  createItem: (data: Partial<InventoryItem>) =>
    apiClient.post<InventoryItem>('/inventory/items', data).then((r) => r.data),

  updateItem: (id: number, data: Partial<InventoryItem>) =>
    apiClient.put<InventoryItem>(`/inventory/items/${id}`, data).then((r) => r.data),

  deleteItem: (id: number) =>
    apiClient.delete(`/inventory/items/${id}`).then((r) => r.data),

  // Stock Movements
  getMovements: (page = 0, size = 20) =>
    apiClient.get<PageResponse<StockMovement>>('/inventory/movements', { params: { page, size } }).then((r) => r.data),

  recordMovement: (data: Partial<StockMovement>) =>
    apiClient.post<StockMovement>('/inventory/movements', data).then((r) => r.data),

  approveMovement: (id: number, approverId: number) =>
    apiClient.put<StockMovement>(`/inventory/movements/${id}/approve`, null, { params: { approverId } }).then((r) => r.data),

  rejectMovement: (id: number, reason: string) =>
    apiClient.put<StockMovement>(`/inventory/movements/${id}/reject`, null, { params: { reason } }).then((r) => r.data),

  adjustStock: (inventoryItemId: number, quantityChange: number, reason: string) =>
    apiClient.post<StockMovement>(`/inventory/items/${inventoryItemId}/adjust`, null, {
      params: { quantityChange, reason },
    }).then((r) => r.data),

  // Warehouses
  getWarehouses: (page = 0, size = 20) =>
    apiClient.get<PageResponse<Warehouse>>('/inventory/warehouses', { params: { page, size } }).then((r) => r.data),

  createWarehouse: (data: Partial<Warehouse>) =>
    apiClient.post<Warehouse>('/inventory/warehouses', data).then((r) => r.data),

  // Stock Levels
  getAvailableStock: (inventoryItemId: number) =>
    apiClient.get<number>(`/inventory/items/${inventoryItemId}/available`).then((r) => r.data),
};

