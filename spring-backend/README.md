Spring backend (migrado desde Quarkus) - OpenAPI

Cómo levantar con Docker Compose

1. Definir variables de entorno (puedes crear un archivo .env en el directorio):

POSTGRES_DB=delivery
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_PORT=5432
SPRING_BACKEND_PORT=8080

2. Construir y levantar:

```bash
# desde el directorio spring-backend
docker compose up --build
```

3. Swagger UI estará disponible en:

http://localhost:8080/api/swagger-ui.html

(Nota: el proyecto expone el OpenAPI estático en /api/v3/api-docs.yaml y Swagger UI carga ese documento)

Liquibase
---------

El proyecto usa Liquibase para ejecutar migraciones al iniciar la aplicación (puedes deshabilitarlo con la variable LIQUIBASE_ENABLED=false).

Los changelogs están en `src/main/resources/db/changelog/` y los scripts SQL en `src/main/resources/db/changelog/sql/`.

Al levantar con `docker compose up --build` Liquibase ejecutará los scripts y creará la tabla `restaurants` con datos de prueba.

Inventario de entidades y endpoints
----------------------------------

Usa esta lista como checklist maestro. Cada ítem representa una entidad (tabla) detectada en `db/changelog/schema/001-schema-create-core-tables.sql` y los endpoints CRUD asociados. Marca la casilla cuando implementes la entidad completa (JPA entity, repository, service, controller, DTOs/mappers y endpoints CRUD).

- [ ] addresses
 - [x] addresses
  - [x] GET /addresses
  - [x] GET /addresses/{id}
  - [x] POST /addresses
  - [x] PUT /addresses/{id}
  - [x] DELETE /addresses/{id}
- [x] audit_logs
  - [x] GET /audit-logs
- [ ] courier_reviews
  - [ ] GET /courier-reviews
  - [ ] POST /courier-reviews
- [ ] couriers
  - [ ] GET /couriers
  - [ ] GET /couriers/{id}
  - [ ] POST /couriers
  - [ ] PUT /couriers/{id}
  - [ ] DELETE /couriers/{id}
- [x] dish_availability
  - [x] GET /dish-availability
  - [x] POST /dish-availability
  - [x] DELETE /dish-availability/{id}
- [ ] dish_option_values
  - [ ] GET /dish-option-values
  - [ ] POST /dish-option-values
  - [ ] DELETE /dish-option-values/{id}
- [ ] dish_options
  - [ ] GET /dish-options
  - [ ] POST /dish-options
  - [ ] DELETE /dish-options/{id}
- [ ] dish_reviews
  - [ ] GET /dish-reviews
  - [ ] POST /dish-reviews
- [ ] dishes
  - [x] GET /dishes
  - [x] GET /dishes/{id}
  - [x] POST /dishes
  - [x] PUT /dishes/{id}
  - [x] DELETE /dishes/{id}
- [ ] menu_categories
  - [ ] GET /menu-categories
  - [ ] GET /menu-categories/{id}
  - [ ] POST /menu-categories
  - [ ] PUT /menu-categories/{id}
  - [ ] DELETE /menu-categories/{id}
- [ ] notifications
  - [ ] GET /notifications
  - [ ] POST /notifications
- [ ] notification_tokens
  - [ ] GET /notification-tokens
  - [ ] POST /notification-tokens
- [ ] order_issues
  - [ ] GET /order-issues
  - [ ] POST /order-issues
  - [ ] DELETE /order-issues/{id}
- [x] order_items
  - [x] GET /order-items
  - [x] POST /order-items
  - [x] DELETE /order-items/{id}
- [ ] order_status_history
  - [ ] GET /order-status-history
  - [ ] POST /order-status-history
- [ ] order_tracking
  - [ ] GET /order-tracking
  - [ ] POST /order-tracking
 - [x] orders
  - [x] GET /orders
  - [x] GET /orders/{id}
  - [x] POST /orders
  - [x] PUT /orders/{id}
  - [x] DELETE /orders/{id}
- [ ] payment_methods
  - [ ] GET /payment-methods
  - [ ] POST /payment-methods
  - [ ] DELETE /payment-methods/{id}
- [ ] payouts
  - [ ] GET /payouts
  - [ ] POST /payouts
- [ ] promotions
  - [ ] GET /promotions
  - [ ] POST /promotions
  - [ ] PUT /promotions/{id}
  - [ ] DELETE /promotions/{id}
- [ ] referral_codes
  - [ ] GET /referral-codes
  - [ ] POST /referral-codes
- [ ] restaurant_closures
  - [ ] GET /restaurant-closures
  - [ ] POST /restaurant-closures
  - [ ] DELETE /restaurant-closures/{id}
- [ ] restaurant_cuisines
  - [ ] GET /restaurant-cuisines
  - [ ] POST /restaurant-cuisines
  - [ ] DELETE /restaurant-cuisines/{restaurantId}/{cuisineId}
- [ ] restaurant_daily_stats
  - [ ] GET /restaurant-daily-stats
- [ ] restaurant_delivery_zones
  - [ ] GET /restaurant-delivery-zones
  - [ ] POST /restaurant-delivery-zones
  - [ ] DELETE /restaurant-delivery-zones/{id}
- [ ] restaurant_opening_hours
  - [ ] GET /restaurant-opening-hours
  - [ ] POST /restaurant-opening-hours
  - [ ] DELETE /restaurant-opening-hours/{id}
- [ ] restaurant_reviews
  - [ ] GET /restaurant-reviews
  - [ ] POST /restaurant-reviews
  - [ ] DELETE /restaurant-reviews/{id}
- [ ] restaurants
 - [x] restaurants
  - [x] GET /restaurants
  - [x] GET /restaurants/{id}
  - [x] POST /restaurants
  - [x] PUT /restaurants/{id}
  - [x] DELETE /restaurants/{id}
- [ ] transactions
 - [x] transactions
  - [x] GET /transactions
  - [x] POST /transactions
 - [x] tips
  - [x] GET /tips
  - [x] POST /tips
- [ ] user_favorites
  - [ ] GET /user-favorites
  - [ ] POST /user-favorites
- [ ] user_saved_dishes
  - [ ] GET /user-saved-dishes
  - [ ] POST /user-saved-dishes
- [ ] users
 - [x] users
  - [x] GET /users
  - [x] GET /users/{id}
  - [x] POST /users
  - [x] PUT /users/{id}
  - [x] DELETE /users/{id}
- [ ] user_wallets
 - [x] user_wallets
  - [x] GET /user-wallets
  - [x] POST /user-wallets
- [ ] wallet_transactions
  - [x] GET /wallet-transactions
  - [x] POST /wallet-transactions

Notas:
- Algunas entidades apuntadas son de soporte interno (ej. `audit_logs`, `order_tracking`, `restaurant_daily_stats`). Puedes decidir no exponer CRUD público para ellas; en ese caso mantendremos checkbox privados.
- Marcar el checkbox principal (por ejemplo `users`) implica implementar la entidad JPA, repository, servicios, DTOs, mappers (con Lombok si lo deseas), y todos los endpoints CRUD listados.


