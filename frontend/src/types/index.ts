// ─── Auth ────────────────────────────────────────────────────────────────────
export interface LoginRequest {
  email: string;
  password: string;
}

export interface JwtResponse {
  token: string;
}

export interface AuthUser {
  id: number;
  username: string;
  email: string;
  roles: string[];
  companyId?: number;
}

// ─── Common ──────────────────────────────────────────────────────────────────
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface ApiError {
  status: number;
  message: string;
  timestamp?: string;
  errors?: Record<string, string>;
}

// ─── Customer ─────────────────────────────────────────────────────────────────
export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  address?: string;
  city?: string;
  country?: string;
  companyName?: string;
  notes?: string;
  createdAt?: string;
  updatedAt?: string;
}

export type CreateCustomerRequest = Omit<Customer, 'id' | 'createdAt' | 'updatedAt'>;

// ─── Supplier ─────────────────────────────────────────────────────────────────
export interface Supplier {
  id: number;
  name: string;
  email?: string;
  phone?: string;
  address?: string;
  city?: string;
  country?: string;
  contactPerson?: string;
  website?: string;
  taxId?: string;
  paymentTerms?: string;
  notes?: string;
  createdAt?: string;
  updatedAt?: string;
}

export type CreateSupplierRequest = Omit<Supplier, 'id' | 'createdAt' | 'updatedAt'>;

// ─── Product ──────────────────────────────────────────────────────────────────
export interface Product {
  id: number;
  name: string;
  sku?: string;
  description?: string;
  price: number;
  costPrice?: number;
  stockQuantity?: number;
  unit?: string;
  categoryId?: number;
  categoryName?: string;
  taxRate?: number;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export type CreateProductRequest = Omit<Product, 'id' | 'createdAt' | 'updatedAt'>;

// ─── Invoice ──────────────────────────────────────────────────────────────────
export type InvoiceStatus = 'DRAFT' | 'PENDING' | 'SENT' | 'PAID' | 'OVERDUE' | 'CANCELLED';

export interface InvoiceItem {
  id?: number;
  productId?: number;
  productName?: string;
  description?: string;
  quantity: number;
  unitPrice: number;
  taxRate?: number;
  totalPrice: number;
}

export interface Invoice {
  id: number;
  companyId?: number;
  companyName?: string;
  invoiceNumber?: string;
  orderId?: number;
  customerId: number;
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
  createdByUsername?: string;
  createdAt?: string;
  updatedAt?: string;
}

export type CreateInvoiceRequest = Omit<Invoice, 'id' | 'createdAt' | 'updatedAt' | 'createdByUsername' | 'companyName' | 'customerName' | 'invoiceNumber'>;

// ─── Employee ─────────────────────────────────────────────────────────────────
export type EmploymentType = 'FULL_TIME' | 'PART_TIME' | 'CONTRACT' | 'INTERN';
export type EmploymentStatus = 'ACTIVE' | 'ON_LEAVE' | 'RESIGNED' | 'TERMINATED';

export interface Employee {
  id: number;
  employeeId?: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  designation?: string;
  department?: string;
  departmentId?: number;
  employmentType?: EmploymentType;
  employmentStatus?: EmploymentStatus;
  dateOfJoining?: string;
  baseSalary?: number;
  reportingManagerId?: number;
  officeLocation?: string;
  isActive?: boolean;
}

export type CreateEmployeeRequest = Omit<Employee, 'id'>;

// ─── Company ──────────────────────────────────────────────────────────────────
export interface Company {
  id: number;
  name: string;
  legalName?: string;
  taxId?: string;
  email?: string;
  phone?: string;
  address?: string;
  city?: string;
  country?: string;
  website?: string;
  industry?: string;
  isActive?: boolean;
  createdAt?: string;
}

// ─── Chart of Accounts ────────────────────────────────────────────────────────
export type AccountType = 'ASSET' | 'LIABILITY' | 'EQUITY' | 'REVENUE' | 'EXPENSE';

export interface ChartOfAccount {
  id: number;
  accountCode: string;
  accountName: string;
  accountType: AccountType;
  parentAccountId?: number;
  description?: string;
  isActive?: boolean;
  balance?: number;
}

// ─── Dashboard ────────────────────────────────────────────────────────────────
export interface DashboardStats {
  totalRevenue: number;
  totalCustomers: number;
  totalProducts: number;
  totalInvoices: number;
  pendingInvoices: number;
  overdueInvoices: number;
  totalEmployees: number;
  totalSuppliers: number;
}

export interface RevenueDataPoint {
  month: string;
  revenue: number;
  expenses: number;
}

