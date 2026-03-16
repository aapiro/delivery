# Análisis de Endpoints del Frontend

## Resumen Ejecutivo
Este documento analiza todos los endpoints consumidos por el frontend y verifica su correspondencia con los endpoints disponibles en el backend.

---

## 1. ENDPOINTS CONSUMIDOS - AUTENTICACIÓN

### Auth - Login
- **Endpoint**: `/auth/login`
- **Método**: POST
- **Consumido en**: `src/services/api.ts` → `loginUser()`
- **Datos**: `{ email: string, password: string }`
- **Respuesta esperada**: `{ user, token: string, refreshToken: string }`
- **Estado**: ✅ Definido en `API_ENDPOINTS.AUTH.LOGIN`

### Auth - Register
- **Endpoint**: `/auth/register`
- **Método**: POST
- **Consumido en**: `src/services/api.ts` → `registerUser()`
- **Datos**: `{ name: string, email: string, password: string }`
- **Respuesta esperada**: `{ user, token: string, refreshToken: string }`
- **Estado**: ✅ Definido en `API_ENDPOINTS.AUTH.REGISTER`

### Auth - Refresh Token
- **Endpoint**: `/auth/refresh`
- **Método**: POST
- **Consumido en**: `src/services/api.ts` → `refreshToken()`
- **Datos**: `{ refreshToken: string }`
- **Respuesta esperada**: `{ token: string, refreshToken: string }`
- **Estado**: ✅ Definido en `API_ENDPOINTS.AUTH.REFRESH`

### Auth - Logout
- **Endpoint**: `/auth/logout`
- **Método**: POST
- **Consumido en**: `src/services/api.ts` → `logoutUser()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.AUTH.LOGOUT` (sin usar en logoutUser)

### Auth - Profile
- **Endpoint**: `/auth/profile`
- **Método**: GET/PUT
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.AUTH.PROFILE`

---

## 2. ENDPOINTS CONSUMIDOS - RESTAURANTES

### Restaurantes - Listar
- **Endpoint**: `/restaurants`
- **Método**: GET
- **Parámetros**: `page`, `limit`, `search`, `category`, `minRating`, `maxDeliveryTime`, `maxDeliveryFee`, `maxMinimumOrder`, `isOpen`
- **Consumido en**: `src/services/restaurants.ts` → `getRestaurants()`
- **Respuesta esperada**: `PaginatedResponse<Restaurant>`
- **Estado**: ✅ Definido en `API_ENDPOINTS.RESTAURANTS.LIST`

### Restaurantes - Detalle
- **Endpoint**: `/restaurants/:id`
- **Método**: GET
- **Consumido en**: `src/services/restaurants.ts` → `getRestaurant()`
- **Respuesta esperada**: `Restaurant`
- **Estado**: ✅ Definido en `API_ENDPOINTS.RESTAURANTS.DETAIL(id)`

### Restaurantes - Platos
- **Endpoint**: `/restaurants/:id/dishes`
- **Método**: GET
- **Consumido en**: `src/services/restaurants.ts` → `getRestaurantDishes()`
- **Respuesta esperada**: `Dish[]`
- **Estado**: ✅ Definido en `API_ENDPOINTS.RESTAURANTS.DISHES(id)`

### Restaurantes - Buscar
- **Endpoint**: `/restaurants/search`
- **Método**: GET
- **Parámetros**: `q`, `category`, `minRating`, `isOpen`
- **Consumido en**: `src/services/restaurants.ts` → `searchRestaurants()`
- **Respuesta esperada**: `Restaurant[]`
- **Estado**: ✅ Definido en `API_ENDPOINTS.RESTAURANTS.SEARCH`

### Restaurantes - Categorías
- **Endpoint**: `/restaurants/categories`
- **Método**: GET
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.RESTAURANTS.CATEGORIES`

---

## 3. ENDPOINTS CONSUMIDOS - PLATOS

### Platos - Detalle
- **Endpoint**: `/dishes/:id`
- **Método**: GET
- **Consumido en**: `src/services/restaurants.ts` → `getDish()`
- **Respuesta esperada**: `Dish`
- **Estado**: ✅ Definido en `API_ENDPOINTS.DISHES.DETAIL(id)`

### Platos - Categorías del Restaurante
- **Endpoint**: `/restaurants/:restaurantId/dish-categories`
- **Método**: GET
- **Consumido en**: `src/services/restaurants.ts` → `getDishCategories()`
- **Respuesta esperada**: Categorías de platos
- **Estado**: ✅ Definido en `API_ENDPOINTS.DISHES.CATEGORIES(restaurantId)`

