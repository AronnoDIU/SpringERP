import apiClient from './client';
import type { Customer, CreateCustomerRequest } from '../types';

export const customersApi = {
  getAll: () =>
    apiClient.get<Customer[]>('/customers').then((r) => r.data),

  getById: (id: number) =>
    apiClient.get<Customer>(`/customers/${id}`).then((r) => r.data),

  search: (query: string) =>
    apiClient.get<Customer[]>('/customers/search', { params: { query } }).then((r) => r.data),

  create: (data: CreateCustomerRequest) =>
    apiClient.post<Customer>('/customers', data).then((r) => r.data),

  update: (id: number, data: Partial<CreateCustomerRequest>) =>
    apiClient.put<Customer>(`/customers/${id}`, data).then((r) => r.data),

  delete: (id: number) =>
    apiClient.delete(`/customers/${id}`).then((r) => r.data),
};

