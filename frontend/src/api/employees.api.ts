import apiClient from './client';
import type { Employee, CreateEmployeeRequest } from '../types';

export const employeesApi = {
  getAll: () =>
    apiClient.get<Employee[]>('/employees').then((r) => r.data),

  getById: (id: number) =>
    apiClient.get<Employee>(`/employees/${id}`).then((r) => r.data),

  create: (data: CreateEmployeeRequest) =>
    apiClient.post<Employee>('/employees', data).then((r) => r.data),

  update: (id: number, data: Partial<CreateEmployeeRequest>) =>
    apiClient.put<Employee>(`/employees/${id}`, data).then((r) => r.data),

  delete: (id: number) =>
    apiClient.delete(`/employees/${id}`).then((r) => r.data),
};

