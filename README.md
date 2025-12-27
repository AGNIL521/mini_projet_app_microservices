# Mini-Projet: Secure Microservices E-Commerce Platform

## Overview
Mini-Projet is a secure, modular, and containerized e-commerce application built with industry best practices. It features a microservices architecture, centralized API Gateway, and robust security using Keycloak (OAuth2/OpenID Connect).

## Architecture
- **Microservices**: Independent Product and Order services (Spring Boot)
- **API Gateway**: Spring Cloud Gateway (centralized entry point, JWT validation, security enforcement)
- **Frontend**: React (role-based UI, no direct microservice access)
- **Security**: Keycloak for authentication/authorization (OAuth2, OpenID Connect)
- **Databases**: PostgreSQL per service (no shared DB)
- **Infrastructure**: Docker & Docker Compose (all components)

```
[React Frontend] ⇄ [API Gateway] ⇄ [Product Service]
                                    ⇄ [Order Service]
                                    ⇄ [Keycloak]
```

## Services
### API Gateway
- Routing, JWT validation, centralized security
- No business logic

### Product Service
- **Entities**: ID, Name, Description, Price, Quantity
- **Features**: Catalogue management
- **Access**:
    - ADMIN: Add/Edit/Delete
    - ADMIN/CLIENT: List/View

### Order Service
- **Entities**: ID, Date, Status, Total, Product List (ID, Qty, Price)
- **Features**: Auto-calculate total, verify product availability
- **Access**:
    - CLIENT: Create/View own orders
    - ADMIN: List all orders
- **Inter-service**: REST call to Product Service for stock/price (JWT propagated)

## Security & Keycloak
- Roles: ADMIN, CLIENT
- Security enforced at Gateway and Service levels
- Frontend handles JWT session, role-based UI, and 401/403 gracefully

## DevSecOps & Quality
- **CI/CD**: Integrate SonarQube (static analysis), OWASP Dependency-Check, Trivy (image scanning)
- **Logging**: Centralized, includes User IDs, tracks access/errors

## Infrastructure
- **Docker Compose**: Orchestrates all services
- **Keycloak**: Runs as a container, pre-configured for roles
- **PostgreSQL**: Dedicated container per service

## Getting Started
1. Clone the repo
2. Build and start all services with Docker Compose:
   ```sh
   docker-compose up --build
   ```
3. Access Keycloak at [http://localhost:8080](http://localhost:8080)
   - Username: `admin` (ADMIN), `client` (CLIENT)
   - Password: same as username
4. Frontend at [http://localhost:3000](http://localhost:3000)

## API Endpoints
- **API Gateway**: [http://localhost:8888]
  - `/api/products/**` → Product Service
  - `/api/orders/**` → Order Service
- **Product Service**: [http://localhost:8081/api/products]
  - `GET /` (ADMIN, CLIENT): List all products
  - `GET /{id}` (ADMIN, CLIENT): Get product by ID
  - `POST /` (ADMIN): Create product
  - `PUT /{id}` (ADMIN): Update product
  - `DELETE /{id}` (ADMIN): Delete product
- **Order Service**: [http://localhost:8082/api/orders]
  - `GET /` (ADMIN): List all orders
  - `GET /my` (CLIENT): List own orders
  - `POST /` (CLIENT): Create order (stock checked)

## Environment & Configuration
- All services configured in `docker-compose.yml`
- Keycloak realm auto-import via `keycloak-realm-export.json`
- Health checks: `/actuator/health` on all Spring Boot services

## Notes
- All inter-service calls propagate JWT for authorization
- Centralized logging includes user IDs
- CI/CD: SonarQube, OWASP Dependency-Check, Trivy enabled in GitHub Actions

---

> This project is designed for academic and industrial-grade security, modularity, and maintainability.