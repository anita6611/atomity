# Multi-Tenant Cost Tracking API

A Spring Boot REST API for tracking daily cloud costs across multiple tenants and teams, with strict tenant isolation.

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.0**
- **Gradle 8.14** (Kotlin DSL, via Gradle Wrapper)
- **PostgreSQL 16**
- **Docker & Docker Compose**
- **Flyway** for database migrations

## Quick Start

Run the entire application with a single command:

```bash
docker compose up --build
```

The API will be available at `http://localhost:8080`

## How to Run

### Prerequisites

- Docker and Docker Compose installed
- No need for Java or Gradle installed locally

### Starting the Application

1. Clone this repository
2. Navigate to the project root
3. Run:
   ```bash
   docker compose up --build
   ```

The command will:
- Build the Spring Boot application using the Gradle Wrapper
- Start a PostgreSQL database container
- Start the API container
- Run Flyway migrations automatically
- Expose the API on port 8080

### Stopping the Application

```bash
docker compose down
```

To also remove volumes:
```bash
docker compose down -v
```

## Tenant Isolation

### How It Works

This API implements strict tenant isolation using a **required HTTP header**:

```
X-Tenant-Id: <tenant-uuid>
```

### Isolation Mechanism

1. **TenantFilter**: A Spring `OncePerRequestFilter` intercepts all HTTP requests
2. **Header Validation**:
   - Validates the `X-Tenant-Id` header is present (except for `POST /tenants`)
   - Validates the header value is a valid UUID
   - Returns `400 Bad Request` if missing or invalid
3. **TenantContext**: A `ThreadLocal` storage holds the tenant ID for the current request
4. **Service Layer Enforcement**: All queries automatically filter by tenant ID
5. **Foreign Key Validation**: Team ownership is verified before creating cost records

### Key Security Features

- Tenant ID extracted from header, never from request body
- All database queries filtered by tenant ID server-side
- Teams can only be accessed by their owning tenant
- Cost records validated against team ownership
- No cross-tenant data leakage possible

### Workflow

1. **First**, create a tenant: `POST /tenants` (no header required)
2. **Then**, use the returned `id` as `X-Tenant-Id` for all subsequent requests
3. All teams, cost records, and reports are automatically scoped to this tenant

## API Endpoints

### Create Tenant (Global Operation)

**No `X-Tenant-Id` header required**

```bash
curl -X POST http://localhost:8080/tenants \
  -H "Content-Type: application/json" \
  -d '{"name": "Acme Corp"}'
```

Response:
```json
{
  "id": "c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11",
  "name": "Acme Corp"
}
```

### Create Team

**Requires `X-Tenant-Id` header**

```bash
curl -X POST http://localhost:8080/teams \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11" \
  -d '{"name": "Platform Team"}'
```

Response:
```json
{
  "id": "b4f7f1c0-7c1d-44e9-a52e-1c3e5b4b22aa",
  "tenantId": "c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11",
  "name": "Platform Team"
}
```

### List Teams

```bash
curl -X GET http://localhost:8080/teams \
  -H "X-Tenant-Id: c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11"
```

### Submit Cost Record

```bash
curl -X POST http://localhost:8080/cost-records \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11" \
  -d '{
    "teamId": "b4f7f1c0-7c1d-44e9-a52e-1c3e5b4b22aa",
    "date": "2025-01-01",
    "costAmount": 123.45
  }'
```

### List Cost Records

```bash
curl -X GET "http://localhost:8080/cost-records?from=2025-01-01&to=2025-01-31" \
  -H "X-Tenant-Id: c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11"
```

### Get Total Cost Report

```bash
curl -X GET "http://localhost:8080/reports/total?from=2025-01-01&to=2025-01-31" \
  -H "X-Tenant-Id: c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11"
```

Response:
```json
{
  "tenantId": "c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11",
  "from": "2025-01-01",
  "to": "2025-01-31",
  "totalCost": 4567.89
}
```

### Get Cost Breakdown by Team

```bash
curl -X GET "http://localhost:8080/reports/by-team?from=2025-01-01&to=2025-01-31" \
  -H "X-Tenant-Id: c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11"
```

Response:
```json
{
  "tenantId": "c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11",
  "from": "2025-01-01",
  "to": "2025-01-31",
  "items": [
    {
      "teamId": "b4f7f1c0-7c1d-44e9-a52e-1c3e5b4b22aa",
      "teamName": "Platform Team",
      "totalCost": 3000.00
    },
    {
      "teamId": "d2a1d2f0-2db9-4e7c-9f12-91b2f7f21011",
      "teamName": "Data Team",
      "totalCost": 1567.89
    }
  ]
}
```

## HTTP Status Codes

- `200 OK` - Successful GET request
- `201 Created` - Successful POST request
- `400 Bad Request` - Missing/invalid `X-Tenant-Id`, validation errors
- `404 Not Found` - Resource not found within tenant scope
- `409 Conflict` - Duplicate resource (same tenant name, team name, or cost record date)

## Database Schema

### Tenants
- `id` (UUID, primary key)
- `name` (unique)
- `created_at`

### Teams
- `id` (UUID, primary key)
- `tenant_id` (foreign key to tenants)
- `name`
- `created_at`
- **Unique constraint**: (tenant_id, name)

