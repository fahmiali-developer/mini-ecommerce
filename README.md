# Mini E-Commerce Microservices

Independent Spring Boot 2.7.18 / Java 8 Maven projects.

## Services

| Service | Port | Database |
|---|---:|---|
| API Gateway | 8080 | - |
| User Service | 8081 | user_service_db |
| Product Service | 8082 | product_service_db |
| Cart Service | 8083 | cart_service_db |
| Order Service | 8084 | order_service_db |
| Payment Service | 8085 | payment_service_db |
| Inventory Service | 8086 | inventory_service_db |
| Notification Service | 8087 | notification_service_db |

## Database setup

Run `database/init.sql` against your local MySQL 8 server.

Default local datasource settings use username `root` and password `root`.
Override them with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables.

## Build and test

From each service directory:

```bash
mvn clean test
```

The initial tests are Spring context smoke tests. Add focused JUnit 5 / Mockito unit tests with each feature.

## Important

- User Service includes Spring Security and JJWT dependencies, but registration/login/JWT endpoints are not implemented yet.
- API Gateway routes are configured, but JWT validation/security filters are not implemented yet.
- Each service has its own database schema. Services must not directly access another service's database.
- This is a learning scaffold, not a production-ready deployment.
| Promotion Service | 8088 | promotion_service_db |
| Shipping Service | 8089 | shipping_service_db |
| Review Service | 8090 | review_service_db |
| Wishlist Service | 8091 | wishlist_service_db |
