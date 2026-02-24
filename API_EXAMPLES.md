# SpringERP REST API Examples

## Authentication

### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin@example.com",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "expiresIn": 86400,
  "userId": 1,
  "username": "admin@example.com",
  "roles": ["ADMIN"]
}
```

### All subsequent requests require:
```
Authorization: Bearer <token>
X-Tenant-ID: 1
```

---

## Accounting Module

### Create Chart of Accounts
```bash
POST /api/v1/accounts
Content-Type: application/json

{
  "accountCode": "1000",
  "accountName": "Cash in Hand",
  "accountType": "ASSET",
  "accountCategory": "Current Asset",
  "description": "Cash held by organization",
  "normalBalance": "DEBIT",
  "isActive": true
}

Response: 201 Created
{
  "id": 1,
  "accountCode": "1000",
  "accountName": "Cash in Hand",
  "accountType": "ASSET",
  "currentBalance": 0.00,
  "isActive": true,
  "createdAt": "2024-02-08T10:30:00",
  "updatedAt": "2024-02-08T10:30:00"
}
```

### Get All Accounts with Pagination
```bash
GET /api/v1/accounts?page=0&size=10&sortBy=accountCode

Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "accountCode": "1000",
      "accountName": "Cash in Hand",
      ...
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 50,
    "totalPages": 5
  }
}
```

### Create Journal Entry
```bash
POST /api/v1/journal-entries
Content-Type: application/json

{
  "journalDate": "2024-02-08",
  "description": "Sales invoice INV-001",
  "referenceNumber": "INV-001",
  "entries": [
    {
      "accountId": 1,
      "debitAmount": 1000.00,
      "creditAmount": 0.00,
      "description": "Cash received"
    },
    {
      "accountId": 5,
      "debitAmount": 0.00,
      "creditAmount": 1000.00,
      "description": "Sales revenue"
    }
  ]
}

Response: 201 Created
{
  "id": 1,
  "entryNumber": "JE-20240208-0001",
  "journalDate": "2024-02-08",
  "status": "DRAFT",
  "approvalStatus": "PENDING",
  "totalDebit": 1000.00,
  "totalCredit": 1000.00,
  "isBalanced": true,
  "createdAt": "2024-02-08T10:35:00"
}
```

### Approve Journal Entry
```bash
POST /api/v1/journal-entries/1/approve
Content-Type: application/json

{
  "approverId": 2,
  "comments": "Approved - all documentation complete"
}

Response: 200 OK
{
  "id": 1,
  "entryNumber": "JE-20240208-0001",
  "status": "APPROVED",
  "approvalStatus": "APPROVED",
  "approvalComments": "Approved - all documentation complete"
}
```

### Post to General Ledger
```bash
POST /api/v1/journal-entries/1/post

Response: 200 OK
{
  "id": 1,
  "status": "POSTED",
  "postingDate": "2024-02-08",
  "message": "Journal entry posted successfully to general ledger"
}
```

### Get Trial Balance
```bash
GET /api/v1/reports/trial-balance?asOfDate=2024-02-08

Response: 200 OK
{
  "asOfDate": "2024-02-08",
  "accounts": [
    {
      "accountCode": "1000",
      "accountName": "Cash in Hand",
      "debitBalance": 5000.00,
      "creditBalance": 0.00
    },
    {
      "accountCode": "5000",
      "accountName": "Sales Revenue",
      "debitBalance": 0.00,
      "creditBalance": 3000.00
    }
  ],
  "totalDebits": 5000.00,
  "totalCredits": 3000.00,
  "isBalanced": false
}
```

---

## HR Module

### Create Department
```bash
POST /api/v1/departments
Content-Type: application/json

{
  "departmentCode": "DEPT-0001",
  "departmentName": "Finance Department",
  "description": "Financial operations and accounting",
  "budget": 500000.00,
  "costCenterCode": "CC-001",
  "isActive": true
}

Response: 201 Created
{
  "id": 1,
  "departmentCode": "DEPT-0001",
  "departmentName": "Finance Department",
  "budget": 500000.00,
  "costCenterCode": "CC-001",
  "isActive": true,
  "createdAt": "2024-02-08T11:00:00"
}
```

### Create Employee
```bash
POST /api/v1/employees
Content-Type: application/json

