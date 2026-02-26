import apiClient from './client';
import type { Product, CreateProductRequest } from '../types';

export const productsApi = {
  getAll: () =>
    apiClient.get<Product[]>('/products').then((r) => r.data),

  getById: (id: number) =>
    apiClient.get<Product>(`/products/${id}`).then((r) => r.data),

  create: (data: CreateProductRequest) =>
    apiClient.post<Product>('/products', data).then((r) => r.data),

  update: (id: number, data: Partial<CreateProductRequest>) =>
    apiClient.put<Product>(`/products/${id}`, data).then((r) => r.data),

  delete: (id: number) =>
    apiClient.delete(`/products/${id}`).then((r) => r.data),
};

