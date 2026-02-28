-- V1.2.0 - Performance and Security Indexes
-- Adds indexes on high-traffic columns for all main business tables.
-- Uses conditional INSERT ... SELECT to safely skip already-existing indexes.

-- Helper: add index only if it doesn't already exist
-- Pattern: ALTER TABLE only when index not found in information_schema
SET @db = DATABASE();

-- users
SELECT COUNT(*) INTO @idx_exists FROM information_schema.statistics WHERE table_schema=@db AND table_name='users' AND index_name='idx_users_reset_token';
SET @sql = IF(@idx_exists = 0, 'ALTER TABLE users ADD INDEX idx_users_reset_token (reset_password_token)', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- customers
SELECT COUNT(*) INTO @idx_exists FROM information_schema.statistics WHERE table_schema=@db AND table_name='customers' AND index_name='idx_customers_company_id';
SET @sql = IF(@idx_exists = 0, 'ALTER TABLE customers ADD INDEX idx_customers_company_id (company_id)', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- invoices
SELECT COUNT(*) INTO @idx_exists FROM information_schema.statistics WHERE table_schema=@db AND table_name='invoices' AND index_name='idx_invoices_company_id';
SET @sql = IF(@idx_exists = 0, 'ALTER TABLE invoices ADD INDEX idx_invoices_company_id (company_id)', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

SELECT COUNT(*) INTO @idx_exists FROM information_schema.statistics WHERE table_schema=@db AND table_name='invoices' AND index_name='idx_invoices_customer_id';
SET @sql = IF(@idx_exists = 0, 'ALTER TABLE invoices ADD INDEX idx_invoices_customer_id (customer_id)', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

SELECT COUNT(*) INTO @idx_exists FROM information_schema.statistics WHERE table_schema=@db AND table_name='invoices' AND index_name='idx_invoices_invoice_date';
SET @sql = IF(@idx_exists = 0, 'ALTER TABLE invoices ADD INDEX idx_invoices_invoice_date (invoice_date)', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- company_users
SELECT COUNT(*) INTO @idx_exists FROM information_schema.statistics WHERE table_schema=@db AND table_name='company_users' AND index_name='idx_company_users_user_company';
SET @sql = IF(@idx_exists = 0, 'ALTER TABLE company_users ADD INDEX idx_company_users_user_company (user_id, company_id)', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- journal_entries
SELECT COUNT(*) INTO @idx_exists FROM information_schema.statistics WHERE table_schema=@db AND table_name='journal_entries' AND index_name='idx_journal_entries_company_id';
SET @sql = IF(@idx_exists = 0, 'ALTER TABLE journal_entries ADD INDEX idx_journal_entries_company_id (company_id)', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- general_ledger
SELECT COUNT(*) INTO @idx_exists FROM information_schema.statistics WHERE table_schema=@db AND table_name='general_ledger' AND index_name='idx_general_ledger_account_id';
SET @sql = IF(@idx_exists = 0, 'ALTER TABLE general_ledger ADD INDEX idx_general_ledger_account_id (account_id)', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- employees
SELECT COUNT(*) INTO @idx_exists FROM information_schema.statistics WHERE table_schema=@db AND table_name='employees' AND index_name='idx_employees_company_id';
SET @sql = IF(@idx_exists = 0, 'ALTER TABLE employees ADD INDEX idx_employees_company_id (company_id)', 'SELECT 1');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;
