import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'react-toastify';
import { customersApi } from '../api/customers.api';
import type { CreateCustomerRequest } from '../types';

export const CUSTOMERS_KEY = 'customers';

export function useCustomers() {
  return useQuery({
    queryKey: [CUSTOMERS_KEY],
    queryFn: customersApi.getAll,
  });
}

export function useCustomer(id: number) {
  return useQuery({
    queryKey: [CUSTOMERS_KEY, id],
    queryFn: () => customersApi.getById(id),
    enabled: !!id,
  });
}

export function useCreateCustomer() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (data: CreateCustomerRequest) => customersApi.create(data),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [CUSTOMERS_KEY] });
      toast.success('Customer created successfully');
    },
    onError: () => toast.error('Failed to create customer'),
  });
}

export function useUpdateCustomer() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: Partial<CreateCustomerRequest> }) =>
      customersApi.update(id, data),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [CUSTOMERS_KEY] });
      toast.success('Customer updated successfully');
    },
    onError: () => toast.error('Failed to update customer'),
  });
}

export function useDeleteCustomer() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => customersApi.delete(id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [CUSTOMERS_KEY] });
      toast.success('Customer deleted successfully');
    },
    onError: () => toast.error('Failed to delete customer'),
  });
}

