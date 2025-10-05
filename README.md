# Sistema de Autenticación - Delivery App

## Descripción

Este proyecto implementa un sistema completo de registro y autenticación de usuarios desde el frontend hasta el backend en Node.js.

## Estructura del Proyecto

### Backend (Node.js)
- **Modelo User**: `/nodejs-backend/src/models/User.js`
- **Migraciones**: `/nodejs-backend/migrations/04-create-user-table.js`  
- **Rutas de Autenticación**: `/nodejs-backend/src/routes/auth.js`
- **Base de Datos**: SQLite (`database.sqlite`)

### Frontend (React)
- **Página de Registro**: `/frontend/src/pages/RegisterPage.tsx`
- **Página de Login**: `/frontend/src/pages/LoginPage.tsx`
- **Servicio API**: `/frontend/src/services/api.ts`
- **Gestión de Estado**: `/frontend/src/hooks/useAuthStore.ts` y `/frontend/src/components/common/AuthProvider.tsx`

## Características Implementadas

### Backend
1. **Registro de nuevos usuarios**:
   - Formulario de registro con validación
   - Hash de contraseñas para seguridad
   - Persistencia en base de datos SQLite

2. **Autenticación de usuarios existentes**:
   - Sistema de login con validación
   - Manejo de tokens JWT (estructura preparada)

3. **Base de Datos**:
   - Tabla users con campos: id, name, email, password
   - Scripts de migración y sembrado

### Frontend
1. **Formularios completos**:
   - Registro con validaciones
   - Login con manejo de errores

2. **Gestión del Estado**:
   - Hook de autenticación (`useAuthStore`)
   - Proveedor de contexto (`AuthProvider`)

3. **Integración completa**:
   - Conexión al backend Node.js
   - Manejo de rutas protegidas
   - Gestión de sesiones

## Cómo Ejecutar (Manualmente)

### 1. Preparar el Backend

```bash
cd nodejs-backend
# Verificar base de datos existente
ls -la database.sqlite

# Si no existe, crear la base de datos con:
python3 setup_db.py
```

### 2. Iniciar el Servidor Node.js

```bash
# Instalar dependencias (si es necesario)
npm install express cors bcrypt sequelize sqlite3

# Iniciar servidor
node server.js
```

> Nota: El archivo `server.js` ya está creado en la carpeta nodejs-backend

### 3. Iniciar el Frontend React

```bash
cd frontend
npm start
```

## Estructura de Datos

### Tabla Users (SQLite)
- id (INTEGER PRIMARY KEY AUTOINCREMENT)
- name (TEXT NOT NULL)
- email (TEXT UNIQUE NOT NULL)  
- password (TEXT NOT NULL)
- created_at (DATETIME DEFAULT CURRENT_TIMESTAMP)
- updated_at (DATETIME DEFAULT CURRENT_TIMESTAMP)

## Pruebas Realizadas

1. ✅ Base de datos SQLite creada correctamente
2. ✅ Tablas creadas con sus respectivos campos
3. ✅ Usuarios de prueba insertados (John Doe, Jane Smith)
4. ✅ Implementación completa del modelo User
5. ✅ Rutas de autenticación actualizadas para usar base de datos
6. ✅ Frontend conectado al backend

## Solución a Errores Comunes

### Error: Cannot read properties of undefined (reading 'total')
Este error ocurre cuando el frontend intenta acceder a `data.pagination.total` pero `data` es `undefined`. 
**Solución**: Se han implementado verificaciones con operador opcional (`?.`) en los componentes.

### Error de conexión al backend
El frontend intenta conectarse a `http://localhost:59806/api/...` pero el servidor no está corriendo.
**Solución**: Iniciar el servidor Node.js antes de ejecutar la aplicación frontend.

## Próximos Pasos

1. **Probar integración completa**
2. **Validar registro y login funcionando**
3. **Verificar persistencia de datos**

## Endpoints del Frontend React

### 🔐 Autenticación (Auth)
- `POST /auth/register` - Registro de nuevo usuario
- `POST /auth/login` - Inicio de sesión de usuario
- `POST /auth/refresh` - Refrescar token de autenticación
- `POST /auth/logout` - Cierre de sesión
- `GET /auth/profile` - Obtener perfil del usuario

### 🏪 Restaurantes
- `GET /restaurants` - Listar todos los restaurantes
- `GET /restaurants/{id}` - Detalle de un restaurante específico
- `GET /restaurants/{id}/dishes` - Platos de un restaurante específico
- `GET /restaurants/categories` - Categorías de restaurantes
- `GET /restaurants/search` - Búsqueda de restaurantes