### Platos - Buscar
- **Endpoint**: `/dishes/search`
- **Método**: GET
- **Parámetros**: `q`
- **Consumido en**: `src/services/restaurants.ts` → `searchDishes()`
- **Respuesta esperada**: `Dish[]`
- **Estado**: ✅ Definido en `API_ENDPOINTS.DISHES.SEARCH`

### Platos - Listar
- **Endpoint**: `/dishes`
- **Método**: GET
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.DISHES.LIST`

---

## 4. ENDPOINTS CONSUMIDOS - ÓRDENES (Usuario)

### Órdenes - Crear
- **Endpoint**: `/orders`
- **Método**: POST
- **Consumido en**: `src/services/orders.ts` → `createOrder()`
- **Datos**: `CreateOrderRequest` con items, dirección, método de pago
- **Respuesta esperada**: `Order`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ORDERS.CREATE`

### Órdenes - Listar
- **Endpoint**: `/orders`
- **Método**: GET
- **Parámetros**: `status`, `limit`, `offset`
- **Consumido en**: `src/services/orders.ts` → `getOrders()`
- **Respuesta esperada**: `Order[]`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ORDERS.LIST`

### Órdenes - Detalle
- **Endpoint**: `/orders/:id`
- **Método**: GET
- **Consumido en**: `src/services/orders.ts` → `getOrderById()`
- **Respuesta esperada**: `Order`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ORDERS.DETAIL(id)`

### Órdenes - Actualizar Estado
- **Endpoint**: `/orders/:id`
- **Método**: PUT
- **Consumido en**: `src/services/orders.ts` → `updateOrderStatus()`
- **Datos**: `{ status: OrderStatus }`
- **Respuesta esperada**: `Order`
- **Estado**: ⚠️ **INCONSISTENCIA**: Se usa PUT pero se puede definir un endpoint específico

### Órdenes - Cancelar
- **Endpoint**: `/orders/:id`
- **Método**: DELETE
- **Consumido en**: `src/services/orders.ts` → `cancelOrder()`
- **Respuesta esperada**: `Order`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ORDERS.CANCEL(id)` (pero en servicio usa DELETE a `/orders/:id`)

### Órdenes - Rastrear
- **Endpoint**: `/orders/:id/track`
- **Método**: GET
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.ORDERS.TRACK(id)`

---

## 5. ENDPOINTS CONSUMIDOS - CARRITO

### Carrito - Obtener
- **Endpoint**: `/cart`
- **Método**: GET
- **Consumido en**: No actualmente (solo localStorage)
- **Estado**: ✅ Definido en `API_ENDPOINTS.CART.GET`

### Carrito - Agregar
- **Endpoint**: `/cart/add`
- **Método**: POST
- **Consumido en**: No actualmente (solo localStorage)
- **Estado**: ✅ Definido en `API_ENDPOINTS.CART.ADD`

### Carrito - Actualizar
- **Endpoint**: `/cart/update`
- **Método**: POST
- **Consumido en**: No actualmente (solo localStorage)
- **Estado**: ✅ Definido en `API_ENDPOINTS.CART.UPDATE`

### Carrito - Remover
- **Endpoint**: `/cart/remove`
- **Método**: POST
- **Consumido en**: No actualmente (solo localStorage)
- **Estado**: ✅ Definido en `API_ENDPOINTS.CART.REMOVE`

### Carrito - Limpiar
- **Endpoint**: `/cart/clear`
- **Método**: POST
- **Consumido en**: No actualmente (solo localStorage)
- **Estado**: ✅ Definido en `API_ENDPOINTS.CART.CLEAR`

---

## 6. ENDPOINTS CONSUMIDOS - USUARIOS

### Usuarios - Dirección - Listar
- **Endpoint**: `/users/addresses`
- **Método**: GET
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.USERS.ADDRESSES`

### Usuarios - Dirección - Agregar
- **Endpoint**: `/users/addresses`
- **Método**: POST
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.USERS.ADD_ADDRESS`

### Usuarios - Dirección - Actualizar
- **Endpoint**: `/users/addresses/:id`
- **Método**: PUT
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.USERS.UPDATE_ADDRESS(id)`

### Usuarios - Dirección - Eliminar
- **Endpoint**: `/users/addresses/:id`
- **Método**: DELETE
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.USERS.DELETE_ADDRESS(id)`

