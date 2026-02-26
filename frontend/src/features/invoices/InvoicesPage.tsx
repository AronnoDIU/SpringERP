import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import {
  useInvoices,
  useCreateInvoice,
  useDeleteInvoice,
} from '../../hooks/useInvoices';
import { Table } from '../../components/common/Table';
import { Button } from '../../components/common/Button';
import { Modal } from '../../components/common/Modal';
import { Input } from '../../components/common/Input';
import { Select } from '../../components/common/Select';
import { Badge } from '../../components/common/Badge';
import { ConfirmDialog } from '../../components/common/ConfirmDialog';
import type { Invoice, CreateInvoiceRequest, InvoiceStatus } from '../../types';

const invoiceSchema = z.object({
  customerId: z.coerce.number().min(1, 'Customer is required'),
  invoiceDate: z.string().min(1, 'Invoice date is required'),
  dueDate: z.string().optional(),
  status: z.enum(['DRAFT', 'PENDING', 'SENT', 'PAID', 'OVERDUE', 'CANCELLED'] as const),
  billingAddress: z.string().optional(),
  shippingAddress: z.string().optional(),
  paymentTerms: z.string().optional(),
  notes: z.string().optional(),
});

type InvoiceFormData = z.infer<typeof invoiceSchema>;

const statusBadgeVariant = (status: InvoiceStatus) => {
  const map: Record<InvoiceStatus, 'success' | 'warning' | 'danger' | 'info' | 'default'> = {
    PAID: 'success',
    PENDING: 'warning',
    SENT: 'info',
    DRAFT: 'default',
    OVERDUE: 'danger',
    CANCELLED: 'default',
  };
  return map[status];
};

const PAGE_SIZE = 10;

export const InvoicesPage: React.FC = () => {
  const [page, setPage] = useState(0);
  const { data: invoicePage, isLoading } = useInvoices(page, PAGE_SIZE);
  const { mutate: createInvoice, isLoading: creating } = useCreateInvoice();
  const { mutate: deleteInvoice } = useDeleteInvoice();

  const [modalOpen, setModalOpen] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState<Invoice | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<InvoiceFormData>({
    resolver: zodResolver(invoiceSchema),
    defaultValues: { status: 'DRAFT' },
  });

  const openCreate = () => { reset({ status: 'DRAFT', invoiceDate: new Date().toISOString().split('T')[0] }); setModalOpen(true); };
  const closeModal = () => { setModalOpen(false); reset({}); };

  const onSubmit = (data: InvoiceFormData) => {
    createInvoice(data as CreateInvoiceRequest, { onSuccess: closeModal });
  };

  const invoices = invoicePage?.content ?? [];
  const totalPages = invoicePage?.totalPages ?? 1;

  return (
    <div className="space-y-5">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-lg font-semibold text-gray-800">Invoices</h2>
          <p className="text-sm text-gray-500">{invoicePage?.totalElements ?? 0} total records</p>
        </div>
        <Button onClick={openCreate} leftIcon={<svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" /></svg>}>
          Create Invoice
        </Button>
      </div>

      <Table<Invoice>
        isLoading={isLoading}
        data={invoices}
        keyExtractor={(i) => i.id}
        columns={[
          { key: 'invoiceNumber', header: 'Invoice #', render: (i) => <span className="font-mono font-medium text-gray-800">{i.invoiceNumber ?? `INV-${i.id}`}</span> },
          { key: 'customerName', header: 'Customer' },
          { key: 'invoiceDate', header: 'Date', render: (i) => new Date(i.invoiceDate).toLocaleDateString() },
          { key: 'dueDate', header: 'Due Date', render: (i) => i.dueDate ? new Date(i.dueDate).toLocaleDateString() : '—' },
          { key: 'totalAmount', header: 'Amount', render: (i) => <span className="font-semibold">${(i.totalAmount ?? 0).toLocaleString()}</span> },
          { key: 'status', header: 'Status', render: (i) => <Badge label={i.status} variant={statusBadgeVariant(i.status)} /> },
          {
            key: 'actions', header: 'Actions',
            render: (i) => (
              <Button size="sm" variant="danger" onClick={() => setDeleteTarget(i)}>Delete</Button>
            ),
          },
        ]}
      />

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="flex items-center justify-center gap-2 mt-4">
          <Button size="sm" variant="secondary" disabled={page === 0} onClick={() => setPage((p) => p - 1)}>← Prev</Button>
          <span className="text-sm text-gray-600">Page {page + 1} of {totalPages}</span>
          <Button size="sm" variant="secondary" disabled={page >= totalPages - 1} onClick={() => setPage((p) => p + 1)}>Next →</Button>
        </div>
      )}

      <Modal isOpen={modalOpen} onClose={closeModal} title="Create Invoice" size="lg">
        <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Input label="Customer ID" type="number" required error={errors.customerId?.message} {...register('customerId')} />
          <Input label="Invoice Date" type="date" required error={errors.invoiceDate?.message} {...register('invoiceDate')} />
          <Input label="Due Date" type="date" {...register('dueDate')} />
          <Select
            label="Status"
            options={[
              { value: 'DRAFT', label: 'Draft' },
              { value: 'PENDING', label: 'Pending' },
              { value: 'SENT', label: 'Sent' },
              { value: 'PAID', label: 'Paid' },
              { value: 'OVERDUE', label: 'Overdue' },
              { value: 'CANCELLED', label: 'Cancelled' },
            ]}
            {...register('status')}
          />
          <Input label="Billing Address" {...register('billingAddress')} />
          <Input label="Shipping Address" {...register('shippingAddress')} />
          <Input label="Payment Terms" placeholder="Net 30..." {...register('paymentTerms')} />
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
            <Button type="submit" isLoading={creating}>Create Invoice</Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        isOpen={!!deleteTarget}
        title="Delete Invoice"
        message={`Are you sure you want to delete invoice "${deleteTarget?.invoiceNumber ?? `INV-${deleteTarget?.id}`}"?`}
        confirmLabel="Delete"
        onConfirm={() => { deleteInvoice(deleteTarget!.id); setDeleteTarget(null); }}
        onCancel={() => setDeleteTarget(null)}
        isDangerous
      />
    </div>
  );
};

