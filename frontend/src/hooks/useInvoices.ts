import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'react-toastify';
import { invoicesApi } from '../api/invoices.api';
import type { CreateInvoiceRequest } from '../types';

export const INVOICES_KEY = 'invoices';

export function useInvoices(page = 0, size = 10) {
  return useQuery({
    queryKey: [INVOICES_KEY, page, size],
    queryFn: () => invoicesApi.getAll(page, size),
    keepPreviousData: true,
  });
}

export function useInvoice(id: number) {
  return useQuery({
    queryKey: [INVOICES_KEY, id],
    queryFn: () => invoicesApi.getById(id),
    enabled: !!id,
  });
}

export function useCreateInvoice() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (data: CreateInvoiceRequest) => invoicesApi.create(data),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [INVOICES_KEY] });
      toast.success('Invoice created successfully');
    },
    onError: () => toast.error('Failed to create invoice'),
  });
}

export function useUpdateInvoice() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: Partial<CreateInvoiceRequest> }) =>
      invoicesApi.update(id, data),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [INVOICES_KEY] });
      toast.success('Invoice updated successfully');
    },
    onError: () => toast.error('Failed to update invoice'),
  });
}

export function useDeleteInvoice() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => invoicesApi.delete(id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [INVOICES_KEY] });
      toast.success('Invoice deleted successfully');
    },
    onError: () => toast.error('Failed to delete invoice'),
  });
}

