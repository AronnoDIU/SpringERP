import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import {
  useInventoryItems,
  useLowStockItems,
  useCreateInventoryItem,
  useUpdateInventoryItem,
  useDeleteInventoryItem,
  useAdjustStock,
} from '../../hooks/useInventory';
import { Table } from '../../components/common/Table';
import { Button } from '../../components/common/Button';
import { Modal } from '../../components/common/Modal';
import { Input } from '../../components/common/Input';
import { Badge } from '../../components/common/Badge';
import { ConfirmDialog } from '../../components/common/ConfirmDialog';
import { StatCard } from '../../components/common/StatCard';
import type { InventoryItem } from '../../api/inventory.api';

const itemSchema = z.object({
  sku: z.string().min(1, 'SKU is required'),
  quantityOnHand: z.coerce.number().int().min(0, 'Quantity must be non-negative'),
  quantityReserved: z.coerce.number().int().min(0).optional(),
  reorderLevel: z.coerce.number().int().min(0, 'Reorder level must be non-negative'),
  reorderQuantity: z.coerce.number().int().min(0).optional(),
  unitCost: z.coerce.number().min(0).optional(),
  location: z.string().optional(),
});

const adjustSchema = z.object({
  quantityChange: z.coerce.number().int(),
  reason: z.string().min(1, 'Reason is required'),
});

type ItemFormData = z.infer<typeof itemSchema>;
type AdjustFormData = z.infer<typeof adjustSchema>;

const PAGE_SIZE = 20;

