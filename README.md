# SpringERP — Enterprise Resource Planning System

A full-stack, industry-standard ERP system built with **Spring Boot** (backend) and **React + TypeScript** (frontend), organized as a monorepo.

---

## Project Structure

```
SpringERP/
├── backend/                       ← Spring Boot Application (Java 17)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/springerp/
│   │   │   │   ├── config/        # Security, CORS, Cache, Rate-Limiting, OpenAPI
│   │   │   │   ├── context/       # Multi-tenancy / Company context holders
│   │   │   │   ├── controller/    # REST API endpoints
│   │   │   │   ├── dto/           # Data Transfer Objects
│   │   │   │   ├── entity/        # JPA Entities
│   │   │   │   ├── exception/     # Custom exceptions & global handler
│   │   │   │   ├── repository/    # Spring Data JPA repositories
│   │   │   │   ├── security/      # JWT filter, UserDetailsService
│   │   │   │   ├── service/       # Business logic (interfaces + impls)
│   │   │   │   └── util/          # JWT utilities, helpers
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-prod.properties
│   │   │       └── db/migration/  # Flyway SQL migrations
│   │   └── test/                  # Unit tests (JUnit 5 + Mockito)
│   ├── Dockerfile
│   ├── pom.xml
│   └── .gitignore
│
├── frontend/                      ← React + TypeScript (Vite)
│   ├── src/
│   │   ├── api/                   # Axios service layer
│   │   ├── components/
│   │   │   ├── common/            # Reusable UI: Button, Table, Modal, Badge…
│   │   │   └── layout/            # Sidebar, Topbar, AppLayout
│   │   ├── features/              # Domain pages
│   │   │   ├── auth/              # Login
│   │   │   ├── dashboard/
│   │   │   ├── customers/
│   │   │   ├── suppliers/
│   │   │   ├── products/
│   │   │   ├── invoices/
│   │   │   ├── employees/
│   │   │   ├── accounting/
│   │   │   └── inventory/         # ← NEW: Inventory management
│   │   ├── hooks/                 # Custom React Query hooks
│   │   ├── store/                 # Zustand auth store
│   │   ├── types/                 # Shared TypeScript interfaces
│   │   └── App.tsx
│   ├── Dockerfile.frontend
│   ├── package.json
│   ├── tsconfig.json
│   └── .gitignore
│
├── .github/
│   └── workflows/
│       └── ci-cd.yml              # CI/CD: test → build → Docker push
├── docker-compose.yml             # Orchestrate DB + Backend + Frontend
├── .env.example
└── README.md
```

---

## Technology Stack

| Layer      | Technology                                       |
|------------|--------------------------------------------------|
| Backend    | Java 17, Spring Boot 3.2, Spring Security, JWT   |
| ORM        | Spring Data JPA, Hibernate, Flyway migrations    |
| Database   | MySQL 8.0                                        |
| Cache      | Caffeine (local), Redis-ready                    |
| Docs       | SpringDoc OpenAPI (Swagger UI)                   |
| Frontend   | React 18, TypeScript, Vite                       |
| UI         | Tailwind CSS                                     |
| State      | Zustand (auth), TanStack Query (server state)    |
| Forms      | React Hook Form + Zod validation                 |
| HTTP       | Axios                                            |
| Container  | Docker, Docker Compose                           |
| CI/CD      | GitHub Actions                                   |

---

## Quick Start

### Prerequisites
- Java 17 (OpenJDK 17 at `/usr/lib/jvm/java-17-openjdk-amd64`)
- Node.js 18+ and npm
- Maven 3.8+

> **Note:** The system Java may be a newer version. The build scripts use Java 17 explicitly.

---

### ▶ Option 1 — One Command (Recommended)

```bash
cd SpringERP
./start.sh
```

This single script will:
1. Build the Spring Boot jar (skipping tests)
2. Start the backend on **port 8090**
3. Start the Vite dev server on **port 3000**
4. Print all URLs and credentials
5. Shut down both services cleanly on `Ctrl+C`

**URLs after startup:**