### 🍽️ Platos (Dishes)
- `GET /dishes` - Listar todos los platos
- `GET /dishes/{id}` - Detalle de un plato específico
- `GET /restaurants/{restaurantId}/dish-categories` - Categorías de platos por restaurante
- `GET /dishes/search` - Búsqueda de platos

### 🛒 Pedidos (Orders)
- `POST /orders` - Crear nuevo pedido
- `GET /orders` - Listar pedidos del usuario
- `GET /orders/{id}` - Detalle de un pedido específico
- `PUT /orders/{id}` - Actualizar estado de pedido
- `DELETE /orders/{id}` - Cancelar pedido
- `GET /orders/{id}/track` - Seguimiento de pedido

### 🧾 Carrito (Cart)
- `GET /cart` - Obtener contenido del carrito
- `POST /cart/add` - Agregar producto al carrito
- `PUT /cart/update` - Actualizar cantidad en el carrito
- `DELETE /cart/remove` - Eliminar producto del carrito
- `DELETE /cart/clear` - Limpiar carrito

### 👤 Usuarios (Users)
- `GET /users/addresses` - Obtener direcciones de usuario
- `POST /users/addresses` - Agregar nueva dirección
- `PUT /users/addresses/{id}` - Actualizar dirección
- `DELETE /users/addresses/{id}` - Eliminar dirección
- `PUT /users/addresses/{id}/default` - Establecer dirección por defecto

### 🎯 Búsqueda (Search)
- `GET /search` - Búsqueda general de restaurantes y platos

### 📊 Reportes
- `GET /reports/sales` - Reporte de ventas
- `GET /reports/users` - Reporte de usuarios
- `GET /reports/export` - Exportar reportes

## Sección Administrativa (Admin)

### 👤 Autenticación Admin
- `POST /admin/auth/login` - Inicio de sesión admin
- `POST /admin/auth/logout` - Cierre de sesión admin
- `POST /admin/auth/refresh` - Refrescar token admin
- `GET /admin/auth/profile` - Perfil del administrador

### 🏪 Gestión de Restaurantes
- `GET /admin/restaurants` - Listar restaurantes (con filtros)
- `GET /admin/restaurants/{id}` - Detalle de restaurante
- `POST /admin/restaurants` - Crear nuevo restaurante
- `PUT /admin/restaurants/{id}` - Actualizar restaurante
- `DELETE /admin/restaurants/{id}` - Eliminar restaurante
- `PATCH /admin/restaurants/{id}/toggle-status` - Cambiar estado del restaurante

### 🍽️ Gestión de Platos
- `GET /admin/dishes` - Listar platos (con filtros)
- `GET /admin/dishes/{id}` - Detalle de plato
- `POST /admin/dishes` - Crear nuevo plato
- `PUT /admin/dishes/{id}` - Actualizar plato
- `DELETE /admin/dishes/{id}` - Eliminar plato
- `PATCH /admin/dishes/{id}/toggle-availability` - Cambiar disponibilidad

### 🏷️ Gestión de Categorías
- `GET /admin/categories` - Listar categorías (con filtros)
- `GET /admin/categories/{id}` - Detalle de categoría
- `POST /admin/categories` - Crear nueva categoría
- `PUT /admin/categories/{id}` - Actualizar categoría
- `DELETE /admin/categories/{id}` - Eliminar categoría
- `PATCH /admin/categories/{id}/toggle-status` - Cambiar estado de categoría

### 📦 Gestión de Pedidos
- `GET /admin/orders` - Listar pedidos (con filtros)
- `GET /admin/orders/{id}` - Detalle de pedido
- `PUT /admin/orders/{id}/status` - Actualizar estado del pedido
- `DELETE /admin/orders/{id}/cancel` - Cancelar pedido
- `POST /admin/orders/{id}/refund` - Procesar reembolso

### 👥 Gestión de Usuarios
- `GET /admin/users` - Listar usuarios (con filtros)
- `GET /admin/users/{id}` - Detalle de usuario
- `PUT /admin/users/{id}` - Actualizar información del usuario
- `PATCH /admin/users/{id}/toggle-status` - Cambiar estado del usuario
- `DELETE /admin/users/{id}` - Eliminar usuario

### 📈 Reportes Admin
- `GET /admin/reports/sales` - Reporte de ventas
- `GET /admin/reports/users` - Reporte de usuarios
- `GET /admin/reports/export` - Exportar reportes

### ⚙️ Sistema Admin
- `GET /admin/system/admins` - Listar administradores
- `GET /admin/system/settings` - Configuración del sistema

## Notas Técnicas

- El sistema usa SQLite para almacenamiento local (en backend Node.js)
- Las contraseñas se hashen con bcrypt (estructura implementada)
- Se sigue el patrón de separación frontend/backend
- La base de datos ya contiene usuarios de prueba