{
  "employeeId": "EMP-2024-0001",
  "userId": 3,
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@company.com",
  "phone": "555-0100",
  "dateOfBirth": "1990-05-15",
  "gender": "MALE",
  "departmentId": 1,
  "designation": "Finance Manager",
  "employmentType": "FULL_TIME",
  "dateOfJoining": "2024-01-15",
  "baseSalary": 60000.00,
  "salaryFrequency": "MONTHLY",
  "reportingManagerId": 2,
  "officeLocation": "New York",
  "address": "123 Main St",
  "city": "New York",
  "state": "NY",
  "postalCode": "10001",
  "country": "USA"
}

Response: 201 Created
{
  "id": 5,
  "employeeId": "EMP-2024-0001",
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@company.com",
  "department": "Finance Department",
  "designation": "Finance Manager",
  "baseSalary": 60000.00,
  "dateOfJoining": "2024-01-15",
  "isActive": true
}
```

### Record Attendance
```bash
POST /api/v1/attendance
Content-Type: application/json

{
  "employeeId": 5,
  "attendanceDate": "2024-02-08",
  "checkInTime": "09:00:00",
  "checkOutTime": "17:30:00",
  "attendanceStatus": "PRESENT",
  "workingHours": 8.5,
  "overtimeHours": 0.5,
  "remarks": "Regular working day"
}

Response: 201 Created
{
  "id": 1,
  "employeeId": 5,
  "attendancDate": "2024-02-08",
  "attendanceStatus": "PRESENT",
  "workingHours": 8.5,
  "overtimeHours": 0.5,
  "isApproved": false
}
```

### Approve Attendance
```bash
POST /api/v1/attendance/1/approve
Content-Type: application/json

{
  "approverId": 2
}

Response: 200 OK
{
  "id": 1,
  "isApproved": true,
  "approvedBy": 2,
  "message": "Attendance approved"
}
```

### Request Leave
```bash
POST /api/v1/leaves
Content-Type: application/json

{
  "employeeId": 5,
  "leaveType": "CASUAL",
  "leaveStartDate": "2024-02-12",
  "leaveEndDate": "2024-02-14",
  "numberOfDays": 3,
  "halfDay": false,
  "reason": "Personal medical appointment",
  "isPaid": true
}

Response: 201 Created
{
  "id": 1,
  "employeeId": 5,
  "leaveType": "CASUAL",
  "leaveStartDate": "2024-02-12",
  "leaveEndDate": "2024-02-14",
  "numberOfDays": 3,
  "status": "PENDING",
  "isPaid": true,
  "createdAt": "2024-02-08T11:30:00"
}
```

### Approve Leave
```bash
POST /api/v1/leaves/1/approve
Content-Type: application/json

{
  "approverId": 2,
  "comments": "Approved as casual leave balance available"
}

Response: 200 OK
{
  "id": 1,
  "status": "APPROVED",
  "approvedBy": 2,
  "approvalDate": "2024-02-08",
  "approvalComments": "Approved as casual leave balance available"
}
```

### Process Payroll
```bash
POST /api/v1/salaries/process
Content-Type: application/json

{
  "salaryMonth": "2024-02-01",
  "employeeIds": [5, 6, 7]
}

Response: 200 OK
{
  "processedCount": 3,
  "failedCount": 0,
  "message": "Payroll processed successfully",
  "salaries": [
    {
      "id": 1,
      "employeeId": 5,
      "salaryMonth": "2024-02-01",
      "basicSalary": 60000.00,
      "grossSalary": 65000.00,
      "netSalary": 52000.00,
      "paymentStatus": "PENDING"
    }
  ]
}
```

---

## Inventory Module

### Create Warehouse
```bash
POST /api/v1/warehouses
Content-Type: application/json

{
  "warehouseName": "Main Warehouse",
  "warehouseCode": "WH-0001",
  "address": "100 Industrial Way",
  "city": "Los Angeles",
  "state": "CA",
  "postalCode": "90001",
  "country": "USA",
  "phone": "555-0200",
  "email": "warehouse@company.com",
  "totalCapacity": 10000,
  "warehouseType": "PRIMARY"
}

