-- V1.1.0 - Multi-Company Architecture Implementation
-- Adds support for multi-company operations with enhanced roles and permissions

-- Add missing columns to companies table
ALTER TABLE companies ADD COLUMN parent_company_id    BIGINT;
ALTER TABLE companies ADD COLUMN default_language     VARCHAR(10) DEFAULT 'en';
ALTER TABLE companies ADD COLUMN default_timezone     VARCHAR(50) DEFAULT 'UTC';
ALTER TABLE companies ADD COLUMN max_api_calls_per_month BIGINT DEFAULT 1000000;
ALTER TABLE companies ADD COLUMN created_by_user_id   BIGINT;
ALTER TABLE companies ADD COLUMN last_login_at        DATETIME;
ALTER TABLE companies ADD COLUMN is_deleted           BOOLEAN DEFAULT FALSE;

-- Add indexes for performance
CREATE INDEX idx_companies_parent_id ON companies(parent_company_id);
CREATE INDEX idx_companies_deleted   ON companies(is_deleted);

-- Add missing columns to users table
ALTER TABLE users ADD COLUMN default_company_id BIGINT;
ALTER TABLE users ADD COLUMN last_company_id    BIGINT;

-- Create Roles table
CREATE TABLE IF NOT EXISTS roles (
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name      VARCHAR(100) NOT NULL,
    role_code      VARCHAR(100) NOT NULL,
    description    TEXT,
    company_id     BIGINT       NOT NULL,
    is_system_role BOOLEAN      DEFAULT FALSE,
    permissions    LONGTEXT,
    is_active      BOOLEAN      DEFAULT TRUE,
    priority       INT          DEFAULT 100,
    created_at     DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by     BIGINT,
    updated_by     BIGINT,
    is_deleted     BOOLEAN      DEFAULT FALSE,
    tenant_id      BIGINT,
    CONSTRAINT fk_role_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE,
    UNIQUE KEY uk_role_code_company (role_code, company_id),
    INDEX idx_role_company (company_id),
    INDEX idx_role_active  (is_active),
    INDEX idx_role_system  (is_system_role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Permissions table
CREATE TABLE IF NOT EXISTS permissions (
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_code      VARCHAR(100) NOT NULL UNIQUE,
    resource             VARCHAR(100) NOT NULL,
    action               VARCHAR(50)  NOT NULL,
    description          TEXT,
    category             VARCHAR(50),
    is_system_permission BOOLEAN DEFAULT FALSE,
    is_active            BOOLEAN DEFAULT TRUE,
    created_at           DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_resource_action (resource, action),
    INDEX idx_resource (resource),
    INDEX idx_action   (action),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create CompanyUser junction table
CREATE TABLE IF NOT EXISTS company_users (
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id          BIGINT  NOT NULL,
    company_id       BIGINT  NOT NULL,
    role_id          BIGINT  NOT NULL,
    is_company_admin BOOLEAN DEFAULT FALSE,
    is_active        BOOLEAN DEFAULT TRUE,
    assigned_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    status           VARCHAR(50) DEFAULT 'ACTIVE',
    last_login_at    DATETIME,
    notes            TEXT,
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by       BIGINT,
    updated_by       BIGINT,
    is_deleted       BOOLEAN DEFAULT FALSE,
    tenant_id        BIGINT,
    CONSTRAINT fk_company_user_user    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
    CONSTRAINT fk_company_user_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE,
    CONSTRAINT fk_company_user_role    FOREIGN KEY (role_id)    REFERENCES roles(id)    ON DELETE CASCADE,
    UNIQUE KEY uk_company_user (user_id, company_id),
    INDEX idx_cu_user    (user_id),
    INDEX idx_cu_company (company_id),
    INDEX idx_cu_role    (role_id),
    INDEX idx_cu_active  (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add company_id to all business entities that don't already have it
ALTER TABLE chart_of_accounts ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE journal_entries   ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE general_ledger    ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE budgets            ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE departments        ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE employees          ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE attendances        ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE leaves             ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE salaries           ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE inventory_items    ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE stock_movements    ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE warehouses         ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE assets             ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;

-- Create indexes for company filters
CREATE INDEX idx_coa_company       ON chart_of_accounts(company_id);
CREATE INDEX idx_je_company        ON journal_entries(company_id);
CREATE INDEX idx_gl_company        ON general_ledger(company_id);
CREATE INDEX idx_emp_company       ON employees(company_id);
CREATE INDEX idx_inv_items_company ON inventory_items(company_id);
CREATE INDEX idx_wh_company        ON warehouses(company_id);

-- Insert default permissions
INSERT INTO permissions (permission_code, resource, action, category, is_system_permission) VALUES
    ('COMPANY:CREATE',  'COMPANY',   'CREATE', 'ADMIN',     1),
    ('COMPANY:READ',    'COMPANY',   'READ',   'ADMIN',     1),
    ('COMPANY:UPDATE',  'COMPANY',   'UPDATE', 'ADMIN',     1),
    ('COMPANY:DELETE',  'COMPANY',   'DELETE', 'ADMIN',     1),
    ('USER:CREATE',     'USER',      'CREATE', 'ADMIN',     1),
    ('USER:READ',       'USER',      'READ',   'ADMIN',     1),
    ('USER:UPDATE',     'USER',      'UPDATE', 'ADMIN',     1),
    ('USER:DELETE',     'USER',      'DELETE', 'ADMIN',     1),
    ('ROLE:CREATE',     'ROLE',      'CREATE', 'ADMIN',     1),
    ('ROLE:READ',       'ROLE',      'READ',   'ADMIN',     1),
    ('ROLE:UPDATE',     'ROLE',      'UPDATE', 'ADMIN',     1),
    ('ROLE:DELETE',     'ROLE',      'DELETE', 'ADMIN',     1),
    ('INVOICE:CREATE',  'INVOICE',   'CREATE', 'FINANCIAL', 1),
    ('INVOICE:READ',    'INVOICE',   'READ',   'FINANCIAL', 1),
    ('INVOICE:UPDATE',  'INVOICE',   'UPDATE', 'FINANCIAL', 1),
    ('INVOICE:DELETE',  'INVOICE',   'DELETE', 'FINANCIAL', 1),
    ('INVOICE:APPROVE', 'INVOICE',   'APPROVE','FINANCIAL', 1),
    ('EMPLOYEE:CREATE', 'EMPLOYEE',  'CREATE', 'HR',        1),
    ('EMPLOYEE:READ',   'EMPLOYEE',  'READ',   'HR',        1),
    ('EMPLOYEE:UPDATE', 'EMPLOYEE',  'UPDATE', 'HR',        1),
    ('EMPLOYEE:DELETE', 'EMPLOYEE',  'DELETE', 'HR',        1),
    ('INVENTORY:CREATE','INVENTORY', 'CREATE', 'INVENTORY', 1),
    ('INVENTORY:READ',  'INVENTORY', 'READ',   'INVENTORY', 1),
    ('INVENTORY:UPDATE','INVENTORY', 'UPDATE', 'INVENTORY', 1),
    ('INVENTORY:DELETE','INVENTORY', 'DELETE', 'INVENTORY', 1),
    ('INVENTORY:ADJUST','INVENTORY', 'ADJUST', 'INVENTORY', 1),
    ('REPORT:READ',     'REPORT',    'READ',   'REPORTING', 1),
    ('REPORT:CREATE',   'REPORT',    'CREATE', 'REPORTING', 1),
    ('REPORT:EXPORT',   'REPORT',    'EXPORT', 'REPORTING', 1)
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

-- End of V1.1.0 migration