| Service    | URL |
|------------|-----|
| Frontend   | http://localhost:3000 |
| Backend API | http://localhost:8090/api/v1 |
| Swagger UI | http://localhost:8090/api/v1/swagger-ui.html |
| H2 Console | http://localhost:8090/api/v1/h2-console |

**Default login:** `admin@springerp.com` / `Admin@123`

---

### ▶ Option 2 — Run Services Separately

**Terminal 1 — Backend:**
```bash
cd SpringERP/backend
JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 mvn package -DskipTests
/usr/lib/jvm/java-17-openjdk-amd64/bin/java \
  -jar target/spring-erp.jar \
  --spring.profiles.active=local \
  --server.port=8090 \
  --jwt.secret=springerpsecretkeywith512bitsforhs512algorithm12345678901234 \
  --app.jwt.expiration-milliseconds=86400000
```

**Terminal 2 — Frontend:**
```bash
cd SpringERP/frontend
npm install          # first time only
VITE_API_BASE_URL=http://localhost:8090/api/v1 npm run dev -- --port 3000
```

---

### ▶ Option 3 — Docker Compose (MySQL production-like)

```bash
cd SpringERP
docker-compose up --build
```
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api/v1
- Swagger UI: http://localhost:8080/api/v1/swagger-ui.html

---

### First-Time Setup (register admin user)

The local H2 database starts empty. Register the admin once:
```bash
curl -X POST http://localhost:8090/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@springerp.com",
    "password": "Admin@123",
    "dateOfBirth": "1990-01-01",
    "role": "ADMIN",
    "phoneNumber": "0123456789",
    "address": "123 Main Street"
  }'
```

---

## API Modules

| Module       | Base Path              |
|--------------|------------------------|
| Auth         | `/api/v1/auth`         |
| Customers    | `/api/v1/customers`    |
| Suppliers    | `/api/v1/suppliers`    |
| Products     | `/api/v1/products`     |
| Invoices     | `/api/v1/invoices`     |
| Employees    | `/api/v1/employees`    |
| Accounting   | `/api/v1/accounting`   |
| Inventory    | `/api/v1/inventory`    |
| Companies    | `/api/v1/admin/companies` |

---

## Running Tests

```bash
# Backend unit tests
cd backend
mvn test

# Frontend type check
cd frontend
npx tsc --noEmit
npm run build
```

---

## Environment Variables

See `.env.example` for all required variables.

| Variable          | Description                      |
|-------------------|----------------------------------|
| `MYSQL_URL`       | JDBC connection string           |
| `MYSQL_USERNAME`  | Database username                |
| `MYSQL_PASSWORD`  | Database password                |
| `JWT_SECRET`      | 64+ char secret for JWT signing  |
| `VITE_API_BASE_URL` | Frontend API base URL          |

---

## License
MIT


`SpringERP` is a Java-based enterprise resource planning (ERP) system designed to manage and automate various business processes. It leverages the Spring Framework to provide a robust, scalable, and modular architecture, enabling seamless integration with other systems and easy customization to meet specific business needs.

