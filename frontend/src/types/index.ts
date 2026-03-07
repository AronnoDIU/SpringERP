// ─── Auth ─────────────────────────────────────────────────────────────────────

export interface LoginRequest {
  email: string;
  password: string;
}

export interface JwtResponse {
  token: string;
}

export interface AuthUser {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
}

// ─── Pagination ───────────────────────────────────────────────────────────────

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

// ─── Company / Navigation (kept from original) ────────────────────────────────

export interface Company {
  id: string;
  name: string;
  industry: string;
  color: string;
  icon: string;
  location: string;
}

export interface SubModule {
  id: string;
  label: string;
  children?: { id: string; label: string }[];
}

export interface NavItem {
  id: string;
  label: string;
  icon: any;
  subModules: SubModule[];
}

// ─── Customer ─────────────────────────────────────────────────────────────────

export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  name?: string;
  email?: string;
  phone?: string;
  address?: string;
  city?: string;
  country?: string;
  companyName?: string;
  notes?: string;
  stage?: string;
  lastContact?: string;
  value?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateCustomerRequest {
  firstName: string;
  lastName: string;
  email?: string;
  phone?: string;
  address?: string;
  city?: string;
  country?: string;
  companyName?: string;
  notes?: string;
}

// ─── Supplier ─────────────────────────────────────────────────────────────────

export interface Supplier {
  id: number;
  name: string;
  email?: string;
  phone?: string;
  address?: string;
  companyName?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateSupplierRequest {
  name: string;
  email?: string;
  phone?: string;
  address?: string;
  companyName?: string;
}

// ─── Product ──────────────────────────────────────────────────────────────────

export interface Product {
  id: number;
  name: string;
  sku?: string;
  description?: string;
  price: number;
  costPrice?: number;
  stock: number;
  stockQuantity?: number;
  unit?: string;
  taxRate?: number;
  isActive?: boolean;
  categoryName?: string;
  category?: { id: number; name: string };
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateProductRequest {
  name: string;
  sku?: string;
  description?: string;
  price: number;
  costPrice?: number;
  stock?: number;
  stockQuantity?: number;
  unit?: string;
  taxRate?: number;
  categoryId?: number;
}

// ─── Employee ─────────────────────────────────────────────────────────────────

export type EmploymentStatus = 'ACTIVE' | 'ON_LEAVE' | 'RESIGNED' | 'TERMINATED';
export type EmploymentType = 'FULL_TIME' | 'PART_TIME' | 'CONTRACT' | 'INTERN';

export interface Employee {
  id: number;
  employeeId?: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  designation?: string;
  department?: string;
  employmentType?: EmploymentType;
  employmentStatus?: EmploymentStatus;
  dateOfJoining?: string;
  baseSalary?: number;
  officeLocation?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateEmployeeRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  designation?: string;
  department?: string;
  employmentType?: EmploymentType;
  employmentStatus?: EmploymentStatus;
  dateOfJoining?: string;
  baseSalary?: number;
  officeLocation?: string;
}

// ─── Invoice ──────────────────────────────────────────────────────────────────

export type InvoiceStatus = 'DRAFT' | 'PENDING' | 'SENT' | 'PAID' | 'OVERDUE' | 'CANCELLED';

export interface InvoiceItem {
  id?: number;
  productId?: number;
  description?: string;
  quantity: number;
  unitPrice: number;
  totalPrice?: number;
}

export interface Invoice {
  id: number;
  invoiceNumber?: string;
  customerId?: number;
  customerName?: string;
  invoiceDate: string;
  dueDate?: string;
  status: InvoiceStatus;
  subtotal?: number;
  taxAmount?: number;
  totalAmount?: number;
  billingAddress?: string;
  shippingAddress?: string;
  paymentTerms?: string;
  notes?: string;
  items?: InvoiceItem[];
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateInvoiceRequest {
  customerId: number;
  invoiceDate: string;
  dueDate?: string;
  status: InvoiceStatus;
  billingAddress?: string;
  shippingAddress?: string;
  paymentTerms?: string;
  notes?: string;
  items?: InvoiceItem[];
}

// ─── Accounting ───────────────────────────────────────────────────────────────

export type AccountType = 'ASSET' | 'LIABILITY' | 'EQUITY' | 'REVENUE' | 'EXPENSE';

export interface ChartOfAccount {
  id: number;
  accountCode: string;
  accountName: string;
  accountType: AccountType;
  accountCategory?: string;
  description?: string;
  currentBalance?: number;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}