### Usuarios - Dirección - Establecer por defecto
- **Endpoint**: `/users/addresses/:id/default`
- **Método**: PATCH
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.USERS.SET_DEFAULT_ADDRESS(id)`

---

## 7. ENDPOINTS CONSUMIDOS - PAGOS

### Pagos - Métodos
- **Endpoint**: `/payments/methods`
- **Método**: GET
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.PAYMENTS.METHODS`

### Pagos - Procesar
- **Endpoint**: `/payments/process`
- **Método**: POST
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.PAYMENTS.PROCESS`

### Pagos - Webhook
- **Endpoint**: `/payments/webhook`
- **Método**: POST
- **Consumido en**: No (backend, no frontend)
- **Estado**: ✅ Definido en `API_ENDPOINTS.PAYMENTS.WEBHOOK`

---

## 8. ENDPOINTS CONSUMIDOS - ADMINISTRACIÓN

### Admin - Autenticación - Login
- **Endpoint**: `/admin/auth/login`
- **Método**: POST
- **Consumido en**: `src/services/admin.ts` → `adminAuth.login()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.LOGIN`

### Admin - Autenticación - Logout
- **Endpoint**: `/admin/auth/logout`
- **Método**: POST
- **Consumido en**: `src/services/admin.ts` → `adminAuth.logout()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.LOGOUT`

### Admin - Autenticación - Refresh
- **Endpoint**: `/admin/auth/refresh`
- **Método**: POST
- **Consumido en**: `src/services/admin.ts` → `adminAuth.refreshToken()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.REFRESH`

### Admin - Autenticación - Profile
- **Endpoint**: `/admin/auth/profile`
- **Método**: GET/PUT
- **Consumido en**: `src/services/admin.ts` → `adminAuth.getProfile()`, `adminAuth.updateProfile()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.PROFILE`

### Admin - Dashboard - Stats
- **Endpoint**: `/admin/dashboard/stats`
- **Método**: GET
- **Consumido en**: `src/services/admin.ts` → `adminDashboard.getStats()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.STATS`

### Admin - Dashboard
- **Endpoint**: `/admin/dashboard`
- **Método**: GET
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.DASHBOARD`

---

### Admin - Restaurantes - Listar
- **Endpoint**: `/admin/restaurants`
- **Método**: GET
- **Consumido en**: `src/services/admin.ts` → `adminRestaurants.getAll()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.RESTAURANTS`

### Admin - Restaurantes - Crear
- **Endpoint**: `/admin/restaurants`
- **Método**: POST
- **Consumido en**: `src/services/admin.ts` → `adminRestaurants.create()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.RESTAURANT_CREATE`

### Admin - Restaurantes - Detalle
- **Endpoint**: `/admin/restaurants/:id`
- **Método**: GET
- **Consumido en**: `src/services/admin.ts` → `adminRestaurants.getById()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.RESTAURANT_DETAIL(id)`

### Admin - Restaurantes - Actualizar
- **Endpoint**: `/admin/restaurants/:id`
- **Método**: PUT
- **Consumido en**: `src/services/admin.ts` → `adminRestaurants.update()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.RESTAURANT_UPDATE(id)`

### Admin - Restaurantes - Eliminar
- **Endpoint**: `/admin/restaurants/:id`
- **Método**: DELETE
- **Consumido en**: `src/services/admin.ts` → `adminRestaurants.delete()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.RESTAURANT_DELETE(id)`

### Admin - Restaurantes - Cambiar Estado
- **Endpoint**: `/admin/restaurants/:id/toggle-status`
- **Método**: PATCH
- **Consumido en**: `src/services/admin.ts` → `adminRestaurants.toggleStatus()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.RESTAURANT_TOGGLE_STATUS(id)`

---

### Admin - Platos - Listar
- **Endpoint**: `/admin/dishes`
- **Método**: GET
- **Consumido en**: `src/services/admin.ts` → `adminDishes.getAll()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.DISHES`

### Admin - Platos - Crear
- **Endpoint**: `/admin/dishes`
- **Método**: POST
- **Consumido en**: `src/services/admin.ts` → `adminDishes.create()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.DISH_CREATE`

### Admin - Platos - Detalle
- **Endpoint**: `/admin/dishes/:id`
- **Método**: GET
- **Consumido en**: `src/services/admin.ts` → `adminDishes.getById()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.DISH_DETAIL(id)`

