import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import {
  useSuppliers,
  useCreateSupplier,
  useUpdateSupplier,
  useDeleteSupplier,
} from '../../hooks/useSuppliers';
import { Table } from '../../components/common/Table';
import { Button } from '../../components/common/Button';
import { Modal } from '../../components/common/Modal';
import { Input } from '../../components/common/Input';
import { ConfirmDialog } from '../../components/common/ConfirmDialog';
import type { Supplier, CreateSupplierRequest } from '../../types';

const supplierSchema = z.object({
  name: z.string().min(1, 'Name is required'),
  email: z.string().email('Invalid email').optional().or(z.literal('')),
  phone: z.string().optional(),
  address: z.string().optional(),
  city: z.string().optional(),
  country: z.string().optional(),
  contactPerson: z.string().optional(),
  website: z.string().optional(),
  taxId: z.string().optional(),
  paymentTerms: z.string().optional(),
  notes: z.string().optional(),
});

type SupplierFormData = z.infer<typeof supplierSchema>;

export const SuppliersPage: React.FC = () => {
  const { data: suppliers, isLoading } = useSuppliers();
  const { mutate: createSupplier, isLoading: creating } = useCreateSupplier();
  const { mutate: updateSupplier, isLoading: updating } = useUpdateSupplier();
  const { mutate: deleteSupplier } = useDeleteSupplier();

  const [modalOpen, setModalOpen] = useState(false);
  const [editTarget, setEditTarget] = useState<Supplier | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<Supplier | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<SupplierFormData>({ resolver: zodResolver(supplierSchema) });

  const openCreate = () => { setEditTarget(null); reset({}); setModalOpen(true); };
  const openEdit = (s: Supplier) => { setEditTarget(s); reset(s); setModalOpen(true); };
  const closeModal = () => { setModalOpen(false); setEditTarget(null); reset({}); };

  const onSubmit = (data: SupplierFormData) => {
    if (editTarget) {
      updateSupplier({ id: editTarget.id, data }, { onSuccess: closeModal });
    } else {
      createSupplier(data as CreateSupplierRequest, { onSuccess: closeModal });
    }
  };

  return (
    <div className="space-y-5">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-lg font-semibold text-gray-800">Suppliers</h2>
          <p className="text-sm text-gray-500">{suppliers?.length ?? 0} total records</p>
        </div>
        <Button onClick={openCreate} leftIcon={<svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" /></svg>}>
          Add Supplier
        </Button>
      </div>

      <Table<Supplier>
        isLoading={isLoading}
        data={suppliers ?? []}
        keyExtractor={(s) => s.id}
        columns={[
          { key: 'name', header: 'Name', render: (s) => <span className="font-medium text-gray-800">{s.name}</span> },
          { key: 'email', header: 'Email' },
          { key: 'phone', header: 'Phone' },
          { key: 'contactPerson', header: 'Contact Person' },
          { key: 'city', header: 'City' },
          { key: 'paymentTerms', header: 'Payment Terms' },
          {
            key: 'actions', header: 'Actions',
            render: (s) => (
              <div className="flex gap-2">
                <Button size="sm" variant="ghost" onClick={() => openEdit(s)}>Edit</Button>
                <Button size="sm" variant="danger" onClick={() => setDeleteTarget(s)}>Delete</Button>
              </div>
            ),
          },
        ]}
      />

      <Modal isOpen={modalOpen} onClose={closeModal} title={editTarget ? 'Edit Supplier' : 'Add Supplier'} size="lg">
        <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Input label="Supplier Name" required error={errors.name?.message} {...register('name')} />
          <Input label="Email" type="email" error={errors.email?.message} {...register('email')} />
          <Input label="Phone" {...register('phone')} />
          <Input label="Contact Person" {...register('contactPerson')} />
          <Input label="Website" {...register('website')} />
          <Input label="Tax ID" {...register('taxId')} />
          <Input label="Payment Terms" {...register('paymentTerms')} />
          <Input label="City" {...register('city')} />
          <Input label="Country" {...register('country')} />
          <Input label="Address" {...register('address')} />
          <div className="sm:col-span-2 flex justify-end gap-3 mt-2">
            <Button type="button" variant="secondary" onClick={closeModal}>Cancel</Button>
            <Button type="submit" isLoading={creating || updating}>{editTarget ? 'Update' : 'Create'}</Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        isOpen={!!deleteTarget}
        title="Delete Supplier"
        message={`Are you sure you want to delete "${deleteTarget?.name}"?`}
        confirmLabel="Delete"
        onConfirm={() => { deleteSupplier(deleteTarget!.id); setDeleteTarget(null); }}
        onCancel={() => setDeleteTarget(null)}
        isDangerous
      />
    </div>
  );
};

