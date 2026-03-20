# Delivery Admin Frontend

Panel de administración construido con **React Admin 5** + **Vite** + **TypeScript** que consume la API REST del `spring-backend`.

Configuración base local:

- Frontend admin: `http://localhost:3001`
- API backend esperada: `http://localhost:8080/api`

---

## Índice

1. [Stack tecnológico](#stack-tecnológico)
2. [Puesta en marcha](#puesta-en-marcha)
3. [Arquitectura del proyecto](#arquitectura-del-proyecto)
4. [Recursos implementados](#recursos-implementados)
5. [Data Provider](#data-provider)
6. [Cómo agregar un nuevo recurso](#cómo-agregar-un-nuevo-recurso)
7. [Variables de entorno](#variables-de-entorno)
8. [Build para producción](#build-para-producción)

---

## Stack tecnológico

| Paquete | Versión | Propósito |
|---|---|---|
| `react` | 18 | UI |
| `react-admin` | 5 | Framework de administración |
| `@mui/material` | 6 | Componentes UI (usado internamente por react-admin) |
| `@mui/icons-material` | 6 | Iconos del menú lateral |
| `vite` | 5 | Bundler y servidor de desarrollo |
| `typescript` | 5 | Tipado estático |

---

## Puesta en marcha

### Prerrequisitos

- Node.js ≥ 18
- Spring Backend corriendo en `http://localhost:8080`

### Instalación y arranque

```bash
cd admin-frontend
npm install
npm run dev        # http://localhost:3001
```

El servidor de Vite hace proxy de todas las peticiones `/api/*` hacia `http://localhost:8080`, por lo que no hay problemas de CORS.

### Scripts disponibles

| Comando | Descripción |
|---|---|
| `npm run dev` | Servidor de desarrollo en `http://localhost:3001` |
| `npm run build` | Compila TypeScript y genera el bundle en `dist/` |
| `npm run preview` | Sirve el bundle de producción localmente |

---

## Arquitectura del proyecto

```
admin-frontend/
├── index.html
├── vite.config.ts          # Proxy /api → http://localhost:8080
├── tsconfig.json
├── package.json
└── src/
    ├── main.tsx            # Punto de entrada React
    ├── App.tsx             # <Admin> con todos los <Resource> registrados
    ├── dataProvider.ts     # Adaptador HTTP para la API REST del spring-backend
    └── resources/
        ├── restaurants.tsx     # Restaurantes (CRUD completo)
        ├── users.tsx           # Usuarios (CRUD completo)
        ├── couriers.tsx        # Repartidores (CRUD completo)
        ├── addresses.tsx       # Direcciones (CRUD completo)
        ├── dishes.tsx          # Platos + Categorías menú + Opciones + Disponibilidad
        ├── orders.tsx          # Pedidos + Ítems + Historial + Seguimiento + Incidencias
        ├── reviews.tsx         # Reseñas de repartidores y platos
        ├── financial.tsx       # Métodos pago + Pagos + Cód. referido + Propinas + Transacciones + Wallets
        └── misc.tsx            # Notificaciones + Tokens + Auditoría (solo lectura)
```

### Flujo de datos

```
Navegador → Vite proxy (/api) → Spring Backend (8080/api)
    ↕
React Admin ← dataProvider.ts (fetch + paginación/filtro client-side)
```

---

## Recursos implementados

### Core (CRUD completo: listar, crear, editar, eliminar)

| Resource name (React Admin) | Endpoint API | Archivo |
|---|---|---|
| `restaurants` | `GET/POST/PUT/DELETE /restaurants` | `restaurants.tsx` |
| `users` | `GET/POST/PUT/DELETE /users` | `users.tsx` |
| `couriers` | `GET/POST/PUT/DELETE /couriers` | `couriers.tsx` |
| `addresses` | `GET/POST/PUT/DELETE /addresses` | `addresses.tsx` |
| `dishes` | `GET/POST/PUT/DELETE /dishes` | `dishes.tsx` |
| `menu-categories` | `GET/POST/PUT/DELETE /menu-categories` | `dishes.tsx` |
| `referral-codes` | `GET/POST/PUT/DELETE /referral-codes` | `financial.tsx` |

### Lista + Crear + Eliminar (sin edición)

| Resource name | Endpoint API | Archivo |
|---|---|---|
| `dish-options` | `GET/POST/DELETE /dish-options` | `dishes.tsx` |
| `dish-option-values` | `GET/POST/DELETE /dish-option-values` | `dishes.tsx` |
| `dish-availability` | `GET/POST/DELETE /dish-availability` | `dishes.tsx` |
| `order-items` | `GET/POST/DELETE /order-items` | `orders.tsx` |
| `order-issues` | `GET/POST/DELETE /order-issues` | `orders.tsx` |
| `payment-methods` | `GET/POST/DELETE /payment-methods` | `financial.tsx` |

### Lista + Crear (sin edición ni eliminación)

| Resource name | Endpoint API | Archivo |
|---|---|---|
| `orders` | `GET/POST/PUT/DELETE /orders` | `orders.tsx` |
| `order-status-history` | `GET/POST /order-status-history` | `orders.tsx` |
| `order-tracking` | `GET/POST /order-tracking` | `orders.tsx` |
| `courier-reviews` | `GET/POST /courier-reviews` | `reviews.tsx` |
| `dish-reviews` | `GET/POST /dish-reviews` | `reviews.tsx` |
| `payouts` | `GET/POST /payouts` | `financial.tsx` |
| `tips` | `GET/POST /tips` | `financial.tsx` |
| `transactions` | `GET/POST /transactions` | `financial.tsx` |
| `user-wallets` | `GET/POST /user-wallets` | `financial.tsx` |
| `wallet-transactions` | `GET/POST /wallet-transactions` | `financial.tsx` |
| `notifications` | `GET/POST /notifications` | `misc.tsx` |
| `notification-tokens` | `GET/POST /notification-tokens` | `misc.tsx` |

### Solo lectura

| Resource name | Endpoint API | Archivo |
|---|---|---|
| `audit-logs` | `GET /audit-logs` | `misc.tsx` |

---

## Data Provider

El archivo `src/dataProvider.ts` implementa el contrato `DataProvider` de React Admin adaptado al Spring Backend, que **no** devuelve cabeceras de paginación estándar sino arrays planos.

### Características

- **Paginación client-side**: descarga todos los registros y pagina localmente.
- **Filtrado client-side**: filtra por cualquier campo usando `contains` (case-insensitive).
- **Ordenación client-side**: ordena por cualquier campo ascendente/descendente.
- **`getOne` con fallback**: intenta `GET /{id}` y si el endpoint no existe (404/405) hace `GET /` y busca por id en el array.
- **Variable de entorno**: la URL base se puede cambiar con `VITE_API_URL` (por defecto `/api`).

### Métodos implementados

| Método React Admin | Comportamiento |
|---|---|
| `getList` | `GET /{resource}` + paginación/filtro/orden client-side |
| `getOne` | `GET /{resource}/{id}` con fallback a lista |
| `getMany` | Múltiples `GET /{resource}/{id}` en paralelo |
| `getManyReference` | `GET /{resource}` filtrado por campo de referencia |
| `create` | `POST /{resource}` |
| `update` | `PUT /{resource}/{id}` |
| `delete` | `DELETE /{resource}/{id}` |
| `deleteMany` | Múltiples `DELETE /{resource}/{id}` en paralelo |

---

## Cómo agregar un nuevo recurso

Sigue estos 3 pasos para registrar un nuevo maestro, por ejemplo `promotions` con endpoints `GET/POST/PUT/DELETE /promotions`.

### Paso 1 — Crear el archivo de recurso

Crea `src/resources/promotions.tsx` (o añádelo a un archivo existente si es un recurso pequeño relacionado con otro):

```tsx
import {
  List, Datagrid, TextField, NumberField, BooleanField, DateField,
  Edit, SimpleForm, TextInput, NumberInput, BooleanInput, DateInput, SelectInput,
  Create, EditButton, DeleteButton,
} from 'react-admin';

// ── List ──────────────────────────────────────────────────────────────────────
export const PromotionList = () => (
  <List perPage={25} sort={{ field: 'id', order: 'DESC' }}>
    <Datagrid bulkActionButtons={false}>
      <NumberField source="id" />
      <TextField source="code" label="Código" />
      <NumberField source="discountPercent" label="% Descuento" />
      <BooleanField source="isActive" label="Activo" />
      <DateField source="expiresAt" label="Expira" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

// ── Form (compartido entre Edit y Create) ─────────────────────────────────────
const PromotionForm = () => (
  <>
    <TextInput source="code" label="Código promocional" fullWidth />
    <NumberInput source="discountPercent" label="Porcentaje de descuento" />
    <NumberInput source="maxUses" label="Máximo de usos" />
    <DateInput source="expiresAt" label="Fecha de expiración" />
    <BooleanInput source="isActive" label="Activo" />
  </>
);

// ── Edit ──────────────────────────────────────────────────────────────────────
export const PromotionEdit = () => (
  <Edit>
    <SimpleForm><PromotionForm /></SimpleForm>
  </Edit>
);

// ── Create ────────────────────────────────────────────────────────────────────
export const PromotionCreate = () => (
  <Create redirect="list">
    <SimpleForm><PromotionForm /></SimpleForm>
  </Create>
);
```

#### Referencia de componentes según los endpoints disponibles

| El backend tiene… | Incluye en el archivo |
|---|---|
| `GET` + `GET/{id}` + `POST` + `PUT/{id}` + `DELETE/{id}` | `List` + `Edit` + `Create` con `EditButton` y `DeleteButton` |
| `GET` + `POST` + `DELETE/{id}` (sin PUT) | `List` + `Create` solo con `DeleteButton` |
| `GET` + `POST` (sin DELETE ni PUT) | `List` + `Create` sin botones de acción |
| Solo `GET` | Solo `List` con `bulkActionButtons={false}` |

#### Componentes de campo más usados

| Componente | Uso |
|---|---|
| `<TextField source="name" />` | Texto |
| `<NumberField source="amount" />` | Número |
| `<BooleanField source="isActive" />` | Booleano (icono check/cruz) |
| `<DateField source="createdAt" showTime />` | Fecha y hora |
| `<EmailField source="email" />` | Email (con enlace `mailto:`) |
| `<TextInput source="name" fullWidth />` | Input texto en formulario |
| `<NumberInput source="price" />` | Input numérico |
| `<BooleanInput source="isActive" />` | Toggle en formulario |
| `<DateInput source="expiresAt" />` | Selector de fecha |
| `<SelectInput source="status" choices={[...]} />` | Select con opciones fijas |

### Paso 2 — Registrar en `App.tsx`

Importa los componentes y añade el `<Resource>` en el bloque correspondiente:

```tsx
// 1. Añade el import al inicio del archivo
import { PromotionList, PromotionEdit, PromotionCreate } from './resources/promotions';

// 2. Añade el icono (opcional, desde @mui/icons-material)
import LocalOfferIcon from '@mui/icons-material/LocalOffer';

// 3. Registra el recurso dentro de <Admin>
<Resource
  name="promotions"            // debe coincidir exactamente con el path del API
  list={PromotionList}
  edit={PromotionEdit}         // omitir si el API no tiene PUT
  create={PromotionCreate}     // omitir si el API no tiene POST
  icon={LocalOfferIcon}
  options={{ label: 'Promociones' }}   // etiqueta en el menú lateral
/>
```

> **Nota sobre `name`:** React Admin usa este valor tanto para las rutas del admin (`/#/promotions`) como para llamar al `dataProvider`. El dataProvider construye la URL `GET /api/promotions` a partir de él, así que debe coincidir con el path del endpoint del Spring Backend (en minúsculas y con guiones).

### Paso 3 — Verificar en el navegador

1. Asegúrate de que el Spring Backend está corriendo: `http://localhost:8080/api/promotions` debe responder.
2. Arranca (o recarga) el admin: `npm run dev`.
3. El nuevo recurso aparecerá automáticamente en el menú lateral.

---

### Casos especiales

#### Recurso sin `GET /{id}` en el backend

El `dataProvider` ya maneja esto: si `GET /promotions/5` devuelve 404 o 405, descarga toda la lista con `GET /promotions` y busca el registro por `id` en memoria. No necesitas hacer nada extra.

#### Agregar filtro de búsqueda a la lista

Añade `<FilterForm>` usando el componente `<SearchInput>` de react-admin:

```tsx
import { List, Datagrid, SearchInput, FilterForm, TextInput } from 'react-admin';

const promotionFilters = [
  <SearchInput source="q" alwaysOn />,          // búsqueda global
  <TextInput source="code" label="Código" />,   // filtro por campo
];

export const PromotionList = () => (
  <List filters={promotionFilters} perPage={25}>
    <Datagrid>
      {/* ... columnas ... */}
    </Datagrid>
  </List>
);
```

#### Poner un recurso en un grupo del menú

React Admin 5 permite agrupar recursos con `<ResourceDefinitionContext>` o usando la prop `options.label` con un separador visual. Para un agrupamiento sencillo, ordena los `<Resource>` en `App.tsx` de forma lógica (ya están agrupados por comentarios: *Core*, *Dishes & Menu*, *Orders*, *Reviews*, *Financial*, *Misc*).

---

## Variables de entorno

Crea un archivo `.env` en la raíz del proyecto para personalizar la URL del backend:

```env
VITE_API_URL=http://localhost:8080/api
```

Por defecto, si no se define, se usa `/api` (funciona con el proxy de Vite en desarrollo).

Para producción (sin proxy), define la URL completa:

```env
VITE_API_URL=https://tu-dominio.com/api
```

---

## Build para producción

```bash
npm run build     # genera dist/
npm run preview   # sirve dist/ localmente para verificar
```

El bundle generado en `dist/` puede desplegarse en cualquier servidor estático (Nginx, S3, Vercel, etc.). Recuerda configurar `VITE_API_URL` con la URL real del backend y asegurarte de que el servidor tenga habilitados los headers CORS para el dominio del admin.