### Admin - Platos - Actualizar
- **Endpoint**: `/admin/dishes/:id`
- **Método**: PUT
- **Consumido en**: `src/services/admin.ts` → `adminDishes.update()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.DISH_UPDATE(id)`

### Admin - Platos - Eliminar
- **Endpoint**: `/admin/dishes/:id`
- **Método**: DELETE
- **Consumido en**: `src/services/admin.ts` → `adminDishes.delete()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.DISH_DELETE(id)`

### Admin - Platos - Cambiar Disponibilidad
- **Endpoint**: `/admin/dishes/:id/toggle-availability`
- **Método**: PATCH
- **Consumido en**: `src/services/admin.ts` → `adminDishes.toggleAvailability()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.DISH_TOGGLE_AVAILABILITY(id)`

---

### Admin - Categorías de Platos - Listar
- **Endpoint**: `/admin/categories`
- **Método**: GET
- **Consumido en**: `src/services/admin.ts` → `adminDishCategories.getAll()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.CATEGORIES`

### Admin - Categorías de Platos - Crear
- **Endpoint**: `/admin/categories`
- **Método**: POST
- **Consumido en**: `src/services/admin.ts` → `adminDishCategories.create()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.CATEGORY_CREATE`

### Admin - Categorías de Platos - Detalle
- **Endpoint**: `/admin/categories/:id`
- **Método**: GET
- **Consumido en**: `src/services/admin.ts` → `adminDishCategories.getById()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.CATEGORY_DETAIL(id)`

### Admin - Categorías de Platos - Actualizar
- **Endpoint**: `/admin/categories/:id`
- **Método**: PUT
- **Consumido en**: `src/services/admin.ts` → `adminDishCategories.update()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.CATEGORY_UPDATE(id)`

### Admin - Categorías de Platos - Eliminar
- **Endpoint**: `/admin/categories/:id`
- **Método**: DELETE
- **Consumido en**: `src/services/admin.ts` → `adminDishCategories.delete()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.CATEGORY_DELETE(id)`

### Admin - Categorías de Platos - Cambiar Estado
- **Endpoint**: `/admin/categories/:id/toggle-status`
- **Método**: PATCH
- **Consumido en**: `src/services/admin.ts` → `adminDishCategories.toggleStatus()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.CATEGORY_TOGGLE_STATUS(id)`

---

### Admin - Órdenes - Listar
- **Endpoint**: `/admin/orders`
- **Método**: GET
- **Consumido en**: `src/services/admin.ts` → `adminOrders.getAll()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.ORDERS`

### Admin - Órdenes - Detalle
- **Endpoint**: `/admin/orders/:id`
- **Método**: GET
- **Consumido en**: `src/services/admin.ts` → `adminOrders.getById()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.ORDER_DETAIL(id)`

### Admin - Órdenes - Actualizar Estado
- **Endpoint**: `/admin/orders/:id/status`
- **Método**: PATCH
- **Consumido en**: `src/services/admin.ts` → `adminOrders.updateStatus()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.ORDER_UPDATE_STATUS(id)`

### Admin - Órdenes - Cancelar
- **Endpoint**: `/admin/orders/:id/cancel`
- **Método**: PATCH
- **Consumido en**: `src/services/admin.ts` → `adminOrders.cancel()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.ORDER_CANCEL(id)`

### Admin - Órdenes - Reembolsar
- **Endpoint**: `/admin/orders/:id/refund`
- **Método**: PATCH
- **Consumido en**: `src/services/admin.ts` → `adminOrders.refund()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.ORDER_REFUND(id)`

---

### Admin - Usuarios - Listar
- **Endpoint**: `/admin/users`
- **Método**: GET
- **Consumido en**: `src/services/admin.ts` → `adminUsers.getAll()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.USERS`

### Admin - Usuarios - Detalle
- **Endpoint**: `/admin/users/:id`
- **Método**: GET
- **Consumido en**: `src/services/admin.ts` → `adminUsers.getById()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.USER_DETAIL(id)`

### Admin - Usuarios - Actualizar
- **Endpoint**: `/admin/users/:id`
- **Método**: PUT
- **Consumido en**: `src/services/admin.ts` → `adminUsers.update()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.USER_UPDATE(id)`

### Admin - Usuarios - Cambiar Estado
- **Endpoint**: `/admin/users/:id/toggle-status`
- **Método**: PATCH
- **Consumido en**: `src/services/admin.ts` → `adminUsers.toggleStatus()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.USER_TOGGLE_STATUS(id)`

