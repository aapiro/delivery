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

