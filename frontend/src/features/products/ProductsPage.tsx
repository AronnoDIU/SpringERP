import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import {
  useProducts,
  useCreateProduct,
  useUpdateProduct,
  useDeleteProduct,
} from '../../hooks/useProducts';
import { Table } from '../../components/common/Table';
import { Button } from '../../components/common/Button';
import { Modal } from '../../components/common/Modal';
import { Input } from '../../components/common/Input';
import { ConfirmDialog } from '../../components/common/ConfirmDialog';
import type { Product, CreateProductRequest } from '../../types';

const productSchema = z.object({
  name: z.string().min(1, 'Name is required'),
  sku: z.string().optional(),
  description: z.string().optional(),
  price: z.coerce.number().min(0, 'Price must be non-negative'),
  costPrice: z.coerce.number().min(0).optional(),
  stockQuantity: z.coerce.number().int().min(0).optional(),
  unit: z.string().optional(),
  taxRate: z.coerce.number().min(0).max(100).optional(),
});

type ProductFormData = z.infer<typeof productSchema>;

export const ProductsPage: React.FC = () => {
  const { data: products, isLoading } = useProducts();
  const { mutate: createProduct, isLoading: creating } = useCreateProduct();
  const { mutate: updateProduct, isLoading: updating } = useUpdateProduct();
  const { mutate: deleteProduct } = useDeleteProduct();

  const [modalOpen, setModalOpen] = useState(false);
  const [editTarget, setEditTarget] = useState<Product | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<Product | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<ProductFormData>({ resolver: zodResolver(productSchema) });

  const openCreate = () => { setEditTarget(null); reset({}); setModalOpen(true); };
  const openEdit = (p: Product) => { setEditTarget(p); reset(p); setModalOpen(true); };
  const closeModal = () => { setModalOpen(false); setEditTarget(null); reset({}); };

  const onSubmit = (data: ProductFormData) => {
    if (editTarget) {
      updateProduct({ id: editTarget.id, data }, { onSuccess: closeModal });
    } else {
      createProduct(data as CreateProductRequest, { onSuccess: closeModal });
    }
  };

  return (
    <div className="space-y-5">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-lg font-semibold text-gray-800">Products</h2>
          <p className="text-sm text-gray-500">{products?.length ?? 0} total records</p>
        </div>
        <Button onClick={openCreate} leftIcon={<svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" /></svg>}>
          Add Product
        </Button>
      </div>

      <Table<Product>
        isLoading={isLoading}
        data={products ?? []}
        keyExtractor={(p) => p.id}
        columns={[
          { key: 'name', header: 'Name', render: (p) => <span className="font-medium text-gray-800">{p.name}</span> },
          { key: 'sku', header: 'SKU' },
          {
            key: 'price', header: 'Price',
            render: (p) => <span className="font-semibold text-gray-700">${p.price.toLocaleString()}</span>,
          },
          {
            key: 'stockQuantity', header: 'Stock',
            render: (p) => (
              <span className={`font-medium ${(p.stockQuantity ?? 0) < 10 ? 'text-red-600' : 'text-green-600'}`}>
                {p.stockQuantity ?? 0} {p.unit}
              </span>
            ),
          },
          { key: 'categoryName', header: 'Category' },
          {
            key: 'isActive', header: 'Status',
            render: (p) => (
              <span className={`px-2 py-0.5 rounded-full text-xs font-medium ${p.isActive ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-500'}`}>
                {p.isActive ? 'Active' : 'Inactive'}
              </span>
            ),
          },
          {
            key: 'actions', header: 'Actions',
            render: (p) => (
              <div className="flex gap-2">
                <Button size="sm" variant="ghost" onClick={() => openEdit(p)}>Edit</Button>
                <Button size="sm" variant="danger" onClick={() => setDeleteTarget(p)}>Delete</Button>
              </div>
            ),
          },
        ]}
      />

      <Modal isOpen={modalOpen} onClose={closeModal} title={editTarget ? 'Edit Product' : 'Add Product'} size="lg">
        <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Input label="Product Name" required error={errors.name?.message} {...register('name')} />
          <Input label="SKU" {...register('sku')} />
          <Input label="Price" type="number" step="0.01" required error={errors.price?.message} {...register('price')} />
          <Input label="Cost Price" type="number" step="0.01" {...register('costPrice')} />
          <Input label="Stock Quantity" type="number" {...register('stockQuantity')} />
          <Input label="Unit" placeholder="pcs, kg, L..." {...register('unit')} />
          <Input label="Tax Rate (%)" type="number" step="0.01" {...register('taxRate')} />
          <div className="sm:col-span-2">
            <label className="text-sm font-medium text-gray-700">Description</label>
            <textarea
              {...register('description')}
              className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              rows={3}
            />
          </div>
          <div className="sm:col-span-2 flex justify-end gap-3 mt-2">
            <Button type="button" variant="secondary" onClick={closeModal}>Cancel</Button>
            <Button type="submit" isLoading={creating || updating}>{editTarget ? 'Update' : 'Create'}</Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        isOpen={!!deleteTarget}
        title="Delete Product"
        message={`Are you sure you want to delete "${deleteTarget?.name}"?`}
        confirmLabel="Delete"
        onConfirm={() => { deleteProduct(deleteTarget!.id); setDeleteTarget(null); }}
        onCancel={() => setDeleteTarget(null)}
        isDangerous
      />
    </div>
  );
};

