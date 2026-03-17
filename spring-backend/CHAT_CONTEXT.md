
Resumen rápido del proyecto (contexto para iniciar un nuevo chat)
==============================================================

Propósito
--------
Este archivo contiene un "seed" actualizado que puedes copiar/pegar en un nuevo chat para retomar el trabajo en el módulo `spring-backend`. Incluye:
- Archivos clave
- Estado actual de implementación (entidades/endpoints ya generados)
- Comandos para ejecutar tests individuales
- Variables de entorno recomendadas
- Un "starter prompt" listo para pegar en un nuevo chat

Por qué usar esto
-----------------
Los modelos de chat tienen límite de contexto. Mantener este archivo actualizado permite:
- Retomar la conversación en otra ventana sin perder el hilo.
- Proveer al asistente un resumen mínimo y accionable.

Archivos clave (ruta relativa al módulo `spring-backend`)
-----------------------------------------------------
- `src/main/resources/static/openapi.yml`  — OpenAPI principal (documenta endpoints y schemas).
- `src/main/resources/db/changelog/schema/001-schema-create-core-tables.sql` — SQL con DDL principal.
- `src/main/resources/application.properties` — configuración Spring (DB URL, liquibase, swagger).
- `README.md` — checklist maestro de entidades/endpoints (tracker de implementación).

Estado actual (implementaciones realizadas)
-----------------------------------------
Las siguientes entidades CRUD ya fueron creadas (Entity, Repository, DTOs, Mapper, Service, Controller) y tienen tests de integración básicos que pasan con H2 en memoria:

- addresses — endpoints: GET/POST/GET/{id}/PUT/DELETE
- restaurants — endpoints: GET/POST/GET/{id}/PUT/DELETE
- transactions — endpoints: GET/POST/GET/{id}/DELETE (básicos)
- tips — endpoints: GET/POST/GET/{id}/PUT/DELETE
- users — endpoints: GET/POST/GET/{id}/PUT/DELETE
- user_wallets — endpoints: GET/POST/GET/{id}/PUT/DELETE
- wallet_transactions — endpoints: GET/POST/GET/{id}/PUT/DELETE
- orders — endpoints: GET/POST/GET/{id}/PUT/DELETE
- order_items — endpoints: GET/POST/GET/{id}/PUT/DELETE

Nota técnica importante
----------------------
- Lombok fue eliminado del `pom.xml` y las clases generadas se convirtieron a POJOs (getters/setters/builders manuales) para evitar problemas de compatibilidad con el JDK presente en el entorno (en este equipo se detectó Java 25). Si prefieres reintroducir Lombok, lo puedo hacer, pero deberás ejecutar Maven con JDK 17 o actualizar Lombok a una versión compatible.
- Se añadió `h2` en scope `test` al `pom.xml` para facilitar tests rápidos en memoria.

Comandos útiles (ejecutar tests individuales)
--------------------------------------------
Para ejecutar solo un test de integración concreto (ejemplo `OrderIntegrationTest`):

```bash
cd /Users/usuario/IdeaProjects/delivery/spring-backend
mvn -Dtest=OrderIntegrationTest test
```

Ejemplos de tests ya creados y nombres de clases:
- `AddressIntegrationTest`
- `RestaurantIntegrationTest`
- `TransactionIntegrationTest`
- `TipIntegrationTest`
- `UserIntegrationTest`
- `UserWalletIntegrationTest`
- `WalletTransactionIntegrationTest`
- `OrderIntegrationTest`
- `OrderItemIntegrationTest`

Comandos comunes
---------------
Construcción (sin tests):

```bash
cd /Users/usuario/IdeaProjects/delivery/spring-backend
mvn -DskipTests package
```

Levantar con Docker Compose (si quieres la stack completa):

```bash
cd /Users/usuario/IdeaProjects/delivery
docker compose up --build
```

Variables de entorno recomendadas
--------------------------------
POSTGRES_DB=delivery
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_PORT=5432
SPRING_BACKEND_PORT=8080
LIQUIBASE_ENABLED=true

Starter prompt (seed) — copia y pega al abrir un nuevo chat
------------------------------------------------------------
Pega el siguiente bloque en un nuevo chat para restaurar contexto y continuar donde lo dejaste. Reemplaza el campo `TAREA` por lo que quieras hacer.

```
Contexto: trabajo en el módulo `spring-backend` del repo 'delivery'.

Estado actual (resumido):
- Entidades CRUD implementadas y testeadas: addresses, restaurants, transactions, tips, users, user_wallets, wallet_transactions, orders, order_items.
- Lombok fue removido; los POJOs usan getters/setters manuales. H2 está en scope test.

Mi objetivo ahora: TAREA

Por favor:
1) Confirma los archivos clave a modificar para esta tarea.
2) Genera/propón la estructura de paquetes y los nombres de clases.
3) Si procede, genera el código (entity/repo/service/controller/DTOs/mapper) y tests básicos.

Comandos útiles para pruebas locales:
```bash
cd /Users/usuario/IdeaProjects/delivery/spring-backend
mvn -Dtest=<NombreDelTest> test
```
```

Notas rápidas para el revisor:
- Si quieres que use Lombok/MapStruct en nuevas entidades, dime y lo revertimos (necesitarás JDK 17 para construir en entorno donde Lombok APT sea compatible).
- Si quieres OpenAPI actualizado, puedo actualizar `src/main/resources/static/openapi.yml` con los nuevos paths/schemas.

Fin del seed.
```

Consejos para no perder el hilo entre ventanas de chat
-----------------------------------------------------
1. Copia el bloque "Starter prompt" arriba cada vez que abras un nuevo chat y añade la tarea concreta.
2. Si el cambio es grande, crea un issue/PR y pega el número del issue en el prompt para que el asistente lea el PR si se necesita.
3. Mantén este `CHAT_CONTEXT.md` actualizado; yo (el asistente) lo usaré como referencia cuando me pidas continuar.

Opciones adicionales (¿quieres que haga ahora?):
- A) Crear `scripts/show_context.sh` en el repo que imprima rutas clave.
- B) Actualizar `src/main/resources/static/openapi.yml` para incluir los endpoints generados hasta ahora.
- C) Añadir validaciones (Bean Validation) y tests negativos para los endpoints ya implementados.

Indica la letra de la opción que quieres y la ejecutaré.