Response: 201 Created
{
  "id": 1,
  "warehouseCode": "WH-0001",
  "warehouseName": "Main Warehouse",
  "address": "100 Industrial Way",
  "totalCapacity": 10000,
  "currentUtilization": 0,
  "isActive": true
}
```

### Create Inventory Item
```bash
POST /api/v1/inventory-items
Content-Type: application/json

{
  "sku": "PROD-001-001",
  "productId": 1,
  "warehouseId": 1,
  "quantityOnHand": 100,
  "quantityReserved": 10,
  "reorderLevel": 20,
  "reorderQuantity": 50,
  "unitCost": 25.00,
  "location": "AISLE-A-SHELF-1",
  "batchNumber": "BATCH-2024-01"
}

Response: 201 Created
{
  "id": 1,
  "sku": "PROD-001-001",
  "productId": 1,
  "quantityOnHand": 100,
  "quantityAvailable": 90,
  "reorderLevel": 20,
  "unitCost": 25.00,
  "isActive": true
}
```

### Record Stock Movement
```bash
POST /api/v1/stock-movements
Content-Type: application/json

{
  "inventoryItemId": 1,
  "movementType": "SALE",
  "quantityMoved": 10,
  "unitPrice": 25.00,
  "totalValue": 250.00,
  "movementDate": "2024-02-08",
  "referenceDocument": "INV-001",
  "referenceId": 1,
  "reason": "Customer order INV-001"
}

Response: 201 Created
{
  "id": 1,
  "inventoryItemId": 1,
  "movementType": "SALE",
  "quantityMoved": 10,
  "totalValue": 250.00,
  "movementDate": "2024-02-08",
  "status": "PENDING",
  "createdAt": "2024-02-08T12:00:00"
}
```

### Approve Stock Movement
```bash
POST /api/v1/stock-movements/1/approve
Content-Type: application/json

{
  "approverId": 2
}

Response: 200 OK
{
  "id": 1,
  "status": "APPROVED",
  "approvedBy": 2,
  "approvalDate": "2024-02-08",
  "message": "Stock movement approved and posted"
}
```

### Get Low Stock Items
```bash
GET /api/v1/inventory-items/low-stock

Response: 200 OK
{
  "content": [
    {
      "id": 5,
      "sku": "PROD-005-001",
      "productName": "Widget",
      "quantityOnHand": 15,
      "reorderLevel": 20,
      "quantityAvailable": 10,
      "status": "LOW_STOCK"
    }
  ],
  "totalCount": 5
}
```

---

## Asset Management

### Create Asset
```bash
POST /api/v1/assets
Content-Type: application/json

{
  "assetCode": "ASSET-2024-0001",
  "assetName": "Company Car - Toyota Camry",
  "assetType": "VEHICLE",
  "category": "Transport",
  "description": "Toyota Camry 2024 for business use",
  "acquisitionDate": "2024-01-15",
  "acquisitionCost": 30000.00,
  "salvageValue": 5000.00,
  "usefulLifeYears": 5,
  "depreciationMethod": "STRAIGHT_LINE",
  "location": "Parking Lot A",
  "responsiblePersonId": 5,
  "serialNumber": "ABC123456",
  "manufacturer": "Toyota",
  "warranty_expiry_date": "2026-01-15"
}

Response: 201 Created
{
  "id": 1,
  "assetCode": "ASSET-2024-0001",
  "assetName": "Company Car - Toyota Camry",
  "assetType": "VEHICLE",
  "acquisitionCost": 30000.00,
  "bookValue": 30000.00,
  "assetStatus": "IN_SERVICE",
  "createdAt": "2024-02-08T13:00:00"
}
```

### Calculate Depreciation
```bash
POST /api/v1/assets/1/depreciate
Content-Type: application/json

{
  "period": "2024-02"
}

Response: 201 Created
{
  "id": 1,
  "assetId": 1,
  "period": "2024-02",
  "depreciationAmount": 500.00,
  "accumulatedDepreciation": 500.00,
  "bookValue": 29500.00,
  "status": "POSTED"
}
```

---

## Workflow Management

### Create Workflow Definition
```bash
POST /api/v1/workflows/definitions
Content-Type: application/json

{
  "workflowName": "Invoice Approval",
  "workflowCode": "INV-APPROVAL-001",
  "processType": "INVOICE_APPROVAL",
  "requiresApproval": true,
  "maxApprovalLevels": 2,
  "timeoutDays": 5,
  "description": "Two-level approval for invoices above $10,000"
}

