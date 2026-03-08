import apiClient from './client';
import type { Employee, CreateEmployeeRequest, PageResponse } from '../types';

export const employeesApi = {
  getAll: () =>
    apiClient
      .get<PageResponse<Employee>>('/employees', { params: { size: 200 } })
      .then((r) => r.data.content),

  getById: (id: number) =>
    apiClient.get<Employee>(`/employees/${id}`).then((r) => r.data),

  create: (data: CreateEmployeeRequest) =>
    apiClient.post<Employee>('/employees', data).then((r) => r.data),

  update: (id: number, data: Partial<CreateEmployeeRequest>) =>
    apiClient.put<Employee>(`/employees/${id}`, data).then((r) => r.data),

  delete: (id: number) =>
    apiClient.delete(`/employees/${id}`).then((r) => r.data),
};

