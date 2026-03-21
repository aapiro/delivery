# Fase 0 – Admin + Quarkus (enfoque top-down)

## Prioridad

- El **frontend principal (cliente)** define el contrato de datos; el **backend** se extiende cuando falten campos.
- El **admin** debe cubrir los **maestros** necesarios para mantener lo que ve el cliente.
- **Backend se adapta al frontend**, no al revés.

## Frontend admin – HTTP y auth

| Pieza | Rol |
|--------|-----|
| `services/adminApiClient.ts` | Axios con `API_CONFIG.BASE_URL` y Bearer leído del persist Zustand (`delivery_token_admin` → `state.token`). |
| `constants/admin.ts` | `ADMIN_ZUSTAND_STORAGE_KEY` alineado con `persist.name` del store; rutas públicas de auth admin. |
| `services/adminHttp.ts` | `get/post/...` devuelven **body**; `pickData()` desenvuelve `{ data: T }` sin romper páginas (`content`) ni `PaginatedResponse` (`pagination`). |
| `services/admin.ts` | Servicios admin usando `adminHttp` + `pickData`; blobs vía `responseType: 'blob'`. |
| `api/custom-instance.ts` | Orval usa **`adminApiClient`** (misma base URL y token admin que el resto del panel). |
| `store/adminStore.ts` | **No** toca `api` público (`delivery_token`); solo persist del admin. |

## Auth admin en Quarkus (JWT)

- **Usuarios**: fila en `users` con `user_type` ∈ `SUPER_ADMIN`, `ADMIN`, `MANAGER`, `MODERATOR` y `password_hash` (BCrypt).
- **Endpoints** (`/api` implícito en cliente):
        - `POST /admin/auth/login` → `{ admin, token, refreshToken, expiresIn }` (JWT con grupo `admin`).
        - `POST /admin/auth/refresh` → mismo shape.
        - `POST /admin/auth/logout` | `GET /admin/auth/profile` (perfil) | `PUT /admin/auth/profile` (nombre/email) → **Bearer** obligatorio.
- **Protección**: recursos bajo `/admin/restaurants`, `/admin/dishes`, `/admin/categories`, `/admin/dashboard`, `.../menu-categories` requieren rol **`admin`** (mismo Bearer).
- **Login público** `/auth/login`: las cuentas administrativas **no** pueden entrar ahí (deben usar `/admin/auth/login`).
- **Seed dev** (Liquibase `005-data-seed-admin-user.sql`): `admin@delivery.local` / `admin123` (solo entornos que cargan `data-test`; en dev con Liquibase desde el changelog master).

## CORS (dev)

En `quarkus-backend`: orígenes `http://localhost:3000` y `http://127.0.0.1:3000`.

## Componentes reutilizables (admin)

Ubicación: `frontend/src/components/admin/common/` (barrel `index.ts`).

| Componente | Uso |
|------------|-----|
| `AdminPageHeader` | Título, descripción y acciones alineadas. |
| `AdminSectionCard` | Bloques con tarjeta + título/descripción opcional. |
| `AdminQueryBoundary` | Patrón React Query: loading (esqueleto lista) / error / children. |
| `AdminErrorState` | Mensaje de error + reintento (`onRetry` o reload). |
| `AdminEmptyState` | Listas vacías. |
| `AdminListPageSkeleton` / `AdminStatsGridSkeleton` | Placeholders de carga. |
| `AdminCardGridSkeleton` | Grid de tarjetas en pulso (p. ej. categorías). |
| `getAdminErrorMessage` | Extrae mensaje legible de errores (p. ej. Axios). |

**Páginas de referencia:** `AdminDashboard`, `OrdersManagement`, `UsersManagement`, `RestaurantsManagement`, `ReportsPage`, `CategoriesManagement`, `DishesManagement`, `RestaurantCreate`, `RestaurantEdit`.

## Rutas admin añadidas (CRUD categoría global + platos)

| Ruta | Pantalla |
|------|-----------|
| `/admin/categories/create` | Alta categoría (`categories` en backend). |
| `/admin/categories/:id/edit` | Edición de nombre. |
| `/admin/dishes/create` | Alta plato (JSON `AdminDishWriteDto`). |
| `/admin/dishes/:id/edit` | Edición plato. |
| `/admin/restaurant-menu-categories` | **Categorías de menú por restaurante** (CRUD sobre `menu_categories`). |

**API admin (JWT):** `GET|POST /admin/restaurants/{id}/menu-categories`, `PUT|DELETE …/menu-categories/{categoryId}` (`adminRestaurantMenuCategories`). Los formularios de platos usan la misma lista vía `adminMenuCategories.getByRestaurant`.

La lista **`/admin/categories`** es la entidad global **`Category`** (nombre + activo), distinta del menú.

## Próximos pasos sugeridos

- Persistir **permisos** por admin en BD (ahora se derivan del `user_type` en código).
- Reactivar **ProtectedRoute** estricto en el frontend cuando el flujo de sesión esté validado en QA.
- Revisar pantallas admin vs. modelo del cliente y añadir endpoints/campos en Quarkus donde falten.

## Build

Si ESLint bloquea el build en otros archivos: `DISABLE_ESLINT_PLUGIN=true npm run build` (solo para verificar TypeScript hasta limpiar lint).