![Java](https://img.shields.io/badge/Java-17+-blue?logo=java&style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen?logo=springboot&style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)
![Spring Security](https://img.shields.io/badge/Spring%20Security-5.7.3-brightgreen?logo=springsecurity&style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.8.4-orange?logo=apachemaven&style=flat-square)
![Docker](https://img.shields.io/badge/Docker-20.10.7-blue.svg)
![GitHub](https://img.shields.io/badge/GitHub-Repo-blue?logo=github&style=flat-square)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)
---

## Table of Contents

- [Features](#features)
- [Performance Optimizations](#performance-optimizations)
- [API Endpoints](#api-endpoints)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [Screenshots](#screenshots)
- [Additional Resources](#additional-resources)
- [Contributing](#contributing)
- [License](#license)
- [Author](#author)

---

## Features

- **User Authentication**: Secure user authentication using JWT.
- **Rate Limiting**: Implement rate limiting to control API usage.
- **Caching**: Utilize Caffeine for efficient caching of data.
- **Response Compression**: Enable response compression for better performance.
- **User Management**: Manage user roles and permissions.
- **Product Management**: Add, update, and delete products.
- **Category Management**: Organize products into categories with a one-to-many relationship.
- **Inventory Management**: Track and manage inventory levels efficiently.
- **Order Processing**: Streamline order management and fulfillment.
- **Customer Relationship Management (CRM)**: Manage customer interactions and data.
- **Financial Accounting**: Automate financial transactions and reporting.
- **Reporting and Analytics**: Generate reports and analyze business performance.

### Customer Management
- Customer profile management
- Customer interaction tracking
- Order history and management
- Customer communication logs

### Supplier Management
- Supplier profile management
- Purchase order tracking
- Supplier interaction history
- Supply chain management

### Order Processing
- Order creation and management
- Order status tracking
- Multiple order items support
- Order history and analytics

### Purchase Management
- Purchase order creation
- Supplier-specific purchase tracking
- Purchase order items management
- Delivery tracking

### Common Features
- Audit tracking (creation and modification timestamps)
- Relationship management between entities
- Data validation and integrity
- Scalable data model

## Technology Stack

### Backend
- Java 21
- Spring Boot 3.2.3
- Spring Data JPA
- Jakarta Persistence (JPA)
- Lombok
- ModelMapper
- MySQL 8.0
- Caffeine
- JWT
- Spring Security
- Bucket4j

### Data Model Updates
- Multi-company architecture
- Company-based data segregation
- Cross-company relationship management
- Company-specific configuration management
- Company-based invoicing and order tracking
- Company-customer relationship management

## Performance Optimizations

- **Caching Strategy**:
   - Caffeine cache with 500 max entries
   - 60-minute expiration time
   - Cache statistics monitoring
   - Smart cache key generation using authentication context

- **Rate Limiting**:
   - Token bucket algorithm using Bucket4j
   - 20 requests per minute per endpoint
   - Rate limit headers in responses

- **Response Optimization**:
   - Compression enabled for JSON/XML responses
   - 1KB minimum compression threshold
   - Optimized cache headers

- **Database Optimization**:
   - MySQL 8.0
   - Optimized table structures
   - Indexes for performance
---

## API Endpoints

### Company Management
- Company profile management
- Multiple company support
- Company-specific settings
- Company-customer relationship management
- Company-based invoicing
- Company-specific order tracking

### Company Management
- `GET /api/v1/companies` - List all companies with pagination
  - Parameters:
    - `page` (optional, default: 0) - Page number
    - `size` (optional, default: 10) - Items per page
    - `sortBy` (optional, default: "id") - Field to sort by
  - Response: List of companies with pagination

- `GET /api/v1/companies/{id}` - Get company by ID
  - Response: Single company details with associated customers, orders, and invoices

- `POST /api/v1/companies` - Create new company
  - Request body: Company details including registration info
  - Response: Created company details

- `PUT /api/v1/companies/{id}` - Update company
  - Request body: Updated company details
  - Response: Updated company information

- `DELETE /api/v1/companies/{id}` - Delete company
  - Response: 204 No Content

- `GET /api/v1/companies/search` - Search companies
  - Parameters:
    - `query`: Search term for company name or registration number
  - Response: List of matching companies


### User Management
- **Create User**: `POST /users`
- **Update User**: `PUT /users/{id}`
- **Delete User**: `DELETE /users/{id}`
- **Get User by ID**: `GET /users/{id}`
- **Get All Users**: `GET /users`

### Authentication
- **Register**: `POST /register`
- **Login**: `POST /login`
- **Logout**: `POST /logout`
- **Refresh Token**: `POST /refresh-token`
- **Forgot Password**: `POST /forgot-password`

### Product Management
- **Create Product**: `POST /products`
- **Update Product**: `PUT /products/{id}`
- **Delete Product**: `DELETE /products/{id}`
- **Get Product by ID**: `GET /products/{id}`
- **Get All Products**: `GET /products`

### Category Management
- **Create Category**: `POST /categories`
- **Update Category**: `PUT /categories/{id}`
- **Delete Category**: `DELETE /categories/{id}`
- **Get Category by ID**: `GET /categories/{id}`
- **Get All Categories**: `GET /categories`

### Inventory Management
- **Create Inventory**: `POST /inventories`
- **Update Inventory**: `PUT /inventories/{id}`
- **Delete Inventory**: `DELETE /inventories/{id}`
- **Get Inventory by ID**: `GET /inventories/{id}`
- **Get All Inventories**: `GET /inventories`

### Order Management
- **Create Order**: `POST /orders`
- **Update Order**: `PUT /orders/{id}`
- **Delete Order**: `DELETE /orders/{id}`
- **Get Order by ID**: `GET /orders/{id}`
- **Get All Orders**: `GET /orders`

### Customer Endpoints
- `GET /api/customers` - List all customers
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

### Supplier Endpoints
- `GET /api/suppliers` - List all suppliers
- `GET /api/suppliers/{id}` - Get supplier by ID
- `POST /api/suppliers` - Create new supplier
- `PUT /api/suppliers/{id}` - Update supplier
- `DELETE /api/suppliers/{id}` - Delete supplier

### Order Endpoints
- `GET /api/orders` - List all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create new order
- `PUT /api/orders/{id}` - Update order
- `DELETE /api/orders/{id}` - Delete order

### Purchase Order Endpoints
- `GET /api/purchase-orders` - List all purchase orders
- `GET /api/purchase-orders/{id}` - Get purchase order by ID
- `POST /api/purchase-orders` - Create new purchase order
- `PUT /api/purchase-orders/{id}` - Update purchase order
- `DELETE /api/purchase-orders/{id}` - Delete purchase order

### Invoice Endpoints
- `GET /api/v1/invoices` - List all invoices with pagination
  - Parameters:
    - `page` (optional, default: 0)—Page number
    - `size` (optional, default: 10) - Items per page
    - `sortBy` (optional, default: "id")—Field to sort by
  - Response: List of invoices with pagination

- `GET /api/v1/invoices/{id}` - Get invoice by ID
  - Response: Single invoice details

- `POST /api/v1/invoices` - Create new invoice
  - Request body: Invoice details including items
  - Response: Created invoice with generated invoice number

- `PUT /api/v1/invoices/{id}` - Update invoice
  - Request body: Updated invoice details
  - Response: Updated invoice information

- `DELETE /api/v1/invoices/{id}` - Delete invoice
  - Response: 204 No Content

- `GET /api/v1/invoices/status/{status}` - Get invoices by status
  - Parameters:
    - `status`: DRAFT, SENT, PAID, OVERDUE, CANCELLED, VOID
  - Response: List of invoices with a specified status

- `PATCH /api/v1/invoices/{id}/status` - Update invoice status
  - Parameters:
    - `status`: New status value
  - Response: Updated invoice with new status

- `GET /api/v1/invoices/{id}/pdf` - Generate PDF for invoice
  - Response: PDF file download

---

## Getting Started

### Prerequisites

Ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 11 or higher (Recommended: JDK 17)
- **Apache Maven**: For building the project
- **Git**: For version control

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/AronnoDIU/SpringERP.git
    cd SpringERP
    ```

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

---

## Usage

To start the application, use the following command:
```sh
mvn spring-boot:run
```

Once the application is running, access it at:
```
http://localhost:8080/api/v1
```

---

## Screenshots

### Dashboard
![Dashboard Screenshot](https://via.placeholder.com/800x400?text=Dashboard+Screenshot)

### Inventory Management
![Inventory Screenshot](https://via.placeholder.com/800x400?text=Inventory+Management+Screenshot)

---

## Additional Resources

- [Spring Framework Documentation](https://spring.io/)
- [Apache Maven Documentation](https://maven.apache.org/)
- [Git Documentation](https://git-scm.com/)

---

## Contributing

Contributions are welcome! Please follow the [Contributing Guidelines](CONTRIBUTING.md) to contribute to this project.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## License

This project is licensed under the [MIT License](LICENSE).

---

## Support

For support, please open an issue in the GitHub issue tracker or contact the development team.
Built with ❤️ using Spring Boot and Java

## Author

Developed by [Yeasir Arafat Aronno](https://github.com/AronnoDIU). Feel free to reach out for collaboration or feedback!

---