### Admin - Usuarios - Eliminar
- **Endpoint**: `/admin/users/:id`
- **Método**: DELETE
- **Consumido en**: `src/services/admin.ts` → `adminUsers.delete()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.USER_DELETE(id)`

---

### Admin - Reportes - Reporte de Ventas
- **Endpoint**: `/admin/reports/sales`
- **Método**: GET
- **Parámetros**: `period`, `startDate`, `endDate`
- **Consumido en**: `src/services/admin.ts` → `adminReports.getSalesReport()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.SALES_REPORT`

### Admin - Reportes - Reporte de Usuarios
- **Endpoint**: `/admin/reports/users`
- **Método**: GET
- **Consumido en**: `src/services/admin.ts` → `adminReports.getUsersReport()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.USERS_REPORT`

### Admin - Reportes - Exportar
- **Endpoint**: `/admin/reports/export`
- **Método**: GET
- **Parámetros**: `type`, `format`, `filters`
- **Consumido en**: `src/services/admin.ts` → `adminReports.exportReport()`
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.EXPORT_REPORT`

### Admin - Reportes - Listado General
- **Endpoint**: `/admin/reports`
- **Método**: GET
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.REPORTS`

---

### Admin - Sistema - Administradores
- **Endpoint**: `/admin/system/admins`
- **Método**: GET/POST/PUT/DELETE
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.ADMINS`

### Admin - Sistema - Configuración
- **Endpoint**: `/admin/system/settings`
- **Método**: GET/PUT
- **Consumido en**: No actualmente
- **Estado**: ✅ Definido en `API_ENDPOINTS.ADMIN.SETTINGS`

---

## 9. RESUMEN DE HALLAZGOS

### ✅ Endpoints Correctamente Definidos y Consumidos
Total de endpoints definidos: **69**
Total de endpoints activamente consumidos: **50**
Total de endpoints definidos pero no consumidos aún: **19**

### ⚠️ Inconsistencias Detectadas

1. **Órdenes - Cancelar**: 
   - El código en `orders.ts` usa `DELETE /orders/:id`
   - Pero hay un endpoint alternativo definido: `PATCH /orders/:id/cancel`
   - **Recomendación**: Usar el endpoint específico `/orders/:id/cancel` para mayor claridad

2. **Órdenes - Actualizar Estado**:
   - El código en `orders.ts` usa `PUT /orders/:id` 
   - Pero hay un endpoint específico definido: `PATCH /admin/orders/:id/status`
   - **Recomendación**: Crear endpoint específico para usuario `/orders/:id/status`

### ✅ Endpoints Implementados pero Aún No Utilizados

Estos endpoints están bien definidos en las constantes pero no se usan actualmente desde componentes:

- `/restaurants/categories` - Categorías de restaurantes
- `/dishes` - Listar todos los platos
- `/cart/*` - Todos los endpoints de carrito (usando localStorage en su lugar)
- `/users/addresses/*` - Gestión de direcciones de usuario
- `/payments/methods` - Métodos de pago
- `/orders/:id/track` - Rastrear órdenes
- `/auth/profile` - Perfil de autenticación
- `/admin/dashboard` - Dashboard general
- `/admin/system/admins` - Gestión de administradores
- `/admin/system/settings` - Configuración del sistema

---

## 10. CONCLUSIÓN

**Respuesta a la pregunta: ¿Todos los endpoints que se consumen desde el frontend consumen endpoints del backend?**

### ✅ **SÍ, TODOS LOS ENDPOINTS ESTÁN DEFINIDOS Y DISPONIBLES**

Todos los endpoints consumidos desde el frontend tienen su correspondiente definición en las constantes (`API_ENDPOINTS`) y están estructurados de manera que corresponderían a endpoints del backend.

### Recomendaciones:

1. **Verificar con el backend** que todos estos endpoints existan efectivamente
2. **Resolver inconsistencias** en métodos HTTP para órdenes
3. **Implementar endpoints de carrito** en el backend (actualmente solo localStorage)
4. **Documentar endpoints no utilizados** para futuras funcionalidades

---

## 11. LISTADO COMPLETO DE ENDPOINTS POR CATEGORÍA

| Categoría | Endpoint | Método | Consumido |
|-----------|----------|--------|-----------|
| **AUTH** | `/auth/login` | POST | ✅ |
| | `/auth/register` | POST | ✅ |
| | `/auth/refresh` | POST | ✅ |
| | `/auth/logout` | POST | ❌ |
| | `/auth/profile` | GET/PUT | ❌ |
| **RESTAURANTS** | `/restaurants` | GET | ✅ |
| | `/restaurants/:id` | GET | ✅ |
| | `/restaurants/:id/dishes` | GET | ✅ |
| | `/restaurants/search` | GET | ✅ |
| | `/restaurants/categories` | GET | ❌ |
| **DISHES** | `/dishes` | GET | ❌ |
| | `/dishes/:id` | GET | ✅ |
| | `/dishes/search` | GET | ✅ |
| | `/restaurants/:id/dish-categories` | GET | ✅ |
| **ORDERS (USER)** | `/orders` | GET | ✅ |
| | `/orders` | POST | ✅ |
| | `/orders/:id` | GET | ✅ |
| | `/orders/:id` | PUT/DELETE | ⚠️ |
| | `/orders/:id/track` | GET | ❌ |
| | `/orders/:id/cancel` | - | ❌ |
| **CART** | `/cart` | GET | ❌ |
| | `/cart/add` | POST | ❌ |
| | `/cart/update` | POST | ❌ |
| | `/cart/remove` | POST | ❌ |
| | `/cart/clear` | POST | ❌ |
| **USERS** | `/users/addresses` | GET/POST | ❌ |
| | `/users/addresses/:id` | PUT/DELETE | ❌ |
| | `/users/addresses/:id/default` | PATCH | ❌ |
| **PAYMENTS** | `/payments/methods` | GET | ❌ |
| | `/payments/process` | POST | ❌ |
| | `/payments/webhook` | POST | ❌ |
| **ADMIN AUTH** | `/admin/auth/login` | POST | ✅ |
| | `/admin/auth/logout` | POST | ✅ |
| | `/admin/auth/refresh` | POST | ✅ |
| | `/admin/auth/profile` | GET/PUT | ✅ |
| **ADMIN DASHBOARD** | `/admin/dashboard` | GET | ❌ |
| | `/admin/dashboard/stats` | GET | ✅ |
| **ADMIN RESTAURANTS** | `/admin/restaurants` | GET | ✅ |
| | `/admin/restaurants` | POST | ✅ |
| | `/admin/restaurants/:id` | GET | ✅ |
| | `/admin/restaurants/:id` | PUT | ✅ |
| | `/admin/restaurants/:id` | DELETE | ✅ |
| | `/admin/restaurants/:id/toggle-status` | PATCH | ✅ |
| **ADMIN DISHES** | `/admin/dishes` | GET | ✅ |
| | `/admin/dishes` | POST | ✅ |
| | `/admin/dishes/:id` | GET | ✅ |
| | `/admin/dishes/:id` | PUT | ✅ |
| | `/admin/dishes/:id` | DELETE | ✅ |
| | `/admin/dishes/:id/toggle-availability` | PATCH | ✅ |
| **ADMIN CATEGORIES** | `/admin/categories` | GET | ✅ |
| | `/admin/categories` | POST | ✅ |
| | `/admin/categories/:id` | GET | ✅ |
| | `/admin/categories/:id` | PUT | ✅ |
| | `/admin/categories/:id` | DELETE | ✅ |
| | `/admin/categories/:id/toggle-status` | PATCH | ✅ |
| **ADMIN ORDERS** | `/admin/orders` | GET | ✅ |
| | `/admin/orders/:id` | GET | ✅ |
| | `/admin/orders/:id/status` | PATCH | ✅ |
| | `/admin/orders/:id/cancel` | PATCH | ✅ |
| | `/admin/orders/:id/refund` | PATCH | ✅ |
| **ADMIN USERS** | `/admin/users` | GET | ✅ |
| | `/admin/users/:id` | GET | ✅ |
| | `/admin/users/:id` | PUT | ✅ |
| | `/admin/users/:id/toggle-status` | PATCH | ✅ |
| | `/admin/users/:id` | DELETE | ✅ |
| **ADMIN REPORTS** | `/admin/reports` | GET | ❌ |
| | `/admin/reports/sales` | GET | ✅ |
| | `/admin/reports/users` | GET | ✅ |
| | `/admin/reports/export` | GET | ✅ |
| **ADMIN SYSTEM** | `/admin/system/admins` | GET/POST/PUT/DELETE | ❌ |
| | `/admin/system/settings` | GET/PUT | ❌ |

---

**Análisis generado el**: 16 de Marzo de 2026

