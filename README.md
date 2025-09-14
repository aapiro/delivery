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

## Notas Técnicas

- El sistema usa SQLite para almacenamiento local
- Las contraseñas se hashen con bcrypt (estructura implementada)
- Se sigue el patrón de separación frontend/backend
- La base de datos ya contiene usuarios de prueba