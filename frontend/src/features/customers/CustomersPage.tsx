import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import {
  useCustomers,
  useCreateCustomer,
  useUpdateCustomer,
  useDeleteCustomer,
} from '../../hooks/useCustomers';
import { Table } from '../../components/common/Table';
import { Button } from '../../components/common/Button';
import { Modal } from '../../components/common/Modal';
import { Input } from '../../components/common/Input';
import { ConfirmDialog } from '../../components/common/ConfirmDialog';
import type { Customer, CreateCustomerRequest } from '../../types';

const customerSchema = z.object({
  firstName: z.string().min(1, 'First name is required'),
  lastName: z.string().min(1, 'Last name is required'),
  email: z.string().email('Invalid email'),
  phone: z.string().optional(),
  address: z.string().optional(),
  city: z.string().optional(),
  country: z.string().optional(),
  companyName: z.string().optional(),
  notes: z.string().optional(),
});

type CustomerFormData = z.infer<typeof customerSchema>;

export const CustomersPage: React.FC = () => {
  const { data: customers, isLoading } = useCustomers();
  const { mutate: createCustomer, isLoading: creating } = useCreateCustomer();
  const { mutate: updateCustomer, isLoading: updating } = useUpdateCustomer();
  const { mutate: deleteCustomer } = useDeleteCustomer();

  const [modalOpen, setModalOpen] = useState(false);
  const [editTarget, setEditTarget] = useState<Customer | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<Customer | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CustomerFormData>({ resolver: zodResolver(customerSchema) });

  const openCreate = () => {
    setEditTarget(null);
    reset({});
    setModalOpen(true);
  };

  const openEdit = (customer: Customer) => {
    setEditTarget(customer);
    reset(customer);
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setEditTarget(null);
    reset({});
  };

  const onSubmit = (data: CustomerFormData) => {
    if (editTarget) {
      updateCustomer(
        { id: editTarget.id, data },
        { onSuccess: closeModal },
      );
    } else {
      createCustomer(data as CreateCustomerRequest, { onSuccess: closeModal });
    }
  };

  const confirmDelete = () => {
    if (deleteTarget) {
      deleteCustomer(deleteTarget.id);
      setDeleteTarget(null);
    }
  };

  return (
    <div className="space-y-5">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-lg font-semibold text-gray-800">Customers</h2>
          <p className="text-sm text-gray-500">{customers?.length ?? 0} total records</p>
        </div>
        <Button onClick={openCreate} leftIcon={
          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
        }>
          Add Customer
        </Button>
      </div>

      {/* Table */}
      <Table<Customer>
        isLoading={isLoading}
        data={customers ?? []}
        keyExtractor={(c) => c.id}
        columns={[
          {
            key: 'firstName',
            header: 'Name',
            render: (c) => (
              <span className="font-medium text-gray-800">
                {c.firstName} {c.lastName}
              </span>
            ),
          },
          { key: 'email', header: 'Email' },
          { key: 'phone', header: 'Phone' },
          { key: 'companyName', header: 'Company' },
          { key: 'city', header: 'City' },
          {
            key: 'actions',
            header: 'Actions',
            render: (c) => (
              <div className="flex gap-2">
                <Button size="sm" variant="ghost" onClick={() => openEdit(c)}>
                  Edit
                </Button>
                <Button size="sm" variant="danger" onClick={() => setDeleteTarget(c)}>
                  Delete
                </Button>
              </div>
            ),
          },
        ]}
      />

      {/* Create / Edit Modal */}
      <Modal
        isOpen={modalOpen}
        onClose={closeModal}
        title={editTarget ? 'Edit Customer' : 'Add Customer'}
        size="lg"
      >
        <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Input label="First Name" required error={errors.firstName?.message} {...register('firstName')} />
          <Input label="Last Name" required error={errors.lastName?.message} {...register('lastName')} />
          <Input label="Email" type="email" required error={errors.email?.message} {...register('email')} />
          <Input label="Phone" {...register('phone')} />
          <Input label="Company Name" {...register('companyName')} />
          <Input label="City" {...register('city')} />
          <Input label="Country" {...register('country')} />
          <Input label="Address" {...register('address')} />
          <div className="sm:col-span-2">
            <label className="text-sm font-medium text-gray-700">Notes</label>
            <textarea
              {...register('notes')}
              className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              rows={3}
            />
          </div>
          <div className="sm:col-span-2 flex justify-end gap-3 mt-2">
            <Button type="button" variant="secondary" onClick={closeModal}>Cancel</Button>
            <Button type="submit" isLoading={creating || updating}>
              {editTarget ? 'Update' : 'Create'}
            </Button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirm */}
      <ConfirmDialog
        isOpen={!!deleteTarget}
        title="Delete Customer"
        message={`Are you sure you want to delete ${deleteTarget?.firstName} ${deleteTarget?.lastName}? This action cannot be undone.`}
        confirmLabel="Delete"
        onConfirm={confirmDelete}
        onCancel={() => setDeleteTarget(null)}
        isDangerous
      />
    </div>
  );
};

