Resumen rápido del proyecto (contexto para iniciar un nuevo chat)
==============================================================

Propósito
--------
Este archivo contiene un resumen mínimo y listo para pegar en un nuevo chat como "seed prompt" cuando quieras retomar o iniciar desarrollo en el módulo `spring-backend`. Incluye: 
- Archivos clave
- Comandos para levantar el entorno
- Variables de entorno
- Lista de entidades / checklist (referencia rápida)
- Un "starter prompt" que puedes pegar en cualquier chat para restaurar suficiente contexto y evitar empezar sin información.

Por qué usar esto
-----------------
Los modelos de chat tienen ventanillas de contexto limitadas. Mantener un archivo con el resumen del proyecto te permite: 
- Copiar/pegar contexto mínimo al abrir un chat nuevo.
- Adjuntar el archivo en el repo para cualquier colaborador.
- Evitar perder tiempo re-explicando la base del proyecto.

Archivos clave (ruta relativa al repo `spring-backend`)
-----------------------------------------------------
- `src/main/resources/static/openapi.yml`  — OpenAPI principal (documenta endpoints y schemas).
- `src/main/resources/db/changelog/db.changelog-master.yaml` — master Liquibase (incluye `schema` y `data-test`).
- `src/main/resources/db/changelog/schema/001-schema-create-core-tables.sql` — SQL con DDL principal (muchas tablas).
- `src/main/resources/application.properties` — configuración Spring (DB URL, liquibase, swagger).
- `README.md` — contiene checklist maestro de entidades/endpoints (usa como tracker).

Variables de entorno recomendadas
---------------------------------
POSTGRES_DB=delivery
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_PORT=5432
SPRING_BACKEND_PORT=8080
LIQUIBASE_ENABLED=true
DB_URL=jdbc:postgresql://postgres:5432/${POSTGRES_DB}
DB_USERNAME=${POSTGRES_USER}
DB_PASSWORD=${POSTGRES_PASSWORD}

Comandos útiles
---------------
# Construir aplicación (mvn):
```
cd spring-backend
mvn -DskipTests package
```

# Levantar con docker compose (asume docker-compose.yml en root o dentro de spring-backend):
```
docker compose up --build
```

# Comprobar tablas creadas por Liquibase (si Postgres expuesto en localhost):
```
psql "postgresql://${POSTGRES_USER}:${POSTGRES_PASSWORD}@localhost:${POSTGRES_PORT}/${POSTGRES_DB}" -c "\dt public.*"
```

# Validar OpenAPI (local):
```
npm install -g @apidevtools/swagger-cli
swagger-cli validate src/main/resources/static/openapi.yml
```

Checklist maestro (referencia - ya está en README)
--------------------------------------------------
(Resumido) Entidades principales detectadas en SQL — marcar en README cuando se implemente cada CRUD.
- addresses
- audit_logs
- courier_reviews
- couriers
- dish_availability
- dish_option_values
- dish_options
- dish_reviews
- dishes
- menu_categories
- notifications
- notification_tokens
- order_issues
- order_items
- order_status_history
- order_tracking
- orders
- payment_methods
- payouts
- promotions
- referral_codes
- restaurant_closures
- restaurant_cuisines
- restaurant_daily_stats
- restaurant_delivery_zones
- restaurant_opening_hours
- restaurant_reviews
- restaurants
- transactions
- tips
- user_favorites
- user_saved_dishes
- users
- user_wallets
- wallet_transactions

Starter prompt (seed) — copia y pega al abrir un nuevo chat
------------------------------------------------------------
"Hola: quiero trabajar en el módulo `spring-backend` del repo 'delivery'. Resumen breve:
- Es una aplicación Spring Boot dockerizada.
- Liquibase aplica los scripts en `src/main/resources/db/changelog/schema/` (archivo principal `001-schema-create-core-tables.sql`).
- OpenAPI principal está en `src/main/resources/static/openapi.yml`.
- Variables DB: POSTGRES_DB=delivery, POSTGRES_USER=postgres, POSTGRES_PASSWORD=postgres, POSTGRES_PORT=5432.

Mi objetivo ahora: <DESCRIBE AQUÍ LA TAREA (ej. "implementar CRUD completo para users: entidad JPA, repository, service, controller, DTOs y mappers con Lombok; tests de integración usando Testcontainers")>

Favor de:
1) Confirmar los archivos clave a modificar.
2) Sugerir estructura de paquetes y nombres de clases.
3) Generar el código (si corresponde) y los tests básicos.

Contexto adicional (si necesitas más): copia el contenido de `src/main/resources/db/changelog/schema/001-schema-create-core-tables.sql` y `src/main/resources/static/openapi.yml`.
"

Consejos para mantener contexto entre chats
-----------------------------------------
1. Siempre empieza un nuevo chat pegando el "Starter prompt" (arriba). Es la forma más simple y fiable.
2. Mantén `CHAT_CONTEXT.md`/`CONTEXT.md` en la raíz del módulo: cualquier colaborador puede usarlo.
3. Crea issues o PRs en el repo con todo el scope, y en el chat referencia el número del issue/PR — así el contexto queda persistido en Git.
4. Usa gists o notas privadas (o el propio repo) para guardar plantillas de prompts para tareas repetitivas.
5. Si usas una interfaz de chat que permite "pin" o "star", guarda ahí el starter prompt.

Plantillas útiles (para pegar en el chat)
-----------------------------------------
- Plantilla para pedir generación de CRUD completa (copiar y ajustar):
```
Quiero que generes CRUD completo para la entidad <EntityName> basada en la tabla <table_name> del archivo SQL. Requisitos:
- Paquete base: com.ilimitech.delivery.spring
- Usar Lombok para getters/setters/builder/mapstruct (o mappers manuales si prefieres)
- Programación funcional (Streams, Optional) cuando proceda
- JPA Entities + Spring Data JPA repositories
- Service layer con lógica mínima
- REST controller con endpoints estándar (GET, POST, PUT, DELETE)
- DTOs y mappers (MapStruct o Lombok builders)
- Test de integración básico que arranque la aplicación con Testcontainers/Postgres
```

Opcional: script para mostrar contexto rápidamente
-------------------------------------------------
Puedes añadir un script `scripts/show_context.sh` que imprima rutas y fragmentos clave. Ejemplo:
```bash
#!/usr/bin/env bash
echo "OpenAPI: src/main/resources/static/openapi.yml"
echo "Liquibase changelog: src/main/resources/db/changelog/schema/001-schema-create-core-tables.sql"
echo "README checklist: README.md"
```

He creado este archivo `spring-backend/CHAT_CONTEXT.md` en el repo para que lo uses como origen de verdad. Pega el "Starter prompt" en un nuevo chat y reemplaza el campo <DESCRIBE AQUÍ LA TAREA> por la tarea concreta para que el asistente tenga suficiente contexto.

¿Quieres que además:
- A) Cree `scripts/show_context.sh` en el repo?
- B) Convierta el "Starter prompt" en un gist público/privado y te deje el enlace?
- C) Genere una plantilla de issue/PR para implementar un CRUD y la añada al repo?

Dime qué prefieres y lo hago (no generaré código CRUD hasta tu confirmación).
