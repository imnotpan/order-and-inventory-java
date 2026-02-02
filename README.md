# Order & Inventory Service (Java / Spring Boot)

RESTful backend service for managing **products** and **inventory** (available vs. reserved stock), built with a production-oriented setup and clean architecture principles.

This project is designed as a **portfolio-ready backend** for junior / trainee roles, using a stack commonly seen in Germany.

---

## Tech Stack

- Java 21  
- Spring Boot 3.5.x  
- Maven  
- PostgreSQL 16  
- Flyway (database migrations)  
- Spring Data JPA (Hibernate)  
- Testcontainers + JUnit 5  
- Docker & Docker Compose  

---

## Architecture

Layered, clean-ish architecture:

```
api/            REST controllers + request/response DTOs
application/    Use cases & services (transaction boundaries)
domain/         Entities, business rules, domain exceptions
persistence/    Spring Data repositories
common/         Global error handling and shared utilities
```

**Rules enforced:**

- Controllers contain **no business logic**
- Services do **not expose JPA entities** directly (DTOs only)
- Transactions live in the `application` layer
- Database changes are handled exclusively via Flyway migrations

---

## Features

### Products
- Create product
- List products (pagination)
- Get product by ID
- Reject duplicate SKUs (409 Conflict)

### Inventory
- Inventory record automatically created when a product is created
- Restock products
- Reserve stock (available → reserved)
- Reject over-reservation without modifying database state

### Error Handling
Consistent JSON error responses using `@RestControllerAdvice`:

- Validation errors (400)
- Resource not found (404)
- Business conflicts (409)
- Unexpected errors (500)

---

## API Endpoints

### Products

#### Create Product
`POST /api/v1/products`

Request:
```json
{
  "sku": "SKU-001",
  "name": "Keyboard",
  "price": 99.99
}
```

Errors:

- 400 validation error
- 409 if SKU already exists

Response **201 Created**:
```json
{
  "id": 1,
  "sku": "SKU-001",
  "name": "Keyboard",
  "price": 99.99,
  "active": true
}
```

#### List Products (Paginated)
`GET /api/v1/products?page=0&size=10`

```json
{
  "content": [
    {
      "id": 1,
      "sku": "SKU-001",
      "name": "Keyboard",
      "price": 99.99,
      "active": true
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

#### Get Product by ID
`GET /api/v1/products/{id}`
Response **200 OK**:

```json
{
  "id": 1,
  "sku": "SKU-001",
  "name": "Keyboard",
  "price": 99.99,
  "active": true
}
```
Errors:

- 404 if product not found

---

### Inventory

> `quantity` must be a positive integer (`quantity > 0`), otherwise **400 Bad Request**.


#### Get inventory by SKU
`GET /api/v1/inventory/{sku}`
Response **200 OK**:

```json
{
  "sku": "SKU-001",
  "availableQuantity": 10,
  "reservedQuantity": 6
}
```
Errors:

- 404 if product/inventory not found (depending on your domain rules)

#### Restock Stock
`POST /api/v1/inventory/{sku}/restock?quantity=10`

Response: **204 No Content**

- 400 if quantity invalid
- 404 if product/inventory not found

#### Reserve Stock
`POST /api/v1/inventory/{sku}/reserve?quantity=6`

Response: **204 No Content**

- 400 if quantity invalid
- 404 if product/inventory not found
- 409 Conflict if insufficient stock


---

## Error Response Format

```json
{
  "code": "INSUFFICIENT_STOCK",
  "message": "Insufficient stock for product sku {sku} (requested=10, available=5)",
  "timestamp": "ISO-8601",
  "path": "/api/v1/inventory/{sku}/reserve"
}
```

### Common Error Codes

- `VALIDATION_ERROR`
- `PRODUCT_ALREADY_EXISTS`
- `PRODUCT_NOT_FOUND`
- `INVENTORY_NOT_FOUND`
- `INSUFFICIENT_STOCK`
- `UNEXPECTED_ERROR`

---

## Local Setup

### Requirements
- Java 21  
- Maven 3.9+  
- Docker Desktop  

### Start PostgreSQL
From project root:
```bash
docker compose up -d
```

### Run the Application
```bash
mvn spring-boot:run
```

Application runs at:
```
http://localhost:8080
```

---

## Database Migrations (Flyway)

Migration scripts:
```
src/main/resources/db/migration
```

Initial schema:
```
V1__init.sql
```

Flyway runs automatically on application startup.

---

## Running Tests

```bash
mvn test
```

- Includes unit and integration tests
- Integration tests use **Testcontainers**
- No local PostgreSQL required for tests

---

## Quick Manual Tests (curl)

Create product:
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{"sku":"SKU-001","name":"Keyboard","price":99.99}'
```

List products:
```bash
curl "http://localhost:8080/api/v1/products?page=0&size=10"
```

Get inventory:
```bash
curl "http://localhost:8080/api/v1/inventory/SKU-001"
```

Restock stock:
```bash
curl -X POST "http://localhost:8080/api/v1/inventory/SKU-001/restock?quantity=10"
```

Reserve stock:
```bash
curl -X POST "http://localhost:8080/api/v1/inventory/SKU-001/reserve?quantity=6"
```

---

## Project TODO (Next Steps)

- Orders domain
  - Create order with order lines
  - Transactional stock reservation
  - Order state machine (CREATED → PAID → SHIPPED / CANCELLED)
- Idempotency key for order creation
- More integration tests (orders + inventory)
- Swagger / OpenAPI documentation
- CI pipeline (GitHub Actions)

---

## License

MIT
