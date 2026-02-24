# SpringERP Quick Start Guide

## Installation & Setup

### 1. Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.8.4 or higher
- Git

### 2. Clone & Build
```bash
git clone https://github.com/AronnoDIU/SpringERP.git
cd SpringERP
mvn clean install
```

### 3. Database Setup
```sql
CREATE DATABASE springerp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'erp_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON springerp.* TO 'erp_user'@'localhost';
FLUSH PRIVILEGES;
```

### 4. Environment Configuration
Create a `.env` file in the project root:
```
MYSQL_URL=jdbc:mysql://localhost:3306/springerp?useSSL=false&serverTimezone=UTC
MYSQL_USERNAME=erp_user
MYSQL_PASSWORD=secure_password
JASYPT_PASSWORD=your-encryption-password-change-me
JWT_SECRET=your-jwt-secret-key-change-me
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### 5. Run the Application
```bash
# Development mode
mvn spring-boot:run

# Or build JAR and run
mvn clean package
java -jar target/spring-erp-0.0.1-SNAPSHOT.jar
```

### 6. Access the Application
- **API Documentation**: http://localhost:8080/api/v1/swagger-ui.html
- **Health Check**: http://localhost:8080/api/v1/actuator/health
- **Metrics**: http://localhost:8080/api/v1/actuator/metrics

---

## Core Features Checklist

### ✅ Accounting Module
- [ ] Chart of Accounts setup
- [ ] Journal Entry creation
- [ ] Trial Balance generation
- [ ] Balance Sheet reporting
- [ ] Income Statement reporting

### ✅ Inventory Module
- [ ] Create Warehouses
- [ ] Add Inventory Items
- [ ] Record Stock Movements
- [ ] Monitor Low Stock Items
- [ ] Physical Inventory Counting

### ✅ HR Module
- [ ] Set up Departments
- [ ] Register Employees
- [ ] Track Attendance
- [ ] Process Leave Requests
- [ ] Generate Payroll

### ✅ Asset Management
- [ ] Register Fixed Assets
- [ ] Calculate Depreciation
- [ ] Track Asset Disposals
- [ ] Generate Asset Reports

### ✅ Workflows
- [ ] Define Approval Workflows
- [ ] Process Approvals
- [ ] Handle Escalations
- [ ] Monitor Pending Workflows

---

## Initial Data Setup

### 1. Create Tenant
```bash
POST /api/v1/tenants
{
  "tenantName": "Acme Corporation",
  "tenantIdentifier": "acme-corp",
  "adminEmail": "admin@acme.com",
  "subscriptionTier": "PROFESSIONAL",
  "maxUsers": 50
}
```

### 2. Create Chart of Accounts
```bash
POST /api/v1/accounts
{
  "accountCode": "1000",
  "accountName": "Cash",
  "accountType": "ASSET",
  "normalBalance": "DEBIT",
  "isActive": true
}
```

### 3. Create Department
```bash
POST /api/v1/departments
{
  "departmentCode": "DEPT-0001",
  "departmentName": "Finance",
  "description": "Finance Department"
}
```

### 4. Create Employee
```bash
POST /api/v1/employees
{
  "employeeId": "EMP-2024-0001",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@acme.com",
  "phone": "555-0100",
  "designation": "Manager",
  "departmentId": 1,
  "employmentType": "FULL_TIME",
  "dateOfJoining": "2024-01-01",
  "baseSalary": 5000.00
}
```

### 5. Create Warehouse
```bash
POST /api/v1/warehouses
{
  "warehouseName": "Main Warehouse",
  "warehouseCode": "WH-0001",
  "address": "123 Business St",
  "city": "New York",
  "state": "NY",
  "postalCode": "10001",
  "country": "USA"
}
```

---

## Key API Patterns

### Authentication
```bash
POST /api/auth/login
{
  "username": "user@example.com",
  "password": "password"
}

# Response includes JWT token
# Use token in Authorization header: Bearer <token>
```

### Pagination
```bash
GET /api/v1/employees?page=0&size=10&sortBy=firstName
```

### Filtering
```bash
GET /api/v1/employees?status=ACTIVE
GET /api/v1/accounts?type=ASSET
```

### Approval Workflows
```bash
# Submit for approval
POST /api/v1/journal-entries/{id}/submit

# Approve
POST /api/v1/journal-entries/{id}/approve
{
  "approverId": 1,
  "comments": "Approved"
}

# Reject
POST /api/v1/journal-entries/{id}/reject
{
  "approverId": 1,
  "comments": "Missing documentation"
}
```

---

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check credentials in `.env` file
- Ensure database character set is utf8mb4

### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### JWT Token Issues
- Ensure JWT_SECRET is set in environment
- Token should be in format: `Bearer <token>`
- Check token expiration (default 24 hours)

### Email Configuration
- For Gmail, use app-specific password
- Enable "Less secure apps" in Gmail settings (if not using 2FA)
- For corporate email, verify SMTP settings

---

## Best Practices

1. **Always use HTTPS in production**
2. **Rotate JWT secrets regularly**
3. **Encrypt sensitive fields** before storing
4. **Backup database regularly**
5. **Monitor audit logs** for security
6. **Use strong passwords** for all users
7. **Enable rate limiting** in production
8. **Test workflows** thoroughly before deployment

---

## Advanced Configuration

### Multi-Tenancy Headers
All API requests should include tenant ID:
```bash
curl -X GET http://localhost:8080/api/v1/employees \
  -H "Authorization: Bearer <token>" \
  -H "X-Tenant-ID: 1"
```

### Custom Report Templates
1. Access Report Template management
2. Define JRXML or JSON template
3. Set refresh frequency
4. Configure recipients for scheduling

### Workflow Definition Example
```json
{
  "workflowName": "Invoice Approval",
  "workflowCode": "INV-APPROVAL-001",
  "processType": "INVOICE_APPROVAL",
  "requiresApproval": true,
  "maxApprovalLevels": 3,
  "timeoutDays": 5
}
```

---

## Performance Tuning

### Database Optimization
```sql
-- Add indexes for frequently queried fields
CREATE INDEX idx_employee_status ON employees(employment_status);
CREATE INDEX idx_account_type ON chart_of_accounts(account_type);
```

### Cache Configuration
Adjust in `application.yml`:
```yaml
spring:
  cache:
    caffeine:
      spec: maximumSize=2000,expireAfterWrite=30m
```

### Connection Pool
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 30
      minimum-idle: 10
```

---

## Deployment

### Docker Deployment
```dockerfile
FROM openjdk:17-slim
COPY target/spring-erp-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Docker Compose
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: springerp
      MYSQL_ROOT_PASSWORD: root
  
  springerp:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    environment:
      MYSQL_URL: jdbc:mysql://mysql:3306/springerp
      MYSQL_USERNAME: root
      MYSQL_PASSWORD: root
```

---

## Support & Resources

- **Documentation**: Check INDUSTRY_STANDARD_ENHANCEMENTS.md
- **GitHub**: https://github.com/AronnoDIU/SpringERP
- **Issues**: Report bugs on GitHub Issues
- **Discussions**: Join community discussions

---

## License
MIT License - See LICENSE file for details

---

**Happy ERP-ing!** 🚀

