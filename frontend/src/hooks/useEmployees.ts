import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'react-toastify';
import { employeesApi } from '../api/employees.api';
import type { CreateEmployeeRequest } from '../types';

export const EMPLOYEES_KEY = 'employees';

export function useEmployees() {
  return useQuery({ queryKey: [EMPLOYEES_KEY], queryFn: employeesApi.getAll });
}

export function useEmployee(id: number) {
  return useQuery({
    queryKey: [EMPLOYEES_KEY, id],
    queryFn: () => employeesApi.getById(id),
    enabled: !!id,
  });
}

export function useCreateEmployee() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (data: CreateEmployeeRequest) => employeesApi.create(data),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [EMPLOYEES_KEY] });
      toast.success('Employee created successfully');
    },
    onError: () => toast.error('Failed to create employee'),
  });
}

export function useUpdateEmployee() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: Partial<CreateEmployeeRequest> }) =>
      employeesApi.update(id, data),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [EMPLOYEES_KEY] });
      toast.success('Employee updated successfully');
    },
    onError: () => toast.error('Failed to update employee'),
  });
}

export function useDeleteEmployee() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => employeesApi.delete(id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: [EMPLOYEES_KEY] });
      toast.success('Employee deleted successfully');
    },
    onError: () => toast.error('Failed to delete employee'),
  });
}

