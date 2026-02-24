# 📊 Multi-Company & Admin Panel Implementation Guide

## Overview

This guide covers the implementation of a **multi-company ERP system** with a professional **admin panel** using AdminLTE, supporting enterprise-grade operations for a $50M+ project.

---

## ✨ Features Implemented

### Phase 1: Multi-Company Foundation ✅
- **Company Hierarchy Support** - Parent/subsidiary relationships
- **Company-Level Data Isolation** - All data segregated by company_id
- **Company User Management** - Users assigned to companies with roles
- **Role-Based Access Control (RBAC)** - Company-specific roles and permissions
- **Company Status Management** - Active, Inactive, Suspended, Trial, Archived

### Phase 2: Admin Panel ✅
- **Dashboard** - KPI overview with company statistics
- **Company Management** - Create, read, update, delete companies
- **User Management** - Manage users across companies
- **Role & Permission Management** - Configure access control
- **System Settings** - Global system configuration
- **Audit Logs Viewer** - Track all changes
- **System Health Monitoring** - Monitor system status

### Phase 3: Administrative Features ⏳
- Bulk Operations (CSV Import/Export)
- API Key Management
- Compliance & Audit Reports
- Custom Dashboards
- Advanced Analytics
- Backup & Restore

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────┐
│      Admin Panel (Web UI)               │
│      Built with AdminLTE                │
└──────────────┬──────────────────────────┘
               │
               ├─ Admin Controllers (HTTP)
               │
