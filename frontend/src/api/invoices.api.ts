import apiClient from './client';
import type { Invoice, CreateInvoiceRequest, InvoiceStatus, PageResponse } from '../types';

export const invoicesApi = {
  getAll: (page = 0, size = 10, sortBy = 'id') =>
    apiClient
      .get<PageResponse<Invoice>>('/invoices', { params: { page, size, sortBy } })
      .then((r) => r.data),

  getById: (id: number) =>
    apiClient.get<Invoice>(`/invoices/${id}`).then((r) => r.data),

  getByStatus: (status: InvoiceStatus) =>
    apiClient.get<Invoice[]>(`/invoices/status/${status}`).then((r) => r.data),

  create: (data: CreateInvoiceRequest) =>
    apiClient.post<Invoice>('/invoices', data).then((r) => r.data),

  update: (id: number, data: Partial<CreateInvoiceRequest>) =>
    apiClient.put<Invoice>(`/invoices/${id}`, data).then((r) => r.data),

  delete: (id: number) =>
    apiClient.delete(`/invoices/${id}`).then((r) => r.data),
};

