import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import {
  useEmployees,
  useCreateEmployee,
  useUpdateEmployee,
  useDeleteEmployee,
} from '../../hooks/useEmployees';
import { Table } from '../../components/common/Table';
import { Button } from '../../components/common/Button';
import { Modal } from '../../components/common/Modal';
import { Input } from '../../components/common/Input';
import { Select } from '../../components/common/Select';
import { Badge } from '../../components/common/Badge';
import { ConfirmDialog } from '../../components/common/ConfirmDialog';
import type { Employee, CreateEmployeeRequest, EmploymentStatus } from '../../types';

const employeeSchema = z.object({
  firstName: z.string().min(1, 'First name is required'),
  lastName: z.string().min(1, 'Last name is required'),
  email: z.string().email('Invalid email'),
  phone: z.string().optional(),
  designation: z.string().optional(),
  department: z.string().optional(),
  employmentType: z.enum(['FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERN'] as const).optional(),
  employmentStatus: z.enum(['ACTIVE', 'ON_LEAVE', 'RESIGNED', 'TERMINATED'] as const).optional(),
  dateOfJoining: z.string().optional(),
  baseSalary: z.coerce.number().min(0).optional(),
  officeLocation: z.string().optional(),
});

type EmployeeFormData = z.infer<typeof employeeSchema>;

const statusBadge = (status?: EmploymentStatus) => {
  if (status === 'ACTIVE') return 'success';
  if (status === 'ON_LEAVE') return 'warning';
  if (status === 'RESIGNED' || status === 'TERMINATED') return 'danger';
  return 'default';
};

export const EmployeesPage: React.FC = () => {
  const { data: employees, isLoading } = useEmployees();
  const { mutate: createEmployee, isLoading: creating } = useCreateEmployee();
  const { mutate: updateEmployee, isLoading: updating } = useUpdateEmployee();
  const { mutate: deleteEmployee } = useDeleteEmployee();

  const [modalOpen, setModalOpen] = useState(false);
  const [editTarget, setEditTarget] = useState<Employee | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<Employee | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<EmployeeFormData>({ resolver: zodResolver(employeeSchema) });

  const openCreate = () => { setEditTarget(null); reset({}); setModalOpen(true); };
  const openEdit = (e: Employee) => { setEditTarget(e); reset(e); setModalOpen(true); };
  const closeModal = () => { setModalOpen(false); setEditTarget(null); reset({}); };

  const onSubmit = (data: EmployeeFormData) => {
    if (editTarget) {
      updateEmployee({ id: editTarget.id, data }, { onSuccess: closeModal });
    } else {
      createEmployee(data as CreateEmployeeRequest, { onSuccess: closeModal });
    }
  };

  return (
    <div className="space-y-5">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-lg font-semibold text-gray-800">Employees</h2>
          <p className="text-sm text-gray-500">{employees?.length ?? 0} total records</p>
        </div>
        <Button onClick={openCreate} leftIcon={<svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" /></svg>}>
          Add Employee
        </Button>
      </div>

      <Table<Employee>
        isLoading={isLoading}
        data={employees ?? []}
        keyExtractor={(e) => e.id}
        columns={[
          { key: 'employeeId', header: 'ID' },
          { key: 'firstName', header: 'Name', render: (e) => <span className="font-medium text-gray-800">{e.firstName} {e.lastName}</span> },
          { key: 'email', header: 'Email' },
          { key: 'designation', header: 'Designation' },
          { key: 'department', header: 'Department' },
          { key: 'employmentType', header: 'Type' },
          {
            key: 'employmentStatus', header: 'Status',
            render: (e) => <Badge label={e.employmentStatus ?? 'N/A'} variant={statusBadge(e.employmentStatus)} />,
          },
          {
            key: 'baseSalary', header: 'Salary',
            render: (e) => e.baseSalary ? `$${e.baseSalary.toLocaleString()}` : '—',
          },
          {
            key: 'actions', header: 'Actions',
            render: (e) => (
              <div className="flex gap-2">
                <Button size="sm" variant="ghost" onClick={() => openEdit(e)}>Edit</Button>
                <Button size="sm" variant="danger" onClick={() => setDeleteTarget(e)}>Delete</Button>
              </div>
            ),
          },
        ]}
      />

      <Modal isOpen={modalOpen} onClose={closeModal} title={editTarget ? 'Edit Employee' : 'Add Employee'} size="lg">
        <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Input label="First Name" required error={errors.firstName?.message} {...register('firstName')} />
          <Input label="Last Name" required error={errors.lastName?.message} {...register('lastName')} />
          <Input label="Email" type="email" required error={errors.email?.message} {...register('email')} />
          <Input label="Phone" {...register('phone')} />
          <Input label="Designation" {...register('designation')} />
          <Input label="Department" {...register('department')} />
          <Select
            label="Employment Type"
            options={[
              { value: 'FULL_TIME', label: 'Full Time' },
              { value: 'PART_TIME', label: 'Part Time' },
              { value: 'CONTRACT', label: 'Contract' },
              { value: 'INTERN', label: 'Intern' },
            ]}
            placeholder="Select type..."
            {...register('employmentType')}
          />
          <Select
            label="Employment Status"
            options={[
              { value: 'ACTIVE', label: 'Active' },
              { value: 'ON_LEAVE', label: 'On Leave' },
              { value: 'RESIGNED', label: 'Resigned' },
              { value: 'TERMINATED', label: 'Terminated' },
            ]}
            placeholder="Select status..."
            {...register('employmentStatus')}
          />
          <Input label="Date of Joining" type="date" {...register('dateOfJoining')} />
          <Input label="Base Salary" type="number" step="0.01" {...register('baseSalary')} />
          <Input label="Office Location" {...register('officeLocation')} />
          <div className="sm:col-span-2 flex justify-end gap-3 mt-2">
            <Button type="button" variant="secondary" onClick={closeModal}>Cancel</Button>
            <Button type="submit" isLoading={creating || updating}>{editTarget ? 'Update' : 'Create'}</Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        isOpen={!!deleteTarget}
        title="Delete Employee"
        message={`Are you sure you want to delete ${deleteTarget?.firstName} ${deleteTarget?.lastName}?`}
        confirmLabel="Delete"
        onConfirm={() => { deleteEmployee(deleteTarget!.id); setDeleteTarget(null); }}
        onCancel={() => setDeleteTarget(null)}
        isDangerous
      />
    </div>
  );
};