export const InventoryPage: React.FC = () => {
  const [page, setPage] = useState(0);
  const { data: itemPage, isLoading } = useInventoryItems(page, PAGE_SIZE);
  const { data: lowStockItems } = useLowStockItems();
  const { mutate: createItem, isLoading: creating } = useCreateInventoryItem();
  const { mutate: updateItem, isLoading: updating } = useUpdateInventoryItem();
  const { mutate: deleteItem } = useDeleteInventoryItem();
  const { mutate: adjustStock, isLoading: adjusting } = useAdjustStock();

  const [modalOpen, setModalOpen] = useState(false);
  const [adjustModalOpen, setAdjustModalOpen] = useState(false);
  const [editTarget, setEditTarget] = useState<InventoryItem | null>(null);
  const [adjustTarget, setAdjustTarget] = useState<InventoryItem | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<InventoryItem | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<ItemFormData>({ resolver: zodResolver(itemSchema) });

  const {
    register: registerAdjust,
    handleSubmit: handleAdjustSubmit,
    reset: resetAdjust,
    formState: { errors: adjustErrors },
  } = useForm<AdjustFormData>({ resolver: zodResolver(adjustSchema) });

  const openCreate = () => {
    setEditTarget(null);
    reset({ quantityOnHand: 0, quantityReserved: 0, reorderLevel: 10, reorderQuantity: 50 });
    setModalOpen(true);
  };
  const openEdit = (item: InventoryItem) => {
    setEditTarget(item);
    reset(item);
    setModalOpen(true);
  };
  const closeModal = () => {
    setModalOpen(false);
    setEditTarget(null);
    reset({});
  };
  const openAdjust = (item: InventoryItem) => {
    setAdjustTarget(item);
    resetAdjust({ quantityChange: 0, reason: '' });
    setAdjustModalOpen(true);
  };
  const closeAdjustModal = () => {
    setAdjustModalOpen(false);
    setAdjustTarget(null);
    resetAdjust({});
  };

  const onSubmit = (data: ItemFormData) => {
    const quantityAvailable = data.quantityOnHand - (data.quantityReserved ?? 0);
    if (editTarget) {
      updateItem({ id: editTarget.id, data: { ...data, quantityAvailable } }, { onSuccess: closeModal });
    } else {
      createItem({ ...data, quantityAvailable }, { onSuccess: closeModal });
    }
  };

  const onAdjustSubmit = (data: AdjustFormData) => {
    if (!adjustTarget) return;
    adjustStock(
      { inventoryItemId: adjustTarget.id, quantityChange: data.quantityChange, reason: data.reason },
      { onSuccess: closeAdjustModal }
    );
  };

  const items = itemPage?.content ?? [];
  const totalPages = itemPage?.totalPages ?? 1;

  const stockBadge = (item: InventoryItem) => {
    if (item.quantityAvailable <= 0) return <Badge variant="danger">Out of Stock</Badge>;
    if (item.quantityAvailable <= item.reorderLevel) return <Badge variant="warning">Low Stock</Badge>;
    return <Badge variant="success">In Stock</Badge>;
  };

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-lg font-semibold text-gray-800">Inventory Management</h2>
          <p className="text-sm text-gray-500">{itemPage?.totalElements ?? 0} total items</p>
        </div>
        <Button
          onClick={openCreate}
          leftIcon={
            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
          }
        >
          Add Item
        </Button>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <StatCard
          title="Total Items"
          value={itemPage?.totalElements ?? 0}
          icon={
            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8" />
            </svg>
          }
          color="blue"
        />
        <StatCard
          title="Low Stock Alerts"
          value={lowStockItems?.length ?? 0}
          icon={
            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
          }
          color="yellow"
        />
        <StatCard
          title="Total Stock Value"
          value={`$${items.reduce((sum, i) => sum + (i.quantityOnHand * (i.unitCost ?? 0)), 0).toLocaleString('en-US', { minimumFractionDigits: 2 })}`}
          icon={
            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
            </svg>
          }
          color="green"
        />
      </div>

      {/* Table */}
      <Table<InventoryItem>
        isLoading={isLoading}
        data={items}
        keyExtractor={(i) => i.id}
        columns={[
          {
            key: 'sku',
            header: 'SKU',
            render: (i) => <span className="font-mono font-medium text-blue-700">{i.sku}</span>,
          },
          { key: 'location', header: 'Location', render: (i) => i.location ?? '—' },
          {
            key: 'quantityOnHand',
            header: 'On Hand',
            render: (i) => <span className="font-medium">{i.quantityOnHand.toLocaleString()}</span>,
          },
          { key: 'quantityReserved', header: 'Reserved' },
          {
            key: 'quantityAvailable',
            header: 'Available',
            render: (i) => <span className="font-medium text-gray-800">{i.quantityAvailable.toLocaleString()}</span>,
          },
          {
            key: 'reorderLevel',
            header: 'Reorder Level',
            render: (i) => i.reorderLevel.toLocaleString(),
          },
          {
            key: 'unitCost',
            header: 'Unit Cost',
            render: (i) =>
              i.unitCost != null ? `$${i.unitCost.toFixed(2)}` : '—',
          },
          {
            key: 'status',
            header: 'Status',
            render: (i) => stockBadge(i),
          },
          {
            key: 'actions',
            header: 'Actions',
            render: (i) => (
              <div className="flex gap-2">
                <Button size="sm" variant="secondary" onClick={() => openEdit(i)}>Edit</Button>
                <Button size="sm" variant="ghost" onClick={() => openAdjust(i)}>Adjust</Button>
                <Button size="sm" variant="danger" onClick={() => setDeleteTarget(i)}>Delete</Button>
              </div>
            ),
          },
        ]}
      />

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="flex justify-end gap-2">
          <Button size="sm" variant="secondary" disabled={page === 0} onClick={() => setPage((p) => p - 1)}>
            Previous
          </Button>
          <span className="text-sm text-gray-600 self-center">
            Page {page + 1} of {totalPages}
          </span>
          <Button size="sm" variant="secondary" disabled={page >= totalPages - 1} onClick={() => setPage((p) => p + 1)}>
            Next
          </Button>
        </div>
      )}

      {/* Create / Edit Modal */}
      <Modal
        isOpen={modalOpen}
        onClose={closeModal}
        title={editTarget ? 'Edit Inventory Item' : 'Add Inventory Item'}
        footer={
          <div className="flex justify-end gap-3">
            <Button variant="secondary" onClick={closeModal}>Cancel</Button>
            <Button form="item-form" type="submit" isLoading={creating || updating}>
              {editTarget ? 'Update' : 'Create'}
            </Button>
          </div>
        }
      >
        <form id="item-form" onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <Input label="SKU" placeholder="e.g. SKU-001" error={errors.sku?.message} required {...register('sku')} />
          <div className="grid grid-cols-2 gap-4">
            <Input label="Quantity On Hand" type="number" error={errors.quantityOnHand?.message} required {...register('quantityOnHand')} />
            <Input label="Quantity Reserved" type="number" {...register('quantityReserved')} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <Input label="Reorder Level" type="number" error={errors.reorderLevel?.message} required {...register('reorderLevel')} />
            <Input label="Reorder Quantity" type="number" {...register('reorderQuantity')} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <Input label="Unit Cost ($)" type="number" step="0.01" {...register('unitCost')} />
            <Input label="Location" placeholder="e.g. Aisle 3" {...register('location')} />
          </div>
        </form>
      </Modal>

      {/* Stock Adjustment Modal */}
      <Modal
        isOpen={adjustModalOpen}
        onClose={closeAdjustModal}
        title={`Adjust Stock — ${adjustTarget?.sku ?? ''}`}
        footer={
          <div className="flex justify-end gap-3">
            <Button variant="secondary" onClick={closeAdjustModal}>Cancel</Button>
            <Button form="adjust-form" type="submit" isLoading={adjusting}>Apply Adjustment</Button>
          </div>
        }
      >
        <form id="adjust-form" onSubmit={handleAdjustSubmit(onAdjustSubmit)} className="space-y-4">
          <p className="text-sm text-gray-500">
            Current quantity on hand: <strong>{adjustTarget?.quantityOnHand ?? 0}</strong>
          </p>
          <Input
            label="Quantity Change (use negative to reduce)"
            type="number"
            placeholder="e.g. 10 or -5"
            error={adjustErrors.quantityChange?.message}
            required
            {...registerAdjust('quantityChange')}
          />
          <Input
            label="Reason"
            placeholder="e.g. Physical count correction"
            error={adjustErrors.reason?.message}
            required
            {...registerAdjust('reason')}
          />
        </form>
      </Modal>

      {/* Delete Confirmation */}
      <ConfirmDialog
        isOpen={deleteTarget !== null}
        title="Delete Inventory Item"
        message={`Are you sure you want to delete item "${deleteTarget?.sku}"? This action cannot be undone.`}
        confirmLabel="Delete"
        variant="danger"
        onConfirm={() => {
          if (deleteTarget) {
            deleteItem(deleteTarget.id, { onSuccess: () => setDeleteTarget(null) });
          }
        }}
        onCancel={() => setDeleteTarget(null)}
      />
    </div>
  );
};

