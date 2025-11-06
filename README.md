# Smartventory — Real‑Time Inventory & Sales Dashboard
# ONGOING- v3.0
Real‑time inventory tracking and sales console built with Spring Boot 3.5, JPA (PostgreSQL), WebSocket/STOMP, and Redis caching. A simple static UI (HTML/CSS/JS) consumes REST APIs and live stock updates.

- Backend: Spring Boot 3, REST, JPA, WebSocket (STOMP), Validation, Actuator, Redis
- Frontend: Static assets with SockJS + STOMP for live updates
- Live updates: Clients subscribe to `/topic/inventory-updates` and receive updates after each sale

## Project Structure

- `src/main/java/com/inventoryapp/inventory_system/InventorySystemApplication.java` — Spring Boot entry point
- `controller/InventoryController.java` — REST API endpoints
- `service/InventoryService.java`, `service/InventoryServiceImpl.java` — business logic
- `config/WebSocketConfig.java` — STOMP WebSocket endpoint `/ws-inventory`, topic `/topic`
- `config/RedisConfig.java` — Redis cache manager and JSON serialization
- `model/Product.java`, `model/SaleTransaction.java` — domain entities
- `repository/ProductRepository.java`, `repository/SaleTransactionRepository.java` — data access
- `dto/ProductRequest.java`, `dto/SaleRequest.java` — request DTOs
- `exception/GlobalExceptionHandler.java` — API error responses
- `resources/static/` — UI (`index.html`, `js/app.js`, `css/styles.css`)
- `resources/application.properties` — port, DB, and actuator settings
- `resources/data.sql` — optional seed data

## Features

- Add/list products via REST
- Process sales with validation and transaction logging
- Redis‑backed cache for product list (`@Cacheable("products")`)
- Real‑time stock updates broadcast to all connected clients
- Health/metrics via Spring Boot Actuator

## Requirements

- JDK 21
- PostgreSQL (configured at `localhost:5435`) with user `postgres` and password `mysecretpassword`
- Redis (default `localhost:6379`)

## Configuration

`src/main/resources/application.properties` (defaults):

- Server: `server.port=8080`
- Postgres: `spring.datasource.url=jdbc:postgresql://localhost:5435/postgres`
- JPA: `spring.jpa.hibernate.ddl-auto=update`, `spring.jpa.show-sql=true`
- Actuator: `management.endpoints.web.exposure.include=health,metrics,info`

Optional H2 profile snippets are present but commented out.

## Run Locally

Using the Maven Wrapper:

```powershell
# from the repo root (Windows PowerShell)
./mvnw.cmd spring-boot:run
```

Open the UI:

- <http://localhost:8080/>

### Docker quick start (optional)

```powershell
# PostgreSQL 16
docker run -d --name pg -e POSTGRES_PASSWORD=mysecretpassword -p 5435:5432 postgres:16

# Redis 7
docker run -d --name redis -p 6379:6379 redis:7
```

## API Overview

Base URL: `http://localhost:8080`

- GET `/api/inventory` — list all products
- POST `/api/inventory` — create a product
  - Body example:
  
  ```json
  { "name": "Wireless Mouse", "sku": "WM123", "stockQuantity": 150, "price": 25.99 }
  ```
- POST `/api/inventory/sale` — record a sale, returns updated product or 400/404 on errors
  - Body example:
  
  ```json
  { "sku": "WM123", "quantitySold": 2 }
  ```

## Real‑Time Updates

- STOMP endpoint: `/ws-inventory`
- Topic: `/topic/inventory-updates`
- See `resources/static/js/app.js` for the SockJS/STOMP client usage.

## Testing

```powershell
./mvnw.cmd test
```

## Notes

- Cache serialization uses a configured `ObjectMapper` to include type info for safe JSON deserialization in Redis (`RedisConfig`).
- Business and validation errors are centralized in `GlobalExceptionHandler`.
- If you prefer in‑memory DB for quick demos, uncomment H2 settings in `application.properties` and comment out Postgres.