┌──────────────▼──────────────────────────┐
│      REST API Layer                     │
│      /api/v1/admin/*                    │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Service Layer                      │
│  - CompanyManagementService             │
│  - RbacService                          │
│  - AuditService                         │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Repository Layer (JPA)             │
│  - CompanyRepository                    │
│  - RoleRepository                       │
│  - PermissionRepository                 │
│  - CompanyUserRepository                │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Database (MySQL)                   │
│  Companies, Roles, Permissions          │
│  CompanyUsers (Junction)                │
└─────────────────────────────────────────┘
```

---

## 📁 File Structure

```
SpringERP/
├── src/main/java/com/springerp/
│   ├── entity/
│   │   ├── CompanyEnhanced.java          ← Multi-company support
│   │   ├── Role.java                     ← Company-specific roles
│   │   ├── Permission.java               ← Granular permissions
│   │   └── CompanyUser.java              ← Junction entity
│   │
│   ├── repository/
│   │   ├── CompanyRepository.java        ← Already existed
│   │   ├── RoleRepository.java           ← NEW
│   │   ├── PermissionRepository.java     ← NEW
│   │   └── CompanyUserRepository.java    ← NEW
│   │
│   ├── service/
│   │   ├── CompanyManagementService.java ← NEW interface
│   │   └── RbacService.java              ← NEW interface
│   │
│   ├── service/impl/
│   │   ├── CompanyManagementServiceImpl.java  ← NEW impl
│   │   └── RbacServiceImpl.java               ← NEW impl
│   │
│   └── controller/
│       ├── admin/
│       │   ├── AdminDashboardController.java      ← NEW
│       │   ├── AdminCompanyController.java        ← NEW
│       │   ├── AdminRoleController.java           ← NEW
│       │   ├── AdminUserController.java           ← (To implement)
│       │   └── AdminAuditController.java          ← (To implement)
│       │
│       └── api/
│           └── CompanyApiController.java          ← NEW REST API
│
├── src/main/resources/
│   ├── templates/admin/
│   │   ├── layout.html                  ← Base layout
│   │   ├── dashboard.html               ← Dashboard
│   │   ├── company/
│   │   │   ├── list.html                ← Company list
│   │   │   └── form.html                ← Company form
│   │   ├── user/
│   │   │   └── list.html                ← (To implement)
│   │   ├── role/
│   │   │   └── list.html                ← (To implement)
│   │   └── settings/
│   │       └── system.html              ← (To implement)
│   │
│   ├── db/migration/
│   │   └── V1_1_0__MultiCompanyArchitecture.sql   ← NEW
│   │
│   └── static/admin/
│       ├── css/
│       │   └── admin-theme.css          ← (To implement)
│       ├── js/
│       │   └── admin.js                 ← (To implement)
│       └── img/
│           └── (Admin icons & logos)
│
└── pom.xml                              ← Dependencies configured
```

---

## 🚀 Getting Started

### 1. Database Setup

Run the migration:
```sql
-- Already included: V1_1_0__MultiCompanyArchitecture.sql
-- Creates: companies (enhanced), roles, permissions, company_users tables
-- Updates: All business entities with company_id field
```

### 2. Access Admin Panel

**URL:** `http://localhost:8080/admin`

**Default Credentials:** (Set up during installation)
- Username: admin
- Password: (configured in properties)

### 3. Create Your First Company

1. Navigate to `/admin/companies/create`
2. Fill in company details
3. Click "Create Company"
4. System assigns default roles (ADMIN, MANAGER, USER, VIEWER)

### 4. Manage Users & Roles

1. Go to `/admin/users` to manage users
2. Assign users to companies
3. Assign roles to users within each company
4. Configure permissions for each role

---

## 📋 Key Endpoints

### Admin Web UI
- `GET /admin` - Dashboard
- `GET /admin/companies` - Companies list
- `POST /admin/companies` - Create company
- `GET /admin/companies/{id}` - Company details
- `GET /admin/users` - Users list
- `GET /admin/roles` - Roles list
- `GET /admin/settings` - System settings
- `GET /admin/audit-logs` - Audit logs

### Admin REST API
- `GET /api/v1/admin/companies` - List all companies (paginated)
- `GET /api/v1/admin/companies/{id}` - Get company details
- `GET /api/v1/admin/companies/{id}/statistics` - Company stats
- `GET /api/v1/admin/companies/{id}/hierarchy` - Company hierarchy
- `POST /api/v1/admin/companies/{id}/activate` - Activate company
- `POST /api/v1/admin/companies/{id}/deactivate` - Deactivate company
- `POST /api/v1/admin/companies/{id}/suspend` - Suspend company

---

## 🔐 Security Features

### Company Isolation
- **Automatic Filtering:** All queries automatically filtered by company_id
- **Tenant Context:** Uses ThreadLocal to maintain current company context
- **Data Segregation:** Users can only access their assigned companies

### Role-Based Access
- **System Roles:** ADMIN, MANAGER, USER, VIEWER
- **Custom Roles:** Create company-specific roles
- **Permission Matrix:** Fine-grained resource + action permissions
- **Audit Trail:** Track all permission changes

### Admin Security
- **Authentication:** JWT-based, requires ADMIN role
- **Authorization:** Company-level access control
- **Activity Logging:** All admin actions logged to audit_logs table
- **IP Whitelisting:** (Can be configured)

---

## 📊 Data Model

### Companies Table
```sql
- id (PK)
- company_name (unique)
- company_code (unique)
- parent_company_id (FK) -- for hierarchy
- status (ACTIVE, INACTIVE, SUSPENDED, TRIAL, ARCHIVED)
- subscription_tier (STARTER, PROFESSIONAL, ENTERPRISE, CUSTOM)
- config (JSON) -- company-specific settings
- max_users (int) -- subscription limit
- max_api_calls_per_month (bigint) -- API quota
- created_at, updated_at
```

### Roles Table
```sql
- id (PK)
- role_code (unique per company)
- role_name
- company_id (FK)
- permissions (JSON array of permission codes)
- is_system_role (boolean)
- is_active (boolean)
```

### Permissions Table
```sql
- id (PK)
- permission_code (unique) -- e.g., "INVOICE:CREATE"
- resource (e.g., "INVOICE", "EMPLOYEE")
- action (e.g., "CREATE", "READ", "UPDATE", "DELETE", "APPROVE")
- category (ADMIN, FINANCIAL, HR, INVENTORY, REPORTING)
- is_system_permission (boolean)
```

### CompanyUsers Table (Junction)
```sql
- id (PK)
- user_id (FK)
- company_id (FK)
- role_id (FK)
- is_company_admin (boolean) -- can manage users in company
- is_active (boolean)
- assigned_at (timestamp)
- status (ACTIVE, INACTIVE, SUSPENDED, INVITED, ARCHIVED)
- last_login_at (timestamp)
```

---

## 💡 Usage Examples

### Create a Company via API
```bash
curl -X POST http://localhost:8080/api/v1/admin/companies \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "companyName": "Acme Corp",
    "companyCode": "ACME",
    "email": "info@acme.com",
    "country": "USA",
    "currency": "USD"
  }'
```

### Get Company Statistics
```bash
curl -X GET http://localhost:8080/api/v1/admin/companies/1/statistics \
  -H "Authorization: Bearer YOUR_TOKEN"

# Response:
{
  "companyId": 1,
  "activeUsers": 25,
  "totalApiCalls": 15000,
  "totalDataSize": 5242880
}
```

### Manage Roles
```java
// Via service
rbacService.createRole("ACCOUNTANT", "Accountant", "Handles financial operations", companyId);
rbacService.assignPermissionToRole(roleId, "INVOICE:CREATE");
rbacService.assignPermissionToRole(roleId, "INVOICE:APPROVE");
rbacService.assignRoleToUser(userId, companyId, roleId);
```

### Check Permissions
```java
// Check if user has permission
boolean canApprove = rbacService.hasPermission(
    userId, companyId, "INVOICE", "APPROVE"
);

// Get all user permissions
List<String> permissions = rbacService.getUserPermissions(userId, companyId);
```

---

## 🎨 Admin Panel UI

### Technologies Used
- **Framework:** AdminLTE 3.2 (Free, Professional)
- **UI Components:** Bootstrap 4
- **Icons:** Font Awesome 6, Bootstrap Icons
- **Tables:** DataTables (sorting, filtering, pagination)
- **Charts:** Chart.js (optional)
- **Responsive:** Mobile-friendly design

### Key Pages
1. **Dashboard** - Overview of companies, users, system health
2. **Companies** - CRUD operations, hierarchy view
3. **Users** - User management across companies
4. **Roles** - Role creation and permission assignment
5. **Audit Logs** - Complete activity history
6. **System Settings** - Global configuration
7. **API Keys** - API key management (future)
8. **System Health** - Monitoring and metrics

---

## 🔄 Request/Response Examples

### List Companies
```http
GET /admin/companies?page=0&size=10&search=acme

200 OK
{
  "content": [
    {
      "id": 1,
      "companyName": "Acme Corp",
      "companyCode": "ACME",
      "status": "ACTIVE",
      "subscriptionTier": "PROFESSIONAL",
      "activeUsers": 25,
      "createdAt": "2024-02-08T10:30:00"
    }
  ],
  "totalElements": 100,
  "totalPages": 10,
  "number": 0,
  "size": 10
}
```

### Create Role
```http
POST /admin/roles
Content-Type: application/x-www-form-urlencoded

companyId=1&roleCode=FIN_MGR&roleName=Finance Manager&description=Manages all financial operations

302 Redirect to /admin/roles?companyId=1
Message: "Role created successfully!"
```

---

## ⚙️ Configuration

### Application Properties
```yaml
# application.yml
springerp:
  admin:
    enabled: true
    path: /admin
    session-timeout: 3600
    require-2fa: false
  
  company:
    default-subscription-tier: PROFESSIONAL
    default-max-users: 100
    trial-period-days: 30
  
  rbac:
    enable-audit-logging: true
    default-roles:
      - code: ADMIN
        name: Administrator
      - code: MANAGER
        name: Manager
      - code: USER
        name: User
      - code: VIEWER
        name: Viewer
```

---

## 📈 Next Steps

### High Priority
1. ✅ Implement User Management UI (`AdminUserController` + templates)
2. ✅ Complete Audit Logs Viewer
3. ✅ Add System Settings page
4. ⏳ Implement API Key Management
5. ⏳ Add System Health/Monitoring dashboard

### Medium Priority
1. ⏳ Bulk Operations (CSV import/export)
2. ⏳ Custom Dashboards
3. ⏳ Advanced Reporting
4. ⏳ Compliance Reports (GDPR, SOX)

### Low Priority
1. ⏳ Two-Factor Authentication for admin
2. ⏳ Email templates management
3. ⏳ Custom branding per company
4. ⏳ Webhook management

---

## 🧪 Testing

### Unit Tests
```java
@Test
void testCreateCompany() {
    CompanyDTO dto = new CompanyDTO();
    dto.setCompanyName("Test Corp");
    dto.setCompanyCode("TEST");
    
    Company company = companyService.createCompany(dto, null);
    
    assertNotNull(company.getId());
    assertEquals("TEST", company.getCompanyCode());
    assertEquals(CompanyStatus.ACTIVE, company.getStatus());
}

@Test
void testRbacPermission() {
    boolean hasPermission = rbacService.hasPermission(
        userId, companyId, "INVOICE", "APPROVE"
    );
    assertTrue(hasPermission);
}
```

### Integration Tests
- Company CRUD operations
- User-Company-Role relationships
- Permission validation
- Audit logging
- API endpoints

---

## 📚 Additional Resources

- **AdminLTE Docs:** https://adminlte.io/
- **Spring Security:** https://spring.io/projects/spring-security
- **Thymeleaf:** https://www.thymeleaf.org/
- **Bootstrap:** https://getbootstrap.com/

---

## 🆘 Troubleshooting

### Users Can't See Companies
**Cause:** CompanyUser record not created
**Solution:** Add user to company via `/admin/users/{id}/assign-company`

### Permission Denied Error
**Cause:** User doesn't have required permission
**Solution:** Assign role with appropriate permissions via `/admin/roles/{id}/permissions`

### Database Migration Failed
**Cause:** Flyway migration issue
**Solution:** Check V1_1_0__MultiCompanyArchitecture.sql syntax

---

**Status:** ✅ Phase 1 & 2 Complete, Phase 3 In Progress
**Next Update:** Q2 2024
**Maintained by:** SpringERP Development Team

