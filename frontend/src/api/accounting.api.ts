import apiClient from './client';
import type { ChartOfAccount, PageResponse } from '../types';
export const accountingApi = {
  getChartOfAccounts: () =>
    apiClient.get<PageResponse<ChartOfAccount>>('/accounting/chart-of-accounts', { params: { size: 200 } }).then((r) => r.data.content),
  getAccountById: (id: number) =>
    apiClient.get<ChartOfAccount>(`/accounting/chart-of-accounts/${id}`).then((r) => r.data),
  createAccount: (data: Omit<ChartOfAccount, 'id'>) =>
    apiClient.post<ChartOfAccount>('/accounting/chart-of-accounts', data).then((r) => r.data),
  updateAccount: (id: number, data: Partial<ChartOfAccount>) =>
    apiClient.put<ChartOfAccount>(`/accounting/chart-of-accounts/${id}`, data).then((r) => r.data),
};
