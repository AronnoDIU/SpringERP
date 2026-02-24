-- V1.1.0 - Multi-Company Architecture Implementation
-- Adds support for multi-company operations with enhanced roles and permissions

-- Update existing company table structure
ALTER TABLE companies ADD COLUMN IF NOT EXISTS parent_company_id BIGINT;
ALTER TABLE companies ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'ACTIVE';
ALTER TABLE companies ADD COLUMN IF NOT EXISTS config LONGTEXT;
ALTER TABLE companies ADD COLUMN IF NOT EXISTS company_code VARCHAR(50) UNIQUE;
ALTER TABLE companies ADD COLUMN IF NOT EXISTS tax_id VARCHAR(50);
ALTER TABLE companies ADD COLUMN IF NOT EXISTS vat_number VARCHAR(50);
ALTER TABLE companies ADD COLUMN IF NOT EXISTS logo_url VARCHAR(255);
ALTER TABLE companies ADD COLUMN IF NOT EXISTS default_language VARCHAR(10) DEFAULT 'en';
ALTER TABLE companies ADD COLUMN IF NOT EXISTS default_timezone VARCHAR(50) DEFAULT 'UTC';
ALTER TABLE companies ADD COLUMN IF NOT EXISTS max_users INT DEFAULT 100;
ALTER TABLE companies ADD COLUMN IF NOT EXISTS max_api_calls_per_month BIGINT DEFAULT 1000000;
ALTER TABLE companies ADD COLUMN IF NOT EXISTS subscription_tier VARCHAR(50) DEFAULT 'PROFESSIONAL';
ALTER TABLE companies ADD COLUMN IF NOT EXISTS trial_end_date DATETIME;
ALTER TABLE companies ADD COLUMN IF NOT EXISTS contract_end_date DATETIME;
ALTER TABLE companies ADD COLUMN IF NOT EXISTS created_by_user_id BIGINT;
ALTER TABLE companies ADD COLUMN IF NOT EXISTS last_login_at DATETIME;
ALTER TABLE companies ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT FALSE;

-- Add indexes for performance
CREATE INDEX IF NOT EXISTS idx_companies_parent_id ON companies(parent_company_id);
CREATE INDEX IF NOT EXISTS idx_companies_status ON companies(status);
CREATE INDEX IF NOT EXISTS idx_companies_deleted ON companies(is_deleted);
CREATE INDEX IF NOT EXISTS idx_companies_code ON companies(company_code);

-- Add foreign key for parent company
ALTER TABLE companies ADD CONSTRAINT fk_company_parent FOREIGN KEY (parent_company_id) REFERENCES companies(id) ON DELETE SET NULL;

