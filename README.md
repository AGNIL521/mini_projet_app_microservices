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
- **Docker Compose**: Orchestrates all services with proper networking and volumes
- **Keycloak**: Version 23.0.0, pre-configured realm `miniprojet-realm`
- **PostgreSQL**: Dedicated containers with persistent volumes
  - `postgres-product`: Product service database
  - `postgres-order`: Order service database
- **Networking**: Custom bridge network `miniprojet-net` for service communication

## Getting Started
1. Clone the repo
2. Build and start all services with Docker Compose:
   ```sh
   docker-compose up --build
   ```
3. Access Keycloak at [http://localhost:8080](http://localhost:8080)
   - Realm: `miniprojet-realm`
   - Username: `admin` (ADMIN), `client` (CLIENT)
   - Password: same as username
4. Frontend at [http://localhost:3000](http://localhost:3000)
5. API Gateway at [http://localhost:8888](http://localhost:8888)

### Troubleshooting
- **Port 8080 already in use**: Stop any services using port 8080 (e.g., other Java applications) before running docker-compose
- **Service startup order**: Services depend on Keycloak and databases - ensure they start completely

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
### Docker Compose Services
- **Keycloak**: Uses realm `miniprojet-realm` with ADMIN/CLIENT roles
- **Product Service**: 
  - Database: PostgreSQL on `postgres-product:5432`
  - Environment: `SPRING_PROFILES_ACTIVE=docker`
- **Order Service**: 
  - Database: PostgreSQL on `postgres-order:5432`
  - Environment: `SPRING_PROFILES_ACTIVE=docker`
- **API Gateway**: JWT validation with Keycloak issuer URI
- **Frontend**: React app with API URL pointing to Gateway

### Security Configuration
All services configured with:
- `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://keycloak:8080/realms/miniprojet-realm`
- Role-based access control at service and gateway levels

### Database Configuration
- Product DB: `product_db` with user `user_product`
- Order DB: `order_db` with user `user_order`
- Persistent volumes for data retention

## Notes
- All inter-service calls propagate JWT for authorization
- Centralized logging includes user IDs
- CI/CD: SonarQube, OWASP Dependency-Check, Trivy enabled in GitHub Actions

---

> This project is designed for academic and industrial-grade security, modularity, and maintainability.