Response: 201 Created
{
  "id": 1,
  "workflowName": "Invoice Approval",
  "workflowCode": "INV-APPROVAL-001",
  "processType": "INVOICE_APPROVAL",
  "isActive": true,
  "createdAt": "2024-02-08T14:00:00"
}
```

### Initiate Workflow
```bash
POST /api/v1/workflows/initiate
Content-Type: application/json

{
  "workflowDefinitionId": 1,
  "entityType": "INVOICE",
  "entityId": 100,
  "initiatedBy": 2
}

Response: 201 Created
{
  "id": 1,
  "instanceNumber": "WF-20240208-0001",
  "entityType": "INVOICE",
  "entityId": 100,
  "status": "IN_PROGRESS",
  "currentStep": 1,
  "currentApproverId": 3,
  "priority": "MEDIUM",
  "dueDate": "2024-02-13T23:59:59",
  "createdAt": "2024-02-08T14:05:00"
}
```

### Approve Workflow
```bash
POST /api/v1/workflows/1/approve
Content-Type: application/json

{
  "approverId": 3,
  "comments": "Approved - invoice is valid and complete"
}

Response: 200 OK
{
  "id": 1,
  "currentStep": 2,
  "currentApproverId": 4,
  "status": "IN_PROGRESS",
  "message": "Workflow step approved, moving to next step"
}
```

---

## Notifications

### Get User Notifications
```bash
GET /api/v1/notifications?userId=5&status=UNREAD&page=0&size=10

Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "recipientId": 5,
      "notificationType": "INVOICE",
      "channel": "EMAIL",
      "subject": "Invoice INV-001 requires approval",
      "status": "PENDING",
      "sentAt": null,
      "readAt": null,
      "relatedEntityType": "INVOICE",
      "relatedEntityId": 100,
      "createdAt": "2024-02-08T14:10:00"
    }
  ],
  "totalUnread": 3
}
```

### Mark Notification as Read
```bash
POST /api/v1/notifications/1/read

Response: 200 OK
{
  "id": 1,
  "status": "DELIVERED",
  "readAt": "2024-02-08T14:15:00"
}
```

---

## Error Responses

### Validation Error
```bash
HTTP 400 Bad Request

{
  "timestamp": "2024-02-08T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Journal entry must be balanced",
  "path": "/api/v1/journal-entries"
}
```

### Unauthorized
```bash
HTTP 401 Unauthorized

{
  "timestamp": "2024-02-08T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or missing JWT token",
  "path": "/api/v1/employees"
}
```

### Forbidden
```bash
HTTP 403 Forbidden

{
  "timestamp": "2024-02-08T10:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "User does not have permission to approve invoices",
  "path": "/api/v1/invoices/1/approve"
}
```

### Not Found
```bash
HTTP 404 Not Found

{
  "timestamp": "2024-02-08T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee with ID 999 not found",
  "path": "/api/v1/employees/999"
}
```

### Rate Limit Exceeded
```bash
HTTP 429 Too Many Requests

{
  "timestamp": "2024-02-08T10:30:00",
  "status": 429,
  "error": "Too Many Requests",
  "message": "Rate limit exceeded: 20 requests per minute",
  "path": "/api/v1/employees"
}
```

---

## Response Headers

All responses include:
```
X-Request-ID: uuid-1234-5678-9012
X-RateLimit-Limit: 20
X-RateLimit-Remaining: 19
X-RateLimit-Reset: 1707396600
Content-Type: application/json
```

---

## Filtering & Searching

### Filter by Status
```bash
GET /api/v1/employees?status=ACTIVE&department=Finance
GET /api/v1/leaves?status=PENDING&leaveType=CASUAL
GET /api/v1/assets?assetStatus=IN_SERVICE&assetType=VEHICLE
```

### Date Range Filtering
```bash
GET /api/v1/salary?startMonth=2024-01-01&endMonth=2024-02-28
GET /api/v1/stock-movements?startDate=2024-01-01&endDate=2024-02-08
```

### Sorting
```bash
GET /api/v1/employees?sortBy=firstName,asc&sortBy=lastName,desc
GET /api/v1/assets?sortBy=acquisitionDate,desc
```

---

**For more details, check the API documentation at `/api/v1/swagger-ui.html`**

