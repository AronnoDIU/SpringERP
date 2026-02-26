import apiClient from './client';
import type { ChartOfAccount } from '../types';
export const accountingApi = {
  getChartOfAccounts: () =>
    apiClient.get<ChartOfAccount[]>('/accounting/chart-of-accounts').then((r) => r.data),
  getAccountById: (id: number) =>
    apiClient.get<ChartOfAccount>(`/accounting/chart-of-accounts/${id}`).then((r) => r.data),
  createAccount: (data: Omit<ChartOfAccount, 'id'>) =>
    apiClient.post<ChartOfAccount>('/accounting/chart-of-accounts', data).then((r) => r.data),
  updateAccount: (id: number, data: Partial<ChartOfAccount>) =>
    apiClient.put<ChartOfAccount>(`/accounting/chart-of-accounts/${id}`, data).then((r) => r.data),
};