-- Create Role table
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(100) NOT NULL,
    role_code VARCHAR(100) NOT NULL,
    description TEXT,
    company_id BIGINT NOT NULL,
    is_system_role BOOLEAN DEFAULT FALSE,
    permissions LONGTEXT,
    is_active BOOLEAN DEFAULT TRUE,
    priority INT DEFAULT 100,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE,
    tenant_id BIGINT,

    CONSTRAINT fk_role_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE,
    UNIQUE KEY uk_role_code_company (role_code, company_id),
    INDEX idx_role_company (company_id),
    INDEX idx_role_active (is_active),
    INDEX idx_role_system (is_system_role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Permission table
CREATE TABLE IF NOT EXISTS permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_code VARCHAR(100) NOT NULL UNIQUE,
    resource VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    is_system_permission BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE KEY uk_resource_action (resource, action),
    INDEX idx_resource (resource),
    INDEX idx_action (action),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create CompanyUser junction table
CREATE TABLE IF NOT EXISTS company_users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    is_company_admin BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    last_login_at DATETIME,
    notes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE,
    tenant_id BIGINT,

    CONSTRAINT fk_company_user_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_company_user_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE,
    CONSTRAINT fk_company_user_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    UNIQUE KEY uk_company_user (user_id, company_id),
    INDEX idx_user (user_id),
    INDEX idx_company (company_id),
    INDEX idx_role (role_id),
    INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Update existing users table to include tenant tracking
ALTER TABLE users ADD COLUMN IF NOT EXISTS default_company_id BIGINT;
ALTER TABLE users ADD COLUMN IF NOT EXISTS last_company_id BIGINT;
ALTER TABLE users ADD CONSTRAINT fk_user_default_company FOREIGN KEY (default_company_id) REFERENCES companies(id) ON DELETE SET NULL;

-- Add company_id column to all business entities (if not exists)
-- Accounting
ALTER TABLE chart_of_accounts ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE journal_entries ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE general_ledger ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE budgets ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;

-- HR
ALTER TABLE departments ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE employees ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE attendances ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE leaves ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE salaries ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;

-- Inventory
ALTER TABLE inventory_items ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE stock_movements ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE warehouses ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;

-- Assets
ALTER TABLE assets ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE asset_depreciations ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;

-- Invoices & Orders
ALTER TABLE invoices ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS company_id BIGINT NOT NULL DEFAULT 1;

-- Create indexes for company filters
CREATE INDEX IF NOT EXISTS idx_coa_company ON chart_of_accounts(company_id);
CREATE INDEX IF NOT EXISTS idx_je_company ON journal_entries(company_id);
CREATE INDEX IF NOT EXISTS idx_gl_company ON general_ledger(company_id);
CREATE INDEX IF NOT EXISTS idx_emp_company ON employees(company_id);
CREATE INDEX IF NOT EXISTS idx_inv_company ON inventory_items(company_id);
CREATE INDEX IF NOT EXISTS idx_wh_company ON warehouses(company_id);
CREATE INDEX IF NOT EXISTS idx_inv_company ON invoices(company_id);

-- Insert default permissions for all resources
INSERT INTO permissions (permission_code, resource, action, category, is_system_permission)
VALUES
    -- Company Permissions
    ('COMPANY:CREATE', 'COMPANY', 'CREATE', 'ADMIN', 1),
    ('COMPANY:READ', 'COMPANY', 'READ', 'ADMIN', 1),
    ('COMPANY:UPDATE', 'COMPANY', 'UPDATE', 'ADMIN', 1),
    ('COMPANY:DELETE', 'COMPANY', 'DELETE', 'ADMIN', 1),

    -- User Permissions
    ('USER:CREATE', 'USER', 'CREATE', 'ADMIN', 1),
    ('USER:READ', 'USER', 'READ', 'ADMIN', 1),
    ('USER:UPDATE', 'USER', 'UPDATE', 'ADMIN', 1),
    ('USER:DELETE', 'USER', 'DELETE', 'ADMIN', 1),

    -- Role Permissions
    ('ROLE:CREATE', 'ROLE', 'CREATE', 'ADMIN', 1),
    ('ROLE:READ', 'ROLE', 'READ', 'ADMIN', 1),
    ('ROLE:UPDATE', 'ROLE', 'UPDATE', 'ADMIN', 1),
    ('ROLE:DELETE', 'ROLE', 'DELETE', 'ADMIN', 1),

    -- Invoice Permissions
    ('INVOICE:CREATE', 'INVOICE', 'CREATE', 'FINANCIAL', 1),
    ('INVOICE:READ', 'INVOICE', 'READ', 'FINANCIAL', 1),
    ('INVOICE:UPDATE', 'INVOICE', 'UPDATE', 'FINANCIAL', 1),
    ('INVOICE:DELETE', 'INVOICE', 'DELETE', 'FINANCIAL', 1),
    ('INVOICE:APPROVE', 'INVOICE', 'APPROVE', 'FINANCIAL', 1),

    -- Employee Permissions
    ('EMPLOYEE:CREATE', 'EMPLOYEE', 'CREATE', 'HR', 1),
    ('EMPLOYEE:READ', 'EMPLOYEE', 'READ', 'HR', 1),
    ('EMPLOYEE:UPDATE', 'EMPLOYEE', 'UPDATE', 'HR', 1),
    ('EMPLOYEE:DELETE', 'EMPLOYEE', 'DELETE', 'HR', 1),

    -- Inventory Permissions
    ('INVENTORY:CREATE', 'INVENTORY', 'CREATE', 'INVENTORY', 1),
    ('INVENTORY:READ', 'INVENTORY', 'READ', 'INVENTORY', 1),
    ('INVENTORY:UPDATE', 'INVENTORY', 'UPDATE', 'INVENTORY', 1),
    ('INVENTORY:DELETE', 'INVENTORY', 'DELETE', 'INVENTORY', 1),
    ('INVENTORY:ADJUST', 'INVENTORY', 'ADJUST', 'INVENTORY', 1),

    -- Reporting Permissions
    ('REPORT:READ', 'REPORT', 'READ', 'REPORTING', 1),
    ('REPORT:CREATE', 'REPORT', 'CREATE', 'REPORTING', 1),
    ('REPORT:EXPORT', 'REPORT', 'EXPORT', 'REPORTING', 1)
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- Add audit log table if not exists
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT,
    action VARCHAR(50),
    user_id BIGINT,
    user_name VARCHAR(100),
    old_values LONGTEXT,
    new_values LONGTEXT,
    ip_address VARCHAR(50),
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    company_id BIGINT,
    tenant_id BIGINT,

    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_user (user_id),
    INDEX idx_timestamp (timestamp),
    INDEX idx_action (action),
    INDEX idx_company (company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Update migration version
INSERT INTO schema_version (installed_rank, version, description, type, script, execution_time, success)
VALUES ((SELECT MAX(installed_rank) + 1 FROM schema_version), '1.1.0', 'Multi-Company Architecture', 'SQL', 'V1_1_0__MultiCompanyArchitecture.sql', 0, TRUE)
ON DUPLICATE KEY UPDATE description = 'Multi-Company Architecture', success = TRUE;

