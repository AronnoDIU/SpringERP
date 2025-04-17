# SpringERP

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

---

## License

This project is licensed under the [MIT License](LICENSE).

---

## Author

Developed by [Yeasir Arafat Aronno](https://github.com/AronnoDIU). Feel free to reach out for collaboration or feedback!

---