-- V1.0.1 - Core CRM and Commerce Tables
-- Creates companies, users, customers, suppliers, products, categories,
-- invoices, orders, and related tables that were missing from the initial schema.

-- Companies Table
CREATE TABLE IF NOT EXISTS companies (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(255),
    company_name        VARCHAR(255),
    company_code        VARCHAR(50),
    registration_number VARCHAR(100),
    tax_id              VARCHAR(50),
    vat_number          VARCHAR(50),
    address             VARCHAR(500),
    city                VARCHAR(100),
    state               VARCHAR(100),
    postal_code         VARCHAR(20),
    country             VARCHAR(100),
    phone               VARCHAR(30),
    email               VARCHAR(150),
    website             VARCHAR(255),
    currency            VARCHAR(10) DEFAULT 'USD',
    logo_url            VARCHAR(500),
    admin_email         VARCHAR(255),
    is_active           BOOLEAN     NOT NULL DEFAULT TRUE,
    status              VARCHAR(50) DEFAULT 'ACTIVE',
    config              LONGTEXT,
    subscription_tier   VARCHAR(50) DEFAULT 'PROFESSIONAL',
    max_users           INT         DEFAULT 100,
    max_storage_gb      INT         DEFAULT 100,
    trial_end_date      TIMESTAMP   NULL,
    contract_end_date   TIMESTAMP   NULL,
    created_at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    tenant_id           BIGINT,
    INDEX idx_companies_status (status),
    INDEX idx_companies_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id                          BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name                  VARCHAR(50)  NOT NULL,
    last_name                   VARCHAR(50)  NOT NULL,
    email                       VARCHAR(100) NOT NULL UNIQUE,
    password                    VARCHAR(255) NOT NULL,
    date_of_birth               DATE,
    role                        VARCHAR(50)  NOT NULL DEFAULT 'USER',
    phone_number                VARCHAR(15),
    address                     VARCHAR(255),
    reset_password_token        VARCHAR(100),
    reset_password_token_expiry DATETIME,
    is_active                   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at                  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by                  VARCHAR(100),
    updated_by                  VARCHAR(100),
    is_deleted                  BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at                  TIMESTAMP    NULL,
    tenant_id                   BIGINT,
    INDEX idx_users_role (role),
    INDEX idx_users_active (is_active),
    INDEX idx_users_tenant (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Categories Table
CREATE TABLE IF NOT EXISTS categories (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    is_deleted  BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at  TIMESTAMP    NULL,
    tenant_id   BIGINT,
    INDEX idx_categories_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Suppliers Table
CREATE TABLE IF NOT EXISTS suppliers (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    email          VARCHAR(150),
    phone          VARCHAR(30),
    address        VARCHAR(500),
    contact_person VARCHAR(150),
    status         VARCHAR(50) DEFAULT 'ACTIVE',
    is_active      BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),
    is_deleted     BOOLEAN     NOT NULL DEFAULT FALSE,
    deleted_at     TIMESTAMP   NULL,
    tenant_id      BIGINT,
    INDEX idx_suppliers_name (name),
    INDEX idx_suppliers_email (email),
    INDEX idx_suppliers_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Supplier Interactions Table
CREATE TABLE IF NOT EXISTS supplier_interactions (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id      BIGINT,
    interaction_type VARCHAR(100),
    notes            TEXT,
    interaction_date DATETIME,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100),
    is_deleted       BOOLEAN   NOT NULL DEFAULT FALSE,
    deleted_at       TIMESTAMP NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Customers Table
CREATE TABLE IF NOT EXISTS customers (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(150),
    phone      VARCHAR(30),
    address    VARCHAR(500),
    company_id BIGINT,
    is_active  BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN   NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP NULL,
    tenant_id  BIGINT,
    INDEX idx_customers_company (company_id),
    INDEX idx_customers_email (email),
    INDEX idx_customers_name (name),
    FOREIGN KEY (company_id) REFERENCES companies(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Customer Interactions Table
CREATE TABLE IF NOT EXISTS customer_interactions (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id      BIGINT,
    interaction_type VARCHAR(100),
    notes            TEXT,
    interaction_date DATETIME,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100),
    is_deleted       BOOLEAN   NOT NULL DEFAULT FALSE,
    deleted_at       TIMESTAMP NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Products Table
CREATE TABLE IF NOT EXISTS products (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    sku         VARCHAR(100) UNIQUE,
    description TEXT,
    price       DECIMAL(19,4),
    cost        DECIMAL(19,4),
    quantity    INT         DEFAULT 0,
    category_id BIGINT,
    supplier_id BIGINT,
    status      VARCHAR(50) DEFAULT 'ACTIVE',
    is_active   BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE,
    deleted_at  TIMESTAMP   NULL,
    tenant_id   BIGINT,
    INDEX idx_products_sku (sku),
    INDEX idx_products_category (category_id),
    INDEX idx_products_supplier (supplier_id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Invoices Table
CREATE TABLE IF NOT EXISTS invoices (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_number VARCHAR(100) UNIQUE,
    company_id     BIGINT,
    customer_id    BIGINT,
    status         VARCHAR(50),
    invoice_date   DATETIME,
    due_date       DATETIME,
    subtotal       DECIMAL(19,4),
    tax_amount     DECIMAL(19,4),
    discount       DECIMAL(19,4),
    total_amount   DECIMAL(19,4),
    notes          TEXT,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),
    is_deleted     BOOLEAN   NOT NULL DEFAULT FALSE,
    deleted_at     TIMESTAMP NULL,
    tenant_id      BIGINT,
    INDEX idx_invoices_customer (customer_id),
    INDEX idx_invoices_status (status),
    INDEX idx_invoices_date (invoice_date),
    FOREIGN KEY (company_id)  REFERENCES companies(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Invoice Items Table
CREATE TABLE IF NOT EXISTS invoice_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id  BIGINT,
    product_id  BIGINT,
    description VARCHAR(500),
    quantity    INT,
    unit_price  DECIMAL(19,4),
    total       DECIMAL(19,4),
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    is_deleted  BOOLEAN   NOT NULL DEFAULT FALSE,
    deleted_at  TIMESTAMP NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Orders Table
CREATE TABLE IF NOT EXISTS orders (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(100) UNIQUE,
    company_id   BIGINT,
    customer_id  BIGINT,
    status       VARCHAR(50),
    order_date   DATETIME,
    total_amount DECIMAL(19,4),
    notes        TEXT,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by   VARCHAR(100),
    updated_by   VARCHAR(100),
    is_deleted   BOOLEAN   NOT NULL DEFAULT FALSE,
    deleted_at   TIMESTAMP NULL,
    tenant_id    BIGINT,
    FOREIGN KEY (company_id)  REFERENCES companies(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Order Items Table
CREATE TABLE IF NOT EXISTS order_items (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT,
    product_id BIGINT,
    quantity   INT,
    unit_price DECIMAL(19,4),
    total      DECIMAL(19,4),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BOOLEAN   NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (order_id)   REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Purchase Orders Table
CREATE TABLE IF NOT EXISTS purchase_orders (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    po_number     VARCHAR(100) UNIQUE,
    supplier_id   BIGINT,
    status        VARCHAR(50),
    order_date    DATETIME,
    expected_date DATETIME,
    total_amount  DECIMAL(19,4),
    notes         TEXT,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by    VARCHAR(100),
    updated_by    VARCHAR(100),
    is_deleted    BOOLEAN   NOT NULL DEFAULT FALSE,
    deleted_at    TIMESTAMP NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Purchase Order Items Table
CREATE TABLE IF NOT EXISTS purchase_order_items (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_order_id BIGINT,
    product_id        BIGINT,
    quantity          INT,
    unit_price        DECIMAL(19,4),
    total             DECIMAL(19,4),
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted        BOOLEAN   NOT NULL DEFAULT FALSE,
    deleted_at        TIMESTAMP NULL,
    FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id),
    FOREIGN KEY (product_id)        REFERENCES products(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Warehouse Locations Table
CREATE TABLE IF NOT EXISTS warehouse_locations (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id BIGINT,
    aisle        VARCHAR(50),
    rack         VARCHAR(50),
    bin          VARCHAR(50),
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted   BOOLEAN   NOT NULL DEFAULT FALSE,
    deleted_at   TIMESTAMP NULL,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Asset Depreciation Table
CREATE TABLE IF NOT EXISTS asset_depreciation (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_id            BIGINT,
    depreciation_date   DATE,
    depreciation_amount DECIMAL(19,4),
    book_value          DECIMAL(19,4),
    method              VARCHAR(100),
    notes               TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted          BOOLEAN   NOT NULL DEFAULT FALSE,
    deleted_at          TIMESTAMP NULL,
    FOREIGN KEY (asset_id) REFERENCES assets(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Workflow Approvals Table
CREATE TABLE IF NOT EXISTS workflow_approvals (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    workflow_instance_id BIGINT,
    approver_id          BIGINT,
    status               VARCHAR(50),
    comments             TEXT,
    approved_at          DATETIME,
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted           BOOLEAN   NOT NULL DEFAULT FALSE,
    deleted_at           TIMESTAMP NULL,
    FOREIGN KEY (workflow_instance_id) REFERENCES workflow_instances(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Seed: Default admin user (BCrypt hash for "Admin@123")
-- Password: Admin@123
INSERT IGNORE INTO users (first_name, last_name, email, password, role, is_deleted, created_at)
VALUES ('Admin', 'User', 'admin@springerp.com',
        '$2b$10$S8yHESmqG7n1CIOANHLss.v.Fb9lE0J73b6MOwugWZn7P66nzUgSy',
        'ADMIN', false, NOW());

-- Seed: Default company
INSERT IGNORE INTO companies (name, company_name, company_code, email, currency, status, created_at)
VALUES ('SpringERP Inc', 'SpringERP Inc', 'SERP001', 'admin@springerp.com', 'USD', 'ACTIVE', NOW());

