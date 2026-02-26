import apiClient from './client';
import type { Supplier, CreateSupplierRequest } from '../types';

export const suppliersApi = {
  getAll: () =>
    apiClient.get<Supplier[]>('/suppliers').then((r) => r.data),

  getById: (id: number) =>
    apiClient.get<Supplier>(`/suppliers/${id}`).then((r) => r.data),

  create: (data: CreateSupplierRequest) =>
    apiClient.post<Supplier>('/suppliers', data).then((r) => r.data),

  update: (id: number, data: Partial<CreateSupplierRequest>) =>
    apiClient.put<Supplier>(`/suppliers/${id}`, data).then((r) => r.data),

  delete: (id: number) =>
    apiClient.delete(`/suppliers/${id}`).then((r) => r.data),
};

