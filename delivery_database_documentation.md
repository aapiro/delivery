# 📊 Documentación del Modelo de Datos - Sistema de Delivery

> **Versión:** 1.0  
> **Última actualización:** Diciembre 2024  
> **Base de datos:** PostgreSQL 15+

---

## 📑 Índice

1. [Gestión de Usuarios](#1-gestión-de-usuarios)
2. [Restaurantes y Menú](#2-restaurantes-y-menú)
3. [Pedidos y Transacciones](#3-pedidos-y-transacciones)
4. [Couriers y Entregas](#4-couriers-y-entregas)
5. [Pagos y Finanzas](#5-pagos-y-finanzas)
6. [Marketing y Promociones](#6-marketing-y-promociones)
7. [Reviews y Ratings](#7-reviews-y-ratings)
8. [Notificaciones](#8-notificaciones)
9. [Analytics y Auditoría](#9-analytics-y-auditoría)
10. [Tipos de Datos Personalizados](#10-tipos-de-datos-personalizados)

---

## 1. Gestión de Usuarios

### 👤 `users`
Tabla principal de usuarios del sistema.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único del usuario | `1001` |
| `email` | VARCHAR(255) | Email único | `juan@gmail.com` |
| `phone` | VARCHAR(20) | Teléfono único | `+34612345678` |
| `full_name` | VARCHAR(255) | Nombre completo | `Juan Pérez García` |
| `user_type` | VARCHAR(20) | Tipo de usuario | `CUSTOMER`, `RESTAURANT_OWNER`, `COURIER`, `ADMIN` |
| `is_verified` | BOOLEAN | Si el usuario está verificado | `true` |
| `is_active` | BOOLEAN | Si la cuenta está activa | `true` |
| `created_at` | TIMESTAMP | Fecha de registro | `2024-01-15 10:30:00` |

**Ejemplo de uso:**
```sql
-- Crear un cliente nuevo
INSERT INTO users (email, phone, full_name, user_type) 
VALUES ('maria@gmail.com', '+34600111222', 'María López', 'CUSTOMER');

-- Buscar usuarios activos por tipo
SELECT * FROM users 
WHERE user_type = 'CUSTOMER' AND is_active = true;
```

**Relaciones:**
- Uno a muchos con `orders` (como cliente)
- Uno a muchos con `addresses`
- Uno a uno con `user_wallets`
- Uno a muchos con `payment_methods`
- Uno a uno con `couriers` (si es courier)

---

### 🏠 `addresses`
Direcciones de usuarios y restaurantes.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `50` |
| `user_id` | BIGINT | Usuario propietario | `1001` |
| `restaurant_id` | BIGINT | Restaurante (si aplica) | `42` |
| `street` | VARCHAR(100) | Calle | `Calle Mayor` |
| `city` | VARCHAR(100) | Ciudad | `Madrid` |
| `state` | VARCHAR(100) | Provincia/Estado | `Madrid` |
| `zipCode` | VARCHAR(6) | Código postal | `28013` |
| `country` | VARCHAR(100) | País | `España` |
| `address_line` | TEXT | Dirección completa | `Calle Mayor 10, 3º B` |
| `latitude` | DECIMAL(9,6) | Latitud | `40.416775` |
| `longitude` | DECIMAL(9,6) | Longitud | `-3.703790` |
| `place_id` | VARCHAR(255) | ID de Google Places | `ChIJgTwKgJQpQg0RaSKMYcHeNsQ` |
| `formatted_address` | TEXT | Dirección formateada | `Calle Mayor, 10, 28013 Madrid, España` |
| `address_type` | VARCHAR(20) | Tipo | `HOME`, `WORK`, `OTHER` |
| `floor` | VARCHAR(10) | Piso | `3º` |
| `apartment` | VARCHAR(10) | Puerta | `B` |
| `delivery_instructions` | TEXT | Instrucciones | `Llamar al portero automático 3B` |
| `is_verified` | BOOLEAN | Si está verificada | `true` |
| `is_default` | BOOLEAN | Si es la dirección por defecto | `true` |

**Ejemplo de uso:**
```sql
-- Agregar dirección de casa
INSERT INTO addresses (
    user_id, street, city, zipCode, country, 
    address_type, floor, apartment, is_default, 
    latitude, longitude, delivery_instructions
) VALUES (
    1001, 'Calle Mayor 10', 'Madrid', '28013', 'España',
    'HOME', '3º', 'B', true,
    40.416775, -3.703790, 'Llamar al 3B por el portero'
);

-- Obtener dirección por defecto de un usuario
SELECT * FROM addresses 
WHERE user_id = 1001 AND is_default = true;
```

---

## 2. Restaurantes y Menú

### 🍽️ `restaurants`
Información de restaurantes registrados.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `42` |
| `name` | VARCHAR(255) | Nombre del restaurante | `Burger King Sol` |
| `description` | TEXT | Descripción | `Las mejores hamburguesas de Madrid` |
| `image_url` | VARCHAR(255) | URL de la imagen | `https://cdn.example.com/bk.jpg` |
| `is_open` | BOOLEAN | Si está abierto ahora | `true` |
| `is_active` | BOOLEAN | Si está activo en la plataforma | `true` |
| `minimum_order` | DECIMAL(10,2) | Pedido mínimo en € | `10.00` |
| `delivery_fee` | DECIMAL(10,2) | Tarifa de envío base | `2.50` |
| `delivery_time_min` | INT | Tiempo mínimo entrega (min) | `25` |
| `delivery_time_max` | INT | Tiempo máximo entrega (min) | `35` |
| `rating` | DECIMAL(3,2) | Valoración promedio | `4.50` |
| `review_count` | INT | Número de reseñas | `1523` |
| `max_active_orders` | INT | Máximo pedidos simultáneos | `20` |
| `avg_preparation_time` | INT | Tiempo promedio preparación | `18` |
| `avg_delivery_time` | INT | Tiempo promedio entrega | `28` |
| `cancellation_rate` | DECIMAL(5,2) | % de cancelaciones | `2.50` |
| `quality_score` | DECIMAL(5,2) | Puntuación de calidad | `95.00` |
| `search_vector` | TSVECTOR | Vector de búsqueda full-text | Auto-generado |
| `deleted_at` | TIMESTAMP | Fecha de eliminación (soft delete) | `NULL` |
| `created_at` | TIMESTAMP | Fecha de creación | `2023-06-01` |
| `updated_at` | TIMESTAMP | Última actualización | `2024-12-27` |

**Ejemplo de uso:**
```sql
-- Crear restaurante
INSERT INTO restaurants (
    name, description, minimum_order, delivery_fee, 
    delivery_time_min, delivery_time_max
) VALUES (
    'Pizzería Napoli', 
    'Auténtica pizza italiana',
    8.00, 2.00, 30, 45
);

-- Buscar restaurantes abiertos cerca (requiere extensión PostGIS)
SELECT * FROM restaurants 
WHERE is_open = true AND is_active = true
ORDER BY rating DESC, review_count DESC
LIMIT 20;

-- Búsqueda full-text
SELECT * FROM restaurants 
WHERE search_vector @@ to_tsquery('spanish', 'pizza & italiana')
ORDER BY ts_rank(search_vector, to_tsquery('spanish', 'pizza')) DESC;
```

---

### 🏷️ `cuisine_types`
Tipos de cocina globales (ej: Italiana, Mexicana, China).

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `5` |
| `name` | VARCHAR(100) | Nombre único | `Italiana`, `Mexicana`, `China` |

**Ejemplo de uso:**
```sql
-- Insertar tipos de cocina
INSERT INTO cuisine_types (name) VALUES 
    ('Italiana'), ('Mexicana'), ('China'), 
    ('Japonesa'), ('Española'), ('Americana');
```

---

### 🔗 `restaurant_cuisines`
Relación muchos a muchos entre restaurantes y tipos de cocina.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `restaurant_id` | BIGINT | ID del restaurante |
| `cuisine_id` | BIGINT | ID del tipo de cocina |

**Ejemplo de uso:**
```sql
-- Asignar cocinas a un restaurante
INSERT INTO restaurant_cuisines (restaurant_id, cuisine_id) VALUES
    (42, 1),  -- Italiana
    (42, 5);  -- Española

-- Buscar restaurantes por tipo de cocina
SELECT r.* FROM restaurants r
JOIN restaurant_cuisines rc ON r.id = rc.restaurant_id
WHERE rc.cuisine_id = 1;  -- Italiana
```

---

### 📋 `menu_categories`
Categorías del menú de cada restaurante.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `100` |
| `restaurant_id` | BIGINT | ID del restaurante | `42` |
| `name` | VARCHAR(255) | Nombre de la categoría | `Hamburguesas`, `Entrantes` |
| `slug` | VARCHAR(255) | URL-friendly name | `hamburguesas`, `entrantes` |
| `icon` | VARCHAR(255) | URL del ícono | `🍔` |
| `display_order` | INT | Orden de visualización | `1`, `2`, `3` |
| `is_active` | BOOLEAN | Si está activa | `true` |
| `created_at` | TIMESTAMP | Fecha creación | `2024-01-01` |
| `updated_at` | TIMESTAMP | Última actualización | `2024-12-27` |

**Ejemplo de uso:**
```sql
-- Crear categorías para un restaurante
INSERT INTO menu_categories (restaurant_id, name, slug, display_order) VALUES
    (42, 'Hamburguesas', 'hamburguesas', 1),
    (42, 'Ensaladas', 'ensaladas', 2),
    (42, 'Postres', 'postres', 3);

-- Obtener categorías de un restaurante ordenadas
SELECT * FROM menu_categories 
WHERE restaurant_id = 42 AND is_active = true
ORDER BY display_order;
```

---

### 🍕 `dishes`
Platos individuales del menú.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `2001` |
| `restaurant_id` | BIGINT | ID del restaurante | `42` |
| `category_id` | BIGINT | ID de categoría | `100` |
| `name` | VARCHAR(255) | Nombre del plato | `Whopper Cheese` |
| `description` | TEXT | Descripción | `Hamburguesa con queso cheddar...` |
| `price` | DECIMAL(10,2) | Precio en € | `7.95` |
| `image_url` | VARCHAR(255) | URL de imagen | `https://cdn.example.com/whopper.jpg` |
| `is_available` | BOOLEAN | Disponible ahora | `true` |
| `is_popular` | BOOLEAN | Si es plato popular | `true` |
| `preparation_time` | INT | Tiempo preparación (min) | `10` |
| `stock` | INT | Stock disponible | `50` |
| `stock_unlimited` | BOOLEAN | Stock ilimitado | `false` |
| `popularity_score` | DECIMAL(5,2) | Puntuación popularidad | `85.50` |
| `search_vector` | TSVECTOR | Vector búsqueda | Auto-generado |
| `deleted_at` | TIMESTAMP | Soft delete | `NULL` |
| `created_at` | TIMESTAMP | Creación | `2024-01-15` |
| `updated_at` | TIMESTAMP | Actualización | `2024-12-27` |

**Ejemplo de uso:**
```sql
-- Crear plato
INSERT INTO dishes (
    restaurant_id, category_id, name, description, 
    price, is_popular, preparation_time, stock_unlimited
) VALUES (
    42, 100, 'Whopper Cheese',
    'Hamburguesa doble con queso cheddar, lechuga, tomate y cebolla',
    7.95, true, 10, true
);

-- Buscar platos disponibles de un restaurante
SELECT d.*, mc.name as category_name
FROM dishes d
JOIN menu_categories mc ON d.category_id = mc.id
WHERE d.restaurant_id = 42 
  AND d.is_available = true 
  AND d.deleted_at IS NULL
ORDER BY d.is_popular DESC, d.popularity_score DESC;

-- Buscar platos por nombre
SELECT * FROM dishes 
WHERE search_vector @@ to_tsquery('spanish', 'hamburguesa & queso')
  AND is_available = true;
```

---

### ⏰ `dish_availability`
Disponibilidad temporal de platos (ej: menú del día).

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `dish_id` | BIGINT | ID del plato | `2001` |
| `day_of_week` | INT | Día (0=Domingo, 6=Sábado) | `1` (Lunes) |
| `start_time` | TIME | Hora inicio | `08:00:00` |
| `end_time` | TIME | Hora fin | `12:00:00` |

**Ejemplo de uso:**
```sql
-- Menú desayuno solo de lunes a viernes 8:00-12:00
INSERT INTO dish_availability (dish_id, day_of_week, start_time, end_time) 
VALUES 
    (2001, 1, '08:00', '12:00'),  -- Lunes
    (2001, 2, '08:00', '12:00'),  -- Martes
    (2001, 3, '08:00', '12:00'),  -- Miércoles
    (2001, 4, '08:00', '12:00'),  -- Jueves
    (2001, 5, '08:00', '12:00');  -- Viernes
```

---

### 🎛️ `dish_options` y `dish_option_values`
Opciones y extras para platos.

**`dish_options`:**

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `50` |
| `dish_id` | BIGINT | ID del plato | `2001` |
| `name` | VARCHAR(100) | Nombre opción | `Tamaño`, `Extras` |
| `required` | BOOLEAN | Si es obligatorio | `true` |

**`dish_option_values`:**

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `200` |
| `option_id` | BIGINT | ID de la opción | `50` |
| `name` | VARCHAR(100) | Nombre valor | `Pequeño`, `Mediano`, `Grande` |
| `extra_price` | DECIMAL(10,2) | Precio extra | `0.00`, `1.50`, `2.50` |

**Ejemplo de uso:**
```sql
-- Crear opción de tamaño (obligatoria)
INSERT INTO dish_options (dish_id, name, required) 
VALUES (2001, 'Tamaño', true);

-- Agregar valores
INSERT INTO dish_option_values (option_id, name, extra_price) VALUES
    (50, 'Pequeño', 0.00),
    (50, 'Mediano', 1.50),
    (50, 'Grande', 2.50);

-- Crear extras opcionales
INSERT INTO dish_options (dish_id, name, required) 
VALUES (2001, 'Extras', false);

INSERT INTO dish_option_values (option_id, name, extra_price) VALUES
    (51, 'Queso extra', 1.00),
    (51, 'Bacon', 1.50),
    (51, 'Doble carne', 2.00);
```

---

## 3. Pedidos y Transacciones

### 📦 `orders`
Pedidos de los clientes.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | SERIAL | ID único | `10001` |
| `user_id` | INT | ID del cliente | `1001` |
| `restaurant_id` | INT | ID del restaurante | `42` |
| `courier_id` | BIGINT | ID del courier | `500` |
| `total_amount` | DECIMAL(10,2) | Total final | `25.95` |
| `subtotal` | DECIMAL(10,2) | Subtotal productos | `20.00` |
| `delivery_fee` | DECIMAL(10,2) | Tarifa entrega | `2.50` |
| `service_fee` | DECIMAL(10,2) | Tarifa servicio | `1.45` |
| `tax_amount` | DECIMAL(10,2) | Impuestos | `2.00` |
| `discount_amount` | DECIMAL(10,2) | Descuentos | `0.00` |
| `delivery_type` | VARCHAR(20) | Tipo | `DELIVERY`, `PICKUP` |
| `status` | VARCHAR(50) | Estado actual | `CREATED`, `PREPARING`, `DELIVERED` |
| `delivery_address` | TEXT | Dirección entrega | `Calle Mayor 10, 3º B` |
| `special_instructions` | TEXT | Instrucciones | `Sin cebolla por favor` |
| `order_date` | TIMESTAMP | Fecha pedido | `2024-12-27 14:30:00` |
| `estimated_delivery_time` | TIMESTAMP | Estimación | `2024-12-27 15:00:00` |
| `scheduled_delivery_time` | TIMESTAMP | Entrega programada | `NULL` |
| `actual_delivery_time` | TIMESTAMP | Entrega real | `2024-12-27 14:58:00` |
| `preparation_started_at` | TIMESTAMP | Inicio preparación | `2024-12-27 14:35:00` |
| `picked_up_at` | TIMESTAMP | Recogido por courier | `2024-12-27 14:50:00` |
| `platform_commission` | DECIMAL(10,2) | Comisión plataforma | `6.00` |
| `restaurant_earnings` | DECIMAL(10,2) | Ganancias restaurante | `14.00` |
| `courier_earnings` | DECIMAL(10,2) | Ganancias courier | `4.50` |
| `cancellation_reason` | TEXT | Motivo cancelación | `NULL` |
| `cancelled_by` | VARCHAR(20) | Quién canceló | `CUSTOMER`, `RESTAURANT`, `SYSTEM` |

**Ejemplo de uso:**
```sql
-- Crear pedido
INSERT INTO orders (
    user_id, restaurant_id, total_amount, subtotal,
    delivery_fee, service_fee, tax_amount, status,
    delivery_address, order_date, estimated_delivery_time
) VALUES (
    1001, 42, 25.95, 20.00,
    2.50, 1.45, 2.00, 'CREATED',
    'Calle Mayor 10, 3º B, 28013 Madrid',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 minutes'
);

-- Ver pedidos activos de un restaurante
SELECT * FROM orders 
WHERE restaurant_id = 42 
  AND status NOT IN ('DELIVERED', 'CANCELLED')
ORDER BY order_date DESC;

-- Estadísticas del día
SELECT 
    COUNT(*) as total_orders,
    SUM(total_amount) as total_revenue,
    AVG(total_amount) as avg_order_value,
    COUNT(CASE WHEN status = 'CANCELLED' THEN 1 END) as cancelled
FROM orders
WHERE restaurant_id = 42 
  AND DATE(order_date) = CURRENT_DATE;
```

---

### 🍔 `order_items`
Items individuales de cada pedido.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | SERIAL | ID único | `50001` |
| `order_id` | INT | ID del pedido | `10001` |
| `dish_id` | INT | ID del plato | `2001` |
| `quantity` | INT | Cantidad | `2` |
| `price` | DECIMAL(10,2) | Precio unitario | `7.95` |

**Ejemplo de uso:**
```sql
-- Agregar items al pedido
INSERT INTO order_items (order_id, dish_id, quantity, price) VALUES
    (10001, 2001, 2, 7.95),   -- 2x Whopper
    (10001, 2005, 1, 4.50),   -- 1x Patatas
    (10001, 2010, 2, 2.00);   -- 2x Bebidas

-- Ver detalle de pedido
SELECT 
    oi.quantity,
    d.name as dish_name,
    oi.price as unit_price,
    (oi.quantity * oi.price) as line_total
FROM order_items oi
JOIN dishes d ON oi.dish_id = d.id
WHERE oi.order_id = 10001;
```

---

### 📊 `order_status_history`
Historial de cambios de estado de pedidos.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `1000` |
| `order_id` | BIGINT | ID del pedido | `10001` |
| `status` | order_status | Nuevo estado | `PREPARING` |
| `changed_by_user_id` | BIGINT | Quién cambió | `42` |
| `notes` | TEXT | Notas | `Pedido confirmado por el restaurante` |
| `created_at` | TIMESTAMP | Fecha cambio | `2024-12-27 14:35:00` |

**Ejemplo de uso:**
```sql
-- Registrar cambio de estado
INSERT INTO order_status_history (order_id, status, changed_by_user_id, notes)
VALUES (10001, 'PREPARING', 42, 'Restaurante empezó la preparación');

-- Ver historial completo de un pedido
SELECT * FROM order_status_history 
WHERE order_id = 10001 
ORDER BY created_at;
```

---

## 4. Couriers y Entregas

### 🚴 `couriers`
Información de repartidores.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `500` |
| `name` | VARCHAR(255) | Nombre | `Carlos Ruiz` |
| `user_id` | BIGINT | ID usuario | `2001` |
| `vehicle_type` | VARCHAR(50) | Tipo vehículo | `BICYCLE`, `MOTORCYCLE`, `CAR` |
| `license_number` | VARCHAR(50) | Número licencia | `12345678A` |
| `license_expiry` | DATE | Vencimiento licencia | `2026-06-15` |
| `vehicle_plate` | VARCHAR(20) | Matrícula | `1234ABC` |
| `current_latitude` | DECIMAL(9,6) | Latitud actual | `40.416775` |
| `current_longitude` | DECIMAL(9,6) | Longitud actual | `-3.703790` |
| `is_online` | BOOLEAN | Si está en línea | `true` |
| `current_orders_count` | INT | Pedidos activos | `2` |
| `is_active` | BOOLEAN | Si está activo | `true` |

**Ejemplo de uso:**
```sql
-- Registrar courier
INSERT INTO couriers (
    name, user_id, vehicle_type, 
    license_number, is_active
) VALUES (
    'Carlos Ruiz', 2001, 'MOTORCYCLE',
    '12345678A', true
);

-- Actualizar ubicación en tiempo real
UPDATE couriers 
SET current_latitude = 40.417000, 
    current_longitude = -3.704000,
    current_orders_count = 1
WHERE id = 500;

-- Buscar couriers disponibles cerca
SELECT * FROM couriers
WHERE is_online = true 
  AND is_active = true
  AND current_orders_count < 3
ORDER BY current_orders_count;
```

---

### 📍 `order_tracking`
Seguimiento en tiempo real de pedidos.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `5000` |
| `order_id` | BIGINT | ID pedido | `10001` |
| `courier_id` | BIGINT | ID courier | `500` |
| `latitude` | DECIMAL(9,6) | Latitud | `40.417500` |
| `longitude` | DECIMAL(9,6) | Longitud | `-3.703500` |
| `status` | VARCHAR(50) | Estado | `EN_ROUTE`, `ARRIVED` |
| `estimated_arrival` | TIMESTAMP | ETA | `2024-12-27 15:05:00` |
| `created_at` | TIMESTAMP | Timestamp | `2024-12-27 14:55:30` |

**Ejemplo de uso:**
```sql
-- Actualizar posición cada 30 segundos
INSERT INTO order_tracking (
    order_id, courier_id, latitude, longitude,
    status, estimated_arrival
) VALUES (
    10001, 500, 40.417500, -3.703500,
    'EN_ROUTE', CURRENT_TIMESTAMP + INTERVAL '10 minutes'
);

-- Obtener últimas posiciones
SELECT * FROM order_tracking
WHERE order_id = 10001
ORDER BY created_at DESC
LIMIT 10;
```

---

## 5. Pagos y Finanzas

### 💳 `payment_methods`
Métodos de pago de usuarios.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `1000` |
| `user_id` | BIGINT | ID usuario | `1001` |
| `type` | VARCHAR(20) | Tipo | `CARD`, `PAYPAL`, `CASH`, `WALLET` |
| `provider` | VARCHAR(50) | Proveedor | `STRIPE`, `PAYPAL` |
| `token` | VARCHAR(255) | Token seguro | `tok_1234567890abcdef` |
| `last_four` | VARCHAR(4) | Últimos 4 dígitos | `4242` |
| `is_default` | BOOLEAN | Método por defecto | `true` |
| `is_active` | BOOLEAN | Activo | `true` |

**Ejemplo de uso:**
```sql
-- Agregar tarjeta
INSERT INTO payment_methods (
    user_id, type, provider, token, last_four, is_default
) VALUES (
    1001, 'CARD', 'STRIPE', 'tok_xxxxxxxx', '4242', true
);
```

---

### 💰 `transactions`
Transacciones de pago.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `20001` |
| `order_id` | BIGINT | ID pedido | `10001` |
| `payment_method_id` | BIGINT | Método usado | `1000` |
| `amount` | DECIMAL(10,2) | Monto | `25.95` |
| `status` | VARCHAR(20) | Estado | `PENDING`, `COMPLETED`, `FAILED`, `REFUNDED` |
| `provider_transaction_id` | VARCHAR(255) | ID del proveedor | `pi_1234567890` |
| `created_at` | TIMESTAMP | Fecha | `2024-12-27 14:30:00` |

**Ejemplo de uso:**
```sql
-- Registrar transacción
INSERT INTO transactions (
    order_id, payment_method_id, amount, 
    status, provider_transaction_id
) VALUES (
    10001, 1000, 25.95,
    'COMPLETED', 'pi_1234567890'
);
```

---

### 💸 `payouts`
Pagos a restaurantes y couriers.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `300` |
| `recipient_type` | VARCHAR(20) | Tipo | `RESTAURANT`, `COURIER` |
| `recipient_id` | BIGINT | ID beneficiario | `42` |
| `amount` | DECIMAL(10,2) | Monto | `450.00` |
| `period_start` | DATE | Inicio periodo | `2024-12-01` |
| `period_end` | DATE | Fin periodo | `2024-12-07` |
| `status` | VARCHAR(20) | Estado | `PENDING`, `COMPLETED` |
| `processed_at` | TIMESTAMP | Procesado | `2024-12-08 10:00:00` |

**Ejemplo de uso:**
```sql
-- Crear payout semanal para restaurante
INSERT INTO payouts (
    recipient_type, recipient_id, amount,
    period_start, period_end, status
) VALUES (
    'RESTAURANT', 42, 1250.00,
    '2024-12-01', '2024-12-07', 'PENDING'
);

-- Ver payouts pendientes
SELECT 
    p.*,
    r.name as restaurant_name
FROM payouts p
JOIN restaurants r ON p.recipient_id = r.id
WHERE p.recipient_type = 'RESTAURANT' 
  AND p.status = 'PENDING'
ORDER BY p.period_end DESC;
```

---

### 💵 `tips`
Propinas para couriers.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `400` |
| `order_id` | BIGINT | ID pedido | `10001` |
| `courier_id` | BIGINT | ID courier | `500` |
| `amount` | DECIMAL(10,2) | Monto propina | `3.00` |
| `tip_type` | VARCHAR(20) | Tipo | `PRE_ORDER`, `POST_ORDER` |
| `created_at` | TIMESTAMP | Fecha | `2024-12-27 15:00:00` |

**Ejemplo de uso:**
```sql
-- Agregar propina después de la entrega
INSERT INTO tips (order_id, courier_id, amount, tip_type)
VALUES (10001, 500, 3.00, 'POST_ORDER');

-- Ver propinas del courier en el día
SELECT 
    SUM(amount) as total_tips,
    COUNT(*) as tip_count,
    AVG(amount) as avg_tip
FROM tips
WHERE courier_id = 500 
  AND DATE(created_at) = CURRENT_DATE;
```

---

### 💼 `user_wallets`
Monederos virtuales de usuarios.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `100` |
| `user_id` | BIGINT | ID usuario | `1001` |
| `balance` | DECIMAL(10,2) | Saldo actual | `15.50` |
| `currency` | VARCHAR(3) | Moneda | `EUR` |

**Ejemplo de uso:**
```sql
-- Crear wallet para usuario
INSERT INTO user_wallets (user_id, balance)
VALUES (1001, 0.00);

-- Consultar saldo
SELECT balance FROM user_wallets WHERE user_id = 1001;
```

---

### 📝 `wallet_transactions`
Movimientos del wallet.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `5000` |
| `wallet_id` | BIGINT | ID wallet | `100` |
| `type` | VARCHAR(20) | Tipo | `CREDIT`, `DEBIT`, `REFUND`, `REFERRAL_BONUS` |
| `amount` | DECIMAL(10,2) | Monto | `10.00` |
| `order_id` | BIGINT | Pedido relacionado | `10001` |
| `description` | TEXT | Descripción | `Bono por referido` |
| `created_at` | TIMESTAMP | Fecha | `2024-12-27 10:00:00` |

**Ejemplo de uso:**
```sql
-- Agregar crédito por referido
INSERT INTO wallet_transactions (
    wallet_id, type, amount, description
) VALUES (
    100, 'CREDIT', 10.00, 'Bono de bienvenida por referido'
);

-- Actualizar balance del wallet
UPDATE user_wallets 
SET balance = balance + 10.00 
WHERE id = 100;

-- Ver historial de wallet
SELECT * FROM wallet_transactions 
WHERE wallet_id = 100 
ORDER BY created_at DESC;
```

---

## 6. Marketing y Promociones

### 🎟️ `promotions`
Cupones y promociones.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `200` |
| `restaurant_id` | BIGINT | Restaurante (NULL = global) | `42` |
| `code` | VARCHAR(50) | Código | `PRIMERAVEZ`, `NAVIDAD2024` |
| `discount_type` | VARCHAR(20) | Tipo descuento | `PERCENT`, `FIXED` |
| `discount_value` | DECIMAL(10,2) | Valor | `20.00` (20% o 20€) |
| `min_order_amount` | DECIMAL(10,2) | Pedido mínimo | `15.00` |
| `start_date` | TIMESTAMP | Inicio | `2024-12-01` |
| `end_date` | TIMESTAMP | Fin | `2024-12-31` |
| `max_uses` | INT | Usos máximos | `1000` |
| `times_used` | INT | Veces usado | `543` |
| `user_specific` | BOOLEAN | Usuario específico | `false` |
| `first_order_only` | BOOLEAN | Solo primer pedido | `true` |
| `is_active` | BOOLEAN | Activo | `true` |

**Ejemplo de uso:**
```sql
-- Crear cupón primer pedido
INSERT INTO promotions (
    code, discount_type, discount_value,
    min_order_amount, start_date, end_date,
    max_uses, first_order_only, is_active
) VALUES (
    'BIENVENIDA10', 'FIXED', 10.00,
    15.00, '2024-01-01', '2024-12-31',
    10000, true, true
);

-- Validar cupón antes de aplicar
SELECT * FROM promotions
WHERE code = 'BIENVENIDA10'
  AND is_active = true
  AND CURRENT_TIMESTAMP BETWEEN start_date AND end_date
  AND (max_uses IS NULL OR times_used < max_uses);
```

---

### 📋 `user_promotion_usage`
Registro de uso de cupones por usuario.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `user_id` | BIGINT | ID usuario |
| `promotion_id` | BIGINT | ID promoción |
| `order_id` | BIGINT | ID pedido |
| `used_at` | TIMESTAMP | Fecha uso |

**Ejemplo de uso:**
```sql
-- Registrar uso de cupón
INSERT INTO user_promotion_usage (user_id, promotion_id, order_id)
VALUES (1001, 200, 10001);

-- Incrementar contador
UPDATE promotions 
SET times_used = times_used + 1 
WHERE id = 200;

-- Verificar si usuario ya usó el cupón
SELECT COUNT(*) FROM user_promotion_usage
WHERE user_id = 1001 AND promotion_id = 200;
```

---

### 🎁 `referral_codes`
Códigos de referidos.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `150` |
| `user_id` | BIGINT | Usuario propietario | `1001` |
| `code` | VARCHAR(20) | Código único | `JUAN2024` |
| `discount_amount` | DECIMAL(10,2) | Descuento | `5.00` |
| `times_used` | INT | Veces usado | `12` |
| `max_uses` | INT | Máximo usos | `NULL` (ilimitado) |

**Ejemplo de uso:**
```sql
-- Crear código de referido
INSERT INTO referral_codes (user_id, code, discount_amount)
VALUES (1001, 'JUAN2024', 5.00);

-- Aplicar referido y dar bonus
BEGIN;
  -- Incrementar contador
  UPDATE referral_codes SET times_used = times_used + 1 
  WHERE code = 'JUAN2024';
  
  -- Dar bonus al referidor
  INSERT INTO wallet_transactions (wallet_id, type, amount, description)
  VALUES (100, 'CREDIT', 5.00, 'Bonus por referir amigo');
  
  UPDATE user_wallets SET balance = balance + 5.00 WHERE user_id = 1001;
COMMIT;
```

---

### 📈 `sponsored_restaurants`
Restaurantes patrocinados (publicidad).

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `restaurant_id` | BIGINT | ID restaurante | `42` |
| `priority` | INT | Prioridad (menor = mejor) | `1`, `2`, `3` |
| `start_date` | TIMESTAMP | Inicio campaña | `2024-12-01` |
| `end_date` | TIMESTAMP | Fin campaña | `2024-12-07` |
| `budget` | DECIMAL(10,2) | Presupuesto | `300.00` |

**Ejemplo de uso:**
```sql
-- Crear campaña de publicidad
INSERT INTO sponsored_restaurants (
    restaurant_id, priority, start_date, end_date, budget
) VALUES (
    42, 1, '2024-12-27', '2025-01-03', 500.00
);

-- Query para listado con patrocinados primero
SELECT r.*,
       CASE WHEN sr.restaurant_id IS NOT NULL THEN true ELSE false END as is_sponsored
FROM restaurants r
LEFT JOIN sponsored_restaurants sr ON r.id = sr.restaurant_id
  AND CURRENT_TIMESTAMP BETWEEN sr.start_date AND sr.end_date
WHERE r.is_active = true
ORDER BY 
  sr.priority NULLS LAST,
  r.rating DESC;
```

---

### 💸 `surge_pricing`
Precios dinámicos por demanda.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `50` |
| `restaurant_id` | BIGINT | ID restaurante | `42` |
| `zone_id` | BIGINT | ID zona | `10` |
| `multiplier` | DECIMAL(3,2) | Multiplicador | `1.50` (50% más) |
| `start_time` | TIMESTAMP | Inicio | `2024-12-27 20:00:00` |
| `end_time` | TIMESTAMP | Fin | `2024-12-27 22:00:00` |
| `reason` | VARCHAR(50) | Razón | `HIGH_DEMAND`, `WEATHER`, `HOLIDAY` |

**Ejemplo de uso:**
```sql
-- Activar surge pricing en hora punta
INSERT INTO surge_pricing (
    restaurant_id, multiplier, start_time, end_time, reason
) VALUES (
    42, 1.30, '2024-12-31 22:00:00', '2025-01-01 02:00:00', 'HOLIDAY'
);

-- Calcular precio con surge
SELECT 
    d.price,
    sp.multiplier,
    (d.price * sp.multiplier) as price_with_surge
FROM dishes d
LEFT JOIN surge_pricing sp ON d.restaurant_id = sp.restaurant_id
  AND CURRENT_TIMESTAMP BETWEEN sp.start_time AND sp.end_time
WHERE d.id = 2001;
```

---

## 7. Reviews y Ratings

### ⭐ `restaurant_reviews`
Reseñas de restaurantes.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `1000` |
| `restaurant_id` | BIGINT | ID restaurante | `42` |
| `user_id` | BIGINT | ID usuario | `1001` |
| `rating` | INT | Calificación (1-5) | `5` |
| `comment` | TEXT | Comentario | `Excelente comida y servicio rápido` |
| `created_at` | TIMESTAMP | Fecha | `2024-12-27 15:30:00` |

**Ejemplo de uso:**
```sql
-- Crear reseña
INSERT INTO restaurant_reviews (restaurant_id, user_id, rating, comment)
VALUES (42, 1001, 5, 'Excelente comida y servicio rápido');

-- Actualizar rating promedio del restaurante
UPDATE restaurants
SET rating = (
    SELECT AVG(rating)::DECIMAL(3,2) 
    FROM restaurant_reviews 
    WHERE restaurant_id = 42
),
review_count = (
    SELECT COUNT(*) 
    FROM restaurant_reviews 
    WHERE restaurant_id = 42
)
WHERE id = 42;

-- Ver reseñas recientes
SELECT 
    rr.*,
    u.full_name as user_name
FROM restaurant_reviews rr
JOIN users u ON rr.user_id = u.id
WHERE rr.restaurant_id = 42
ORDER BY rr.created_at DESC
LIMIT 10;
```

---

### 🚴 `courier_reviews`
Reseñas de couriers.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `2000` |
| `courier_id` | BIGINT | ID courier | `500` |
| `order_id` | BIGINT | ID pedido | `10001` |
| `user_id` | BIGINT | ID usuario | `1001` |
| `rating` | INT | Calificación (1-5) | `5` |
| `comment` | TEXT | Comentario | `Muy amable y puntual` |
| `created_at` | TIMESTAMP | Fecha | `2024-12-27 15:30:00` |

**Ejemplo de uso:**
```sql
-- Crear reseña de courier
INSERT INTO courier_reviews (courier_id, order_id, user_id, rating, comment)
VALUES (500, 10001, 1001, 5, 'Muy amable y puntual');

-- Ver estadísticas del courier
SELECT 
    AVG(rating)::DECIMAL(3,2) as avg_rating,
    COUNT(*) as total_reviews
FROM courier_reviews
WHERE courier_id = 500;
```

---

### 🍕 `dish_reviews`
Reseñas de platos específicos.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `3000` |
| `dish_id` | BIGINT | ID plato | `2001` |
| `order_id` | BIGINT | ID pedido | `10001` |
| `user_id` | BIGINT | ID usuario | `1001` |
| `rating` | INT | Calificación (1-5) | `5` |
| `comment` | TEXT | Comentario | `Deliciosa, perfecta temperatura` |
| `tags` | VARCHAR(50)[] | Etiquetas | `{'delicious', 'hot', 'large_portion'}` |
| `created_at` | TIMESTAMP | Fecha | `2024-12-27 15:30:00` |

**Ejemplo de uso:**
```sql
-- Crear reseña de plato con tags
INSERT INTO dish_reviews (
    dish_id, order_id, user_id, rating, comment, tags
) VALUES (
    2001, 10001, 1001, 5, 
    'Deliciosa, perfecta temperatura',
    ARRAY['delicious', 'hot', 'large_portion']
);

-- Buscar platos con tags específicos
SELECT d.*, COUNT(*) as positive_reviews
FROM dishes d
JOIN dish_reviews dr ON d.id = dr.dish_id
WHERE 'delicious' = ANY(dr.tags)
  AND dr.rating >= 4
GROUP BY d.id
ORDER BY positive_reviews DESC;
```

---

### ❤️ `user_favorites`
Restaurantes favoritos del usuario.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | BIGSERIAL | ID único |
| `user_id` | BIGINT | ID usuario |
| `restaurant_id` | BIGINT | ID restaurante |
| `created_at` | TIMESTAMP | Fecha agregado |

**Ejemplo de uso:**
```sql
-- Agregar a favoritos
INSERT INTO user_favorites (user_id, restaurant_id)
VALUES (1001, 42)
ON CONFLICT (user_id, restaurant_id) DO NOTHING;

-- Ver favoritos del usuario
SELECT r.* 
FROM restaurants r
JOIN user_favorites uf ON r.id = uf.restaurant_id
WHERE uf.user_id = 1001
ORDER BY uf.created_at DESC;

-- Quitar de favoritos
DELETE FROM user_favorites 
WHERE user_id = 1001 AND restaurant_id = 42;
```

---

### 📌 `user_saved_dishes`
Platos guardados del usuario.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | BIGSERIAL | ID único |
| `user_id` | BIGINT | ID usuario |
| `dish_id` | BIGINT | ID plato |
| `created_at` | TIMESTAMP | Fecha guardado |

**Ejemplo de uso:**
```sql
-- Guardar plato
INSERT INTO user_saved_dishes (user_id, dish_id)
VALUES (1001, 2001)
ON CONFLICT (user_id, dish_id) DO NOTHING;

-- Ver platos guardados
SELECT d.*, r.name as restaurant_name
FROM dishes d
JOIN user_saved_dishes usd ON d.id = usd.dish_id
JOIN restaurants r ON d.restaurant_id = r.id
WHERE usd.user_id = 1001;
```

---

## 8. Notificaciones

### 🔔 `notifications`
Notificaciones enviadas a usuarios.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `10000` |
| `user_id` | BIGINT | ID usuario | `1001` |
| `type` | VARCHAR(50) | Tipo | `ORDER_CONFIRMED`, `COURIER_ASSIGNED`, `DELIVERED` |
| `title` | VARCHAR(255) | Título | `¡Pedido confirmado!` |
| `message` | TEXT | Mensaje | `Tu pedido #10001 ha sido confirmado` |
| `data` | JSONB | Datos extra | `{"order_id": 10001, "eta": "30 min"}` |
| `is_read` | BOOLEAN | Leída | `false` |
| `created_at` | TIMESTAMP | Fecha | `2024-12-27 14:35:00` |

**Ejemplo de uso:**
```sql
-- Crear notificación
INSERT INTO notifications (
    user_id, type, title, message, data
) VALUES (
    1001, 'ORDER_CONFIRMED',
    '¡Pedido confirmado!',
    'Tu pedido #10001 ha sido confirmado por el restaurante',
    '{"order_id": 10001, "restaurant_name": "Burger King"}'::jsonb
);

-- Ver notificaciones no leídas
SELECT * FROM notifications
WHERE user_id = 1001 AND is_read = false
ORDER BY created_at DESC;

-- Marcar como leída
UPDATE notifications 
SET is_read = true 
WHERE id = 10000;
```

---

### 📱 `notification_tokens`
Tokens para push notifications.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `500` |
| `user_id` | BIGINT | ID usuario | `1001` |
| `token` | VARCHAR(255) | Token FCM/APNs | `dQw4w9WgXcQ...` |
| `platform` | VARCHAR(20) | Plataforma | `IOS`, `ANDROID`, `WEB` |
| `is_active` | BOOLEAN | Activo | `true` |

**Ejemplo de uso:**
```sql
-- Registrar token de dispositivo
INSERT INTO notification_tokens (user_id, token, platform)
VALUES (1001, 'fcm_token_here', 'ANDROID')
ON CONFLICT (token) DO UPDATE 
SET is_active = true;

-- Obtener tokens activos para enviar push
SELECT token, platform 
FROM notification_tokens
WHERE user_id = 1001 AND is_active = true;
```

---

## 9. Analytics y Auditoría

### 📊 `events`
Registro de eventos de negocio.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `50000` |
| `entity_type` | VARCHAR(50) | Tipo entidad | `ORDER`, `USER`, `RESTAURANT` |
| `entity_id` | BIGINT | ID entidad | `10001` |
| `event_type` | VARCHAR(50) | Tipo evento | `CREATED`, `UPDATED`, `CANCELLED` |
| `metadata` | JSONB | Datos extra | `{"reason": "out_of_stock"}` |
| `created_at` | TIMESTAMP | Fecha | `2024-12-27 14:30:00` |

**Ejemplo de uso:**
```sql
-- Registrar evento
INSERT INTO events (entity_type, entity_id, event_type, metadata)
VALUES (
    'ORDER', 10001, 'CANCELLED',
    '{"reason": "restaurant_closed", "cancelled_by": "SYSTEM"}'::jsonb
);

-- Analizar cancelaciones
SELECT 
    metadata->>'reason' as cancellation_reason,
    COUNT(*) as count
FROM events
WHERE event_type = 'CANCELLED'
  AND entity_type = 'ORDER'
  AND created_at >= CURRENT_DATE - INTERVAL '7 days'
GROUP BY metadata->>'reason'
ORDER BY count DESC;
```

---

### 🔍 `audit_logs`
Auditoría de cambios en datos críticos.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `100000` |
| `table_name` | VARCHAR(50) | Tabla | `orders` |
| `record_id` | BIGINT | ID registro | `10001` |
| `action` | VARCHAR(20) | Acción | `INSERT`, `UPDATE`, `DELETE` |
| `old_data` | JSONB | Datos anteriores | `{"status": "PREPARING"}` |
| `new_data` | JSONB | Datos nuevos | `{"status": "READY"}` |
| `created_at` | TIMESTAMP | Fecha | `2024-12-27 14:45:00` |

**Ejemplo de uso:**
```sql
-- Ver historial de cambios de un pedido
SELECT * FROM audit_logs
WHERE table_name = 'orders' AND record_id = 10001
ORDER BY created_at DESC;

-- Trigger para auditar cambios (ejemplo)
CREATE OR REPLACE FUNCTION audit_order_changes()
RETURNS TRIGGER AS $
BEGIN
    INSERT INTO audit_logs (table_name, record_id, action, old_data, new_data)
    VALUES (
        'orders',
        NEW.id,
        TG_OP,
        to_jsonb(OLD),
        to_jsonb(NEW)
    );
    RETURN NEW;
END;
$ LANGUAGE plpgsql;
```

---

### 📈 `restaurant_daily_stats` (Vista Materializada)
Estadísticas diarias de restaurantes para dashboards.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `restaurant_id` | BIGINT | ID restaurante |
| `date` | DATE | Fecha |
| `total_orders` | BIGINT | Total pedidos |
| `total_revenue` | NUMERIC | Ingresos totales |
| `avg_order_value` | NUMERIC | Ticket promedio |
| `cancelled_orders` | BIGINT | Pedidos cancelados |
| `avg_delivery_minutes` | DOUBLE PRECISION | Tiempo entrega promedio |

**Ejemplo de uso:**
```sql
-- Refrescar estadísticas
REFRESH MATERIALIZED VIEW restaurant_daily_stats;

-- Ver estadísticas del mes
SELECT 
    date,
    total_orders,
    total_revenue,
    avg_order_value,
    cancelled_orders
FROM restaurant_daily_stats
WHERE restaurant_id = 42
  AND date >= DATE_TRUNC('month', CURRENT_DATE)
ORDER BY date DESC;

-- Top restaurantes del mes
SELECT 
    r.name,
    SUM(rds.total_revenue) as monthly_revenue,
    SUM(rds.total_orders) as monthly_orders
FROM restaurant_daily_stats rds
JOIN restaurants r ON rds.restaurant_id = r.id
WHERE rds.date >= DATE_TRUNC('month', CURRENT_DATE)
GROUP BY r.id, r.name
ORDER BY monthly_revenue DESC
LIMIT 10;
```

---

## 10. Configuración y Estado

### ⚙️ `restaurant_status`
Estado en tiempo real del restaurante.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `restaurant_id` | BIGINT | ID restaurante | `42` |
| `status` | VARCHAR(30) | Estado | `OPEN`, `PAUSED`, `BUSY`, `CLOSED` |
| `reason` | TEXT | Razón | `Muchos pedidos activos` |
| `updated_at` | TIMESTAMP | Última actualización | `2024-12-27 14:30:00` |

**Ejemplo de uso:**
```sql
-- Pausar restaurante temporalmente
INSERT INTO restaurant_status (restaurant_id, status, reason)
VALUES (42, 'PAUSED', 'Falta de ingredientes')
ON CONFLICT (restaurant_id) 
DO UPDATE SET 
    status = EXCLUDED.status,
    reason = EXCLUDED.reason,
    updated_at = CURRENT_TIMESTAMP;

-- Ver estado actual
SELECT r.name, rs.status, rs.reason
FROM restaurants r
LEFT JOIN restaurant_status rs ON r.id = rs.restaurant_id
WHERE r.id = 42;
```

---

### 🕐 `restaurant_opening_hours`
Horarios de apertura.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `100` |
| `restaurant_id` | BIGINT | ID restaurante | `42` |
| `day_of_week` | INT | Día (0=Dom, 6=Sáb) | `1` (Lunes) |
| `open_time` | TIME | Hora apertura | `11:00:00` |
| `close_time` | TIME | Hora cierre | `23:00:00` |
| `is_closed` | BOOLEAN | Cerrado ese día | `false` |

**Ejemplo de uso:**
```sql
-- Configurar horarios de lunes a viernes
INSERT INTO restaurant_opening_hours (
    restaurant_id, day_of_week, open_time, close_time
) VALUES
    (42, 1, '11:00', '23:00'),  -- Lunes
    (42, 2, '11:00', '23:00'),  -- Martes
    (42, 3, '11:00', '23:00'),  -- Miércoles
    (42, 4, '11:00', '23:00'),  -- Jueves
    (42, 5, '11:00', '00:00'),  -- Viernes
    (42, 6, '12:00', '01:00'),  -- Sábado
    (42, 0, '12:00', '23:00');  -- Domingo

-- Verificar si está abierto ahora
SELECT 
    CASE 
        WHEN roh.open_time <= CURRENT_TIME 
         AND roh.close_time >= CURRENT_TIME 
         AND roh.is_closed = false
        THEN true 
        ELSE false 
    END as is_open_now
FROM restaurant_opening_hours roh
WHERE roh.restaurant_id = 42
  AND roh.day_of_week = EXTRACT(DOW FROM CURRENT_DATE);
```

---

### 🚫 `restaurant_closures`
Cierres temporales excepcionales.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `50` |
| `restaurant_id` | BIGINT | ID restaurante | `42` |
| `start_datetime` | TIMESTAMP | Inicio cierre | `2024-12-25 00:00:00` |
| `end_datetime` | TIMESTAMP | Fin cierre | `2024-12-25 23:59:59` |
| `reason` | TEXT | Motivo | `Navidad - Festivo` |

**Ejemplo de uso:**
```sql
-- Programar cierre por festivo
INSERT INTO restaurant_closures (
    restaurant_id, start_datetime, end_datetime, reason
) VALUES (
    42, 
    '2024-12-25 00:00:00', 
    '2024-12-25 23:59:59',
    'Navidad - Festivo nacional'
);

-- Verificar si hay cierre programado
SELECT * FROM restaurant_closures
WHERE restaurant_id = 42
  AND CURRENT_TIMESTAMP BETWEEN start_datetime AND end_datetime;
```

---

### 📍 `restaurant_delivery_zones`
Zonas de entrega con configuración específica.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `20` |
| `restaurant_id` | BIGINT | ID restaurante | `42` |
| `radius_km` | DECIMAL(5,2) | Radio en km | `5.00` |
| `minimum_order` | DECIMAL(10,2) | Pedido mínimo | `12.00` |
| `delivery_fee` | DECIMAL(10,2) | Tarifa entrega | `3.00` |

**Ejemplo de uso:**
```sql
-- Configurar zonas de entrega
INSERT INTO restaurant_delivery_zones (
    restaurant_id, radius_km, minimum_order, delivery_fee
) VALUES
    (42, 3.0, 10.00, 2.00),   -- Zona 1: cerca
    (42, 5.0, 15.00, 3.50),   -- Zona 2: media
    (42, 8.0, 20.00, 5.00);   -- Zona 3: lejos

-- Calcular tarifa según distancia (requiere PostGIS)
-- Ejemplo simplificado
SELECT 
    rdz.delivery_fee,
    rdz.minimum_order
FROM restaurant_delivery_zones rdz
WHERE rdz.restaurant_id = 42
  AND rdz.radius_km >= 4.5  -- distancia calculada
ORDER BY rdz.radius_km ASC
LIMIT 1;
```

---

### 💼 `restaurant_commissions`
Comisiones que cobra la plataforma.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `restaurant_id` | BIGINT | ID restaurante | `42` |
| `commission_percent` | DECIMAL(5,2) | Porcentaje | `30.00` (30%) |
| `fixed_fee` | DECIMAL(10,2) | Tarifa fija | `0.50` |
| `start_date` | TIMESTAMP | Inicio vigencia | `2024-01-01` |
| `end_date` | TIMESTAMP | Fin vigencia | `NULL` |

**Ejemplo de uso:**
```sql
-- Configurar comisión
INSERT INTO restaurant_commissions (
    restaurant_id, commission_percent, fixed_fee, start_date
) VALUES (
    42, 30.00, 0.50, '2024-01-01'
);

-- Calcular comisión de un pedido
SELECT 
    o.subtotal,
    rc.commission_percent,
    rc.fixed_fee,
    (o.subtotal * rc.commission_percent / 100) + rc.fixed_fee as platform_commission,
    o.subtotal - ((o.subtotal * rc.commission_percent / 100) + rc.fixed_fee) as restaurant_earnings
FROM orders o
JOIN restaurant_commissions rc ON o.restaurant_id = rc.restaurant_id
WHERE o.id = 10001
  AND (rc.end_date IS NULL OR CURRENT_DATE <= rc.end_date);
```

---

### ⚠️ `order_issues`
Incidencias y problemas en pedidos.

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| `id` | BIGSERIAL | ID único | `500` |
| `order_id` | BIGINT | ID pedido | `10001` |
| `type` | VARCHAR(50) | Tipo | `LATE`, `WRONG_ITEM`, `COLD`, `MISSING_ITEM` |
| `description` | TEXT | Descripción | `Faltaba una hamburguesa` |
| `created_at` | TIMESTAMP | Fecha | `2024-12-27 15:10:00` |

**Ejemplo de uso:**
```sql
-- Reportar incidencia
INSERT INTO order_issues (order_id, type, description)
VALUES (10001, 'MISSING_ITEM', 'Faltaba las patatas fritas del combo');

-- Ver incidencias frecuentes de un restaurante
SELECT 
    oi.type,
    COUNT(*) as incident_count
FROM order_issues oi
JOIN orders o ON oi.order_id = o.id
WHERE o.restaurant_id = 42
  AND oi.created_at >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY oi.type
ORDER BY incident_count DESC;
```

---

## 11. Tipos de Datos Personalizados

### 📌 `order_status` (ENUM)
Estados posibles de un pedido.

**Valores:**
- `CREATED` - Pedido creado, esperando confirmación
- `CONFIRMED` - Confirmado por el restaurante
- `PREPARING` - En preparación
- `READY` - Listo para recoger
- `PICKED_UP` - Recogido por courier
- `DELIVERED` - Entregado al cliente
- `CANCELLED` - Cancelado

**Flujo normal:**
```
CREATED → CONFIRMED → PREPARING → READY → PICKED_UP → DELIVERED
                ↓
           CANCELLED (posible en cualquier momento)
```

**Ejemplo de uso:**
```sql
-- Cambiar estado con validación
UPDATE orders 
SET status = 'PREPARING'::order_status
WHERE id = 10001 
  AND status = 'CONFIRMED'::order_status;

-- Query por estado
SELECT * FROM orders 
WHERE status IN ('PREPARING'::order_status, 'READY'::order_status);
```

---

## 12. Índices Importantes

```sql
-- Orders
CREATE INDEX idx_orders_restaurant_id ON orders(restaurant_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_restaurant_status ON orders(restaurant_id, status);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_date ON orders(order_date);

-- Dishes
CREATE INDEX idx_dishes_restaurant_id ON dishes(restaurant_id);
CREATE INDEX idx_dishes_available ON dishes(is_available) WHERE is_available = true;

-- Tracking
CREATE INDEX idx_order_tracking_order ON order_tracking(order_id);
CREATE INDEX idx_order_tracking_courier ON order_tracking(courier_id);

-- Status History
CREATE INDEX idx_order_status_history_order ON order_status_history(order_id);

-- Full-text Search
CREATE INDEX idx_restaurants_search ON restaurants USING gin(search_vector);
CREATE INDEX idx_dishes_search ON dishes USING gin(search_vector);

-- Geoespacial (requiere PostGIS)
CREATE INDEX idx_addresses_location ON addresses USING gist(
    ll_to_earth(latitude, longitude)
);
```

---

## 13. Queries Útiles

### 🔍 Buscar restaurantes cerca
```sql
-- Requiere extensión earthdistance
SELECT 
    r.*,
    earth_distance(
        ll_to_earth(40.416775, -3.703790),  -- ubicación usuario
        ll_to_earth(a.latitude, a.longitude)
    ) / 1000 as distance_km
FROM restaurants r
JOIN addresses a ON r.id = a.restaurant_id
WHERE earth_box(ll_to_earth(40.416775, -3.703790), 5000) @> ll_to_earth(a.latitude, a.longitude)
  AND r.is_active = true
  AND r.is_open = true
ORDER BY distance_km, r.rating DESC;
```

### 📊 Dashboard del restaurante
```sql
SELECT 
    -- Hoy
    COUNT(CASE WHEN DATE(order_date) = CURRENT_DATE THEN 1 END) as orders_today,
    SUM(CASE WHEN DATE(order_date) = CURRENT_DATE THEN total_amount ELSE 0 END) as revenue_today,
    
    -- Esta semana
    COUNT(CASE WHEN order_date >= DATE_TRUNC('week', CURRENT_DATE) THEN 1 END) as orders_week,
    SUM(CASE WHEN order_date >= DATE_TRUNC('week', CURRENT_DATE) THEN total_amount ELSE 0 END) as revenue_week,
    
    -- Este mes
    COUNT(CASE WHEN order_date >= DATE_TRUNC('month', CURRENT_DATE) THEN 1 END) as orders_month,
    SUM(CASE WHEN order_date >= DATE_TRUNC('month', CURRENT_DATE) THEN total_amount ELSE 0 END) as revenue_month,
    
    -- Promedio
    AVG(total_amount) as avg_order_value,
    
    -- Tasa de cancelación
    (COUNT(CASE WHEN status = 'CANCELLED' THEN 1 END)::FLOAT / NULLIF(COUNT(*), 0) * 100)::DECIMAL(5,2) as cancellation_rate
    
FROM orders
WHERE restaurant_id = 42;
```

### 🏆 Top platos más vendidos
```sql
SELECT 
    d.name,
    d.price,
    COUNT(oi.id) as times_ordered,
    SUM(oi.quantity) as total_quantity,
    SUM(oi.quantity * oi.price) as total_revenue
FROM order_items oi
JOIN dishes d ON oi.dish_id = d.id
JOIN orders o ON oi.order_id = o.id
WHERE o.restaurant_id = 42
  AND o.status = 'DELIVERED'
  AND o.order_date >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY d.id, d.name, d.price
ORDER BY times_ordered DESC
LIMIT 10;
```

### 👥 Clientes frecuentes
```sql
SELECT 
    u.id,
    u.full_name,
    u.email,
    COUNT(o.id) as total_orders,
    SUM(o.total_amount) as total_spent,
    AVG(o.total_amount) as avg_order_value,
    MAX(o.order_date) as last_order_date
FROM users u
JOIN orders o ON u.id = o.user_id
WHERE o.restaurant_id = 42
  AND o.status = 'DELIVERED'
GROUP BY u.id, u.full_name, u.email
HAVING COUNT(o.id) >= 3
ORDER BY total_spent DESC;
```

### 🚴 Rendimiento de couriers
```sql
SELECT 
    c.id,
    c.name,
    COUNT(o.id) as total_deliveries,
    AVG(EXTRACT(EPOCH FROM (o.actual_delivery_time - o.picked_up_at))/60) as avg_delivery_minutes,
    AVG(cr.rating) as avg_rating,
    SUM(o.courier_earnings) as total_earnings,
    SUM(t.amount) as total_tips
FROM couriers c
LEFT JOIN orders o ON c.id = o.courier_id AND o.status = 'DELIVERED'
LEFT JOIN courier_reviews cr ON c.id = cr.courier_id
LEFT JOIN tips t ON c.id = t.courier_id
WHERE o.order_date >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY c.id, c.name
ORDER BY total_deliveries DESC;
```

---

## 14. Mantenimiento

### 🔄 Refresco de vistas materializadas
```sql
-- Ejecutar diariamente (por ejemplo, a las 2 AM)
REFRESH MATERIALIZED VIEW CONCURRENTLY restaurant_daily_stats;
```

### 🗑️ Limpieza de datos antiguos
```sql
-- Archivar notificaciones leídas antiguas
DELETE FROM notifications 
WHERE is_read = true 
  AND created_at < CURRENT_DATE - INTERVAL '90 days';

-- Archivar tracking antiguo
DELETE FROM order_tracking 
WHERE created_at < CURRENT_DATE - INTERVAL '30 days';

-- Limpiar tokens inactivos
DELETE FROM notification_tokens 
WHERE is_active = false 
  AND updated_at < CURRENT_DATE - INTERVAL '180 days';
```

### 📊 Análisis de rendimiento
```sql
-- Tablas más grandes
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC
LIMIT 10;

-- Índices no utilizados
SELECT 
    schemaname,
    tablename,
    indexname,
    idx_scan,
    pg_size_pretty(pg_relation_size(indexrelid)) as size
FROM pg_stat_user_indexes
WHERE idx_scan = 0
  AND indexrelname NOT LIKE 'pg_toast%'
ORDER BY pg_relation_size(indexrelid) DESC;
```

---

## 📚 Recursos Adicionales

### Extensiones PostgreSQL Recomendadas
```sql
-- Geoespacial
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS earthdistance CASCADE;

-- Full-text search avanzado
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

### Backup y Restore
```bash
# Backup completo
pg_dump -U postgres -h localhost delivery_db > backup_$(date +%Y%m%d).sql

# Restore
psql -U postgres -h localhost delivery_db < backup_20241227.sql

# Backup solo esquema
pg_dump -U postgres -h localhost --schema-only delivery_db > schema.sql

# Backup solo datos
pg_dump -U postgres -h localhost --data-only delivery_db > data.sql
```

---

## 📝 Notas Finales

### Convenciones
- **IDs**: Siempre BIGSERIAL para escalabilidad
- **Timestamps**: Usar TIMESTAMP (no TIMESTAMPTZ para simplificar)
- **Money**: DECIMAL(10,2) para cantidades
- **Soft Delete**: Usar `deleted_at` en lugar de hard delete
- **Audit**: Todas las tablas críticas tienen `created_at` y `updated_at`

### Mejores Prácticas
1. Siempre usar transacciones para operaciones múltiples
2. Indexar foreign keys
3. Validar datos en aplicación antes de insertar
4. Usar prepared statements para prevenir SQL injection
5. Monitorear queries lentas regularmente

---

**Fin de la documentación** 🎉