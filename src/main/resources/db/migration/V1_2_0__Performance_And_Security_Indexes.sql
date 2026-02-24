-- V1.2.0 - Performance and Security Indexes
-- Adds missing indexes on soft-delete columns and other high-traffic queries
-- for all main business tables.

-- ============================================================
-- Soft-delete indexes (is_deleted is queried on every read)
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_users_is_deleted ON users(is_deleted);
CREATE INDEX IF NOT EXISTS idx_customers_is_deleted ON customers(is_deleted);
CREATE INDEX IF NOT EXISTS idx_suppliers_is_deleted ON suppliers(is_deleted);
CREATE INDEX IF NOT EXISTS idx_invoices_is_deleted ON invoices(is_deleted);
CREATE INDEX IF NOT EXISTS idx_products_is_deleted ON products(is_deleted);
CREATE INDEX IF NOT EXISTS idx_employees_is_deleted ON employees(is_deleted);
CREATE INDEX IF NOT EXISTS idx_departments_is_deleted ON departments(is_deleted);

-- ============================================================
-- Customer indexes
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_customers_company_id ON customers(company_id);
CREATE INDEX IF NOT EXISTS idx_customers_email ON customers(email);
CREATE INDEX IF NOT EXISTS idx_customers_name ON customers(name);

-- ============================================================
-- Supplier indexes
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_suppliers_email ON suppliers(email);
CREATE INDEX IF NOT EXISTS idx_suppliers_name ON suppliers(name);

-- ============================================================
-- Invoice indexes (critical for multi-tenancy performance)
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_invoices_company_id ON invoices(company_id);
CREATE INDEX IF NOT EXISTS idx_invoices_customer_id ON invoices(customer_id);
CREATE INDEX IF NOT EXISTS idx_invoices_order_id ON invoices(order_id);
CREATE INDEX IF NOT EXISTS idx_invoices_status ON invoices(status);
CREATE INDEX IF NOT EXISTS idx_invoices_invoice_date ON invoices(invoice_date);
CREATE UNIQUE INDEX IF NOT EXISTS idx_invoices_invoice_number ON invoices(invoice_number);

-- ============================================================
-- Users indexes
-- ============================================================
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_reset_token ON users(reset_password_token);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);

-- ============================================================
-- Company_users composite index
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_company_users_user_company ON company_users(user_id, company_id);

-- ============================================================
-- Product indexes
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_products_category_id ON products(category_id);

-- ============================================================
-- Journal entries indexes
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_journal_entries_date ON journal_entries(journal_date);
CREATE INDEX IF NOT EXISTS idx_journal_entries_status ON journal_entries(status);
CREATE INDEX IF NOT EXISTS idx_journal_entries_company_id ON journal_entries(company_id);

-- ============================================================
-- General ledger indexes
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_general_ledger_account_id ON general_ledger(account_id);
CREATE INDEX IF NOT EXISTS idx_general_ledger_transaction_date ON general_ledger(transaction_date);

-- ============================================================
-- Employee indexes
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_employees_department_id ON employees(department_id);
CREATE INDEX IF NOT EXISTS idx_employees_employment_status ON employees(employment_status);
CREATE UNIQUE INDEX IF NOT EXISTS idx_employees_employee_id ON employees(employee_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_employees_email ON employees(email);