### Cost Records
- `id` (UUID, primary key)
- `team_id` (foreign key to teams)
- `date` (DATE)
- `cost_amount` (DECIMAL)
- `created_at`
- **Unique constraint**: (team_id, date)

## Design Decisions

### 1. Header-Based Tenant Resolution

**Decision**: Use HTTP header `X-Tenant-Id` for tenant identification

**Rationale**:
- Clean separation between authentication metadata and request payload
- Easy to implement and test
- Follows common multi-tenancy patterns
- Allows middleware (filter) to handle tenant resolution consistently

**Trade-offs**:
- Clients must manage tenant ID
- No JWT-based authentication in this simplified version

### 2. ThreadLocal for Tenant Context

**Decision**: Store tenant ID in `ThreadLocal` within request scope

**Rationale**:
- Avoids passing tenant ID through every method signature
- Request-scoped and automatically cleaned up
- Thread-safe for concurrent requests

**Trade-offs**:
- Must ensure cleanup in filter's finally block
- Could be problematic with async processing (not used here)

### 3. UUID Primary Keys

**Decision**: Use UUID for all entity IDs

**Rationale**:
- Prevents ID guessing across tenants
- Safe for distributed systems
- PostgreSQL has native UUID support

**Trade-offs**:
- Larger storage than integers
- Slightly slower joins (acceptable for this scale)

### 4. Repository-Level Filtering

**Decision**: Enforce tenant scoping in service layer via repository queries

**Rationale**:
- Single source of truth for filtering logic
- Prevents accidental cross-tenant queries
- Easy to test and audit

**Trade-offs**:
- No framework-level row-level security (RLS)
- Developers must remember to filter by tenant

### 5. Flyway Migrations

**Decision**: Use Flyway for schema versioning

**Rationale**:
- Industry standard
- Automatic on startup
- Version-controlled schema changes
- Safe rollback capability

### 6. No Authentication/Authorization

**Decision**: Simplified to focus on tenant isolation mechanism

**Rationale**:
- Assignment focuses on multi-tenancy, not auth
- Real implementation would add JWT/OAuth2

**Trade-offs**:
- Not production-ready without auth

## Assumptions

1. **Tenant Creation**: Tenants are created manually via API (no self-registration)
2. **Date Format**: ISO-8601 date format (YYYY-MM-DD)
3. **Cost Amount**: Positive decimal values only, stored with 2 decimal places
4. **Single Currency**: No multi-currency support
5. **No Soft Deletes**: Hard deletes with cascade
6. **No Pagination**: All list endpoints return full results (would add in production)
7. **No Caching**: Direct database queries (would add Redis/Caffeine in production)

## Trade-offs & Simplifications

### What's Included
- Clean layered architecture (Controller → Service → Repository)
- Proper error handling with custom exceptions
- Bean validation for request DTOs
- Database constraints and indexes
- Docker containerization
- Health checks in docker-compose

### What's Simplified
- **No Authentication**: Real system needs JWT/OAuth2
- **No API Versioning**: Would add `/v1/` prefix
- **No Rate Limiting**: Production needs throttling
- **No Observability**: Would add Micrometer + Prometheus
- **No Integration Tests**: Included only structure, not test suite
- **No Pagination**: List endpoints return all results
- **No Soft Deletes**: Hard deletes only
- **No Audit Logging**: No created_by/updated_by tracking

## What I Would Improve with More Time

1. **Security**:
   - Add JWT-based authentication
   - Implement role-based access control (RBAC)
   - Add API key authentication for service accounts
   - Enable HTTPS/TLS

2. **Testing**:
   - Unit tests for service layer
   - Integration tests with Testcontainers
   - API contract tests
   - Load testing

3. **Observability**:
   - Structured logging (JSON) with correlation IDs
   - Metrics with Micrometer/Prometheus
   - Distributed tracing (OpenTelemetry)
   - Health/readiness endpoints

4. **Performance**:
   - Pagination for list endpoints
   - Redis caching for reports
   - Database query optimization
   - Connection pooling tuning

5. **API Improvements**:
   - HATEOAS links
   - OpenAPI/Swagger documentation
   - API versioning
   - Batch operations

6. **Reliability**:
   - Retry logic with exponential backoff
   - Circuit breakers
   - Graceful shutdown
   - Database connection resilience

7. **Developer Experience**:
   - Makefile for common operations
   - Local development profile
   - Database seeding scripts
   - Postman/Insomnia collection

8. **Production Readiness**:
   - Kubernetes manifests
   - CI/CD pipeline
   - Database backups
   - Monitoring dashboards

## Project Structure

```
.
├── src/main/java/com/atomity/costtracking/
│   ├── CostTrackingApiApplication.java
│   ├── model/                    # JPA entities
│   ├── repository/               # Spring Data JPA repositories
│   ├── service/                  # Business logic
│   ├── controller/               # REST endpoints
│   ├── dto/                      # Request/Response DTOs
│   ├── context/                  # Tenant context (ThreadLocal)
│   ├── filter/                   # Tenant header filter
│   └── exception/                # Custom exceptions & handler
├── src/main/resources/
│   ├── application.yml           # Spring configuration
│   └── db/migration/             # Flyway SQL scripts
├── build.gradle.kts              # Gradle build configuration
├── settings.gradle.kts
├── Dockerfile                    # Multi-stage build
├── docker-compose.yml            # PostgreSQL + App
└── README.md
```

## License

This is a take-home assignment project.
