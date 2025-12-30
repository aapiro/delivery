CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE,
    full_name VARCHAR(255),
    user_type VARCHAR(20), -- CUSTOMER, RESTAURANT_OWNER, COURIER, ADMIN
    is_verified BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- 1. Create restaurants table
CREATE TABLE IF NOT EXISTS restaurants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    is_open BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE,
    minimum_order DECIMAL(10,2) DEFAULT 0,
    delivery_fee DECIMAL(10,2) DEFAULT 0,
    delivery_time_min INT,
    delivery_time_max INT,
    rating DECIMAL(3,2) DEFAULT 0,
    review_count INT DEFAULT 0,
    max_active_orders INT DEFAULT 20,
    avg_preparation_time INT,
    avg_delivery_time INT,
    cancellation_rate DECIMAL(5,2),
    quality_score DECIMAL(5,2),
    deleted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
-- Registro de eventos de negocio y comportamiento, no solo técnicos.
CREATE TABLE events (
     id BIGSERIAL PRIMARY KEY,
     entity_type VARCHAR(50),
     entity_id BIGINT,
     event_type VARCHAR(50),
     metadata JSONB,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Define cuánto cobra la plataforma a cada restaurante por pedido.
-- Ejemplo real
-- “Este restaurante paga 30% + 0.50€ por pedido”
-- INSERT INTO restaurant_commissions VALUES (42, 30.00, 0.50, '2025-01-01', NULL);
CREATE TABLE IF NOT EXISTS restaurant_commissions (
    restaurant_id BIGINT REFERENCES restaurants(id),
    commission_percent DECIMAL(5,2),
    fixed_fee DECIMAL(10,2),
    start_date TIMESTAMP,
    end_date TIMESTAMP
);
--Tabla para promocionar restaurantes dentro de la app pagando por visibilidad.
-- Ejemplo real
-- “Burger King paga 300 € para salir primero esta semana”
-- INSERT INTO sponsored_restaurants VALUES (42, 1, '2025-01-01', '2025-01-07', 300.00);
CREATE TABLE IF NOT EXISTS sponsored_restaurants (
    restaurant_id BIGINT REFERENCES restaurants(id),
    priority INT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    budget DECIMAL(10,2)
);

-- restaurant_status EjemploS:
-- BUSY → demasiados pedidos
-- PAUSED → falta stock
-- CLOSED → fuera de horario
CREATE TABLE IF NOT EXISTS restaurant_status (
    restaurant_id BIGINT PRIMARY KEY REFERENCES restaurants(id),
    status VARCHAR(30), -- OPEN, PAUSED, BUSY, CLOSED
    reason TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Create menu_categories table (MOVIDA AQUÍ ARRIBA)
CREATE TABLE IF NOT EXISTS menu_categories (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT REFERENCES restaurants(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    icon VARCHAR(255),
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- 3. Create dishes table (MOVIDA AQUÍ ABAJO)
CREATE TABLE IF NOT EXISTS dishes (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT REFERENCES restaurants(id) ON DELETE CASCADE,
    category_id BIGINT REFERENCES menu_categories(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE,
    is_popular BOOLEAN DEFAULT FALSE,
    preparation_time INT,
    stock INT,
    stock_unlimited BOOLEAN DEFAULT TRUE,
    popularity_score DECIMAL(5,2),
    deleted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
-- Ej: desayuno / menú del día.
CREATE TABLE IF NOT EXISTS dish_availability (
    dish_id BIGINT REFERENCES dishes(id),
    day_of_week INT,
    start_time TIME,
    end_time TIME
);
-- 4. Create cuisine_types table (Globales)
CREATE TABLE IF NOT EXISTS cuisine_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
    );

-- 5. Create restaurant_cuisines table (Muchos a Muchos)
CREATE TABLE IF NOT EXISTS restaurant_cuisines (
    restaurant_id BIGINT REFERENCES restaurants(id) ON DELETE CASCADE,
    cuisine_id BIGINT REFERENCES cuisine_types(id) ON DELETE CASCADE,
    PRIMARY KEY (restaurant_id, cuisine_id)
    );
CREATE TABLE IF NOT EXISTS couriers (
      id BIGSERIAL PRIMARY KEY,
      name VARCHAR(255),
      vehicle_type VARCHAR(50),
      user_id BIGINT REFERENCES users(id),
      license_number VARCHAR(50),
      license_expiry DATE,
      vehicle_plate VARCHAR(20),
      current_latitude DECIMAL(9,6),
      current_longitude DECIMAL(9,6),
      is_online BOOLEAN DEFAULT FALSE,
      current_orders_count INT DEFAULT 0,
      is_active BOOLEAN
);
-- Create orders table if it doesn't exist
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2),
    delivery_fee DECIMAL(10,2),
    delivery_type VARCHAR(20), -- DELIVERY / PICKUP
    service_fee DECIMAL(10,2),
    tax_amount DECIMAL(10,2),
    discount_amount DECIMAL(10,2),
    status VARCHAR(50) NOT NULL, -- todo cambiar esta linea por esto "status order_status NOT NULL"
    delivery_address TEXT,
    order_date TIMESTAMP NOT NULL,
    courier_id BIGINT REFERENCES couriers(id),
    estimated_delivery_time TIMESTAMP,
    scheduled_delivery_time TIMESTAMP,
    actual_delivery_time TIMESTAMP,
    preparation_started_at TIMESTAMP,
    picked_up_at TIMESTAMP,
    platform_commission DECIMAL(10,2),
    restaurant_earnings DECIMAL(10,2),
    courier_earnings DECIMAL(10,2),
    cancellation_reason TEXT,
    cancelled_by VARCHAR(20), -- CUSTOMER, RESTAURANT, COURIER, SYSTEM
    special_instructions TEXT,
    CONSTRAINT fk_order_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
    );

-- Agregar campos faltantes
-- Create order_items table if it doesn't exist
CREATE TABLE IF NOT EXISTS order_items (
    id SERIAL PRIMARY KEY,
    order_id INT NOT NULL,
    dish_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_item_dish FOREIGN KEY (dish_id) REFERENCES dishes(id)
    );
-- Create reviews table if it doesn't exist
CREATE TABLE IF NOT EXISTS restaurant_reviews (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT REFERENCES restaurants(id),
    user_id BIGINT,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Abre por franjas
-- Cierra días
-- Puede pausar pedidos
CREATE TABLE IF NOT EXISTS restaurant_opening_hours (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT REFERENCES restaurants(id),
    day_of_week INT CHECK (day_of_week BETWEEN 0 AND 6),
    open_time TIME,
    close_time TIME,
    is_closed BOOLEAN DEFAULT FALSE
);
CREATE TABLE IF NOT EXISTS restaurant_closures (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT REFERENCES restaurants(id),
    start_datetime TIMESTAMP,
    end_datetime TIMESTAMP,
    reason TEXT
);
-- Gestión de estados del pedido
DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT 1
            FROM pg_type
            WHERE typname = 'order_status'
        ) THEN
            CREATE TYPE order_status AS ENUM (
                'CREATED',
                'CONFIRMED',
                'PREPARING',
                'READY',
                'PICKED_UP',
                'DELIVERED',
                'CANCELLED'
                );
        END IF;
    END
$$;
-- Registro de incidencias en pedidos
CREATE TABLE IF NOT EXISTS order_issues (
     id BIGSERIAL PRIMARY KEY,
     order_id BIGINT REFERENCES orders(id),
     type VARCHAR(50), -- LATE, WRONG_ITEM, COLD
     description TEXT,
     created_at TIMESTAMP
);

-- ✔ Cálculo de distancia
-- ✔ Zonas de entrega
-- ✔ Múltiples direcciones
CREATE TABLE IF NOT EXISTS addresses (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT,
     restaurant_id BIGINT,
     street VARCHAR(100),
     state VARCHAR(100),
     zipCode VARCHAR(6),
     country VARCHAR(100),
     address_line TEXT,
     city VARCHAR(100),
     latitude DECIMAL(9,6),
     longitude DECIMAL(9,6),
     is_verified BOOLEAN DEFAULT FALSE,
     place_id VARCHAR(255), -- Google Places ID
     formatted_address TEXT,
     delivery_instructions TEXT,
     address_type VARCHAR(20), -- HOME, WORK, OTHER
     floor VARCHAR(10),
     apartment VARCHAR(10),
     is_default BOOLEAN DEFAULT FALSE
);

-- Geocoding y validación
ALTER TABLE addresses ADD COLUMN is_verified BOOLEAN DEFAULT FALSE;
ALTER TABLE addresses ADD COLUMN place_id VARCHAR(255); -- Google Places ID
ALTER TABLE addresses ADD COLUMN formatted_address TEXT;
ALTER TABLE addresses ADD COLUMN delivery_instructions TEXT;
ALTER TABLE addresses ADD COLUMN address_type VARCHAR(20); -- HOME, WORK, OTHER
ALTER TABLE addresses ADD COLUMN floor VARCHAR(10);
ALTER TABLE addresses ADD COLUMN apartment VARCHAR(10);
-- Precios distintos por zona
-- Validación de pedidos
CREATE TABLE IF NOT EXISTS restaurant_delivery_zones (
     id BIGSERIAL PRIMARY KEY,
     restaurant_id BIGINT REFERENCES restaurants(id),
     radius_km DECIMAL(5,2),
     minimum_order DECIMAL(10,2),
     delivery_fee DECIMAL(10,2)
);
-- Promociones y descuentos
CREATE TABLE IF NOT EXISTS promotions (
     id BIGSERIAL PRIMARY KEY,
     restaurant_id BIGINT REFERENCES restaurants(id),
     code VARCHAR(50),
     discount_type VARCHAR(20), -- PERCENT / FIXED
     discount_value DECIMAL(10,2),
     min_order_amount DECIMAL(10,2),
     start_date TIMESTAMP,
     end_date TIMESTAMP,
     max_uses INT,
     times_used INT DEFAULT 0,
     user_specific BOOLEAN DEFAULT FALSE,
     first_order_only BOOLEAN DEFAULT FALSE,
     is_active BOOLEAN DEFAULT TRUE
);
CREATE TABLE user_promotion_usage (
      user_id BIGINT REFERENCES users(id),
      promotion_id BIGINT REFERENCES promotions(id),
      order_id BIGINT REFERENCES orders(id),
      used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      PRIMARY KEY (user_id, promotion_id, order_id)
);
-- Precios dinámicos según demanda (como Uber)
CREATE TABLE surge_pricing (
      id BIGSERIAL PRIMARY KEY,
      restaurant_id BIGINT REFERENCES restaurants(id),
      zone_id BIGINT,
      multiplier DECIMAL(3,2), -- 1.5x, 2.0x
      start_time TIMESTAMP,
      end_time TIMESTAMP,
      reason VARCHAR(50) -- HIGH_DEMAND, WEATHER, HOLIDAY
);
--Tamaños
-- Extras
-- Quitar ingredientes
-- Opciones del plato (obligatorios) ejemplo: tamaño
CREATE TABLE IF NOT EXISTS dish_options (
     id BIGSERIAL PRIMARY KEY,
     dish_id BIGINT REFERENCES dishes(id),
     name VARCHAR(100),
     required BOOLEAN DEFAULT FALSE
);
-- Extras (opcionales) ejemplo: queso extra, bacon
CREATE TABLE IF NOT EXISTS dish_option_values (
     id BIGSERIAL PRIMARY KEY,
     option_id BIGINT REFERENCES dish_options(id),
     name VARCHAR(100),
     extra_price DECIMAL(10,2)
);
--
CREATE TABLE IF NOT EXISTS audit_logs (
     id BIGSERIAL PRIMARY KEY,
     table_name VARCHAR(50),
     record_id BIGINT,
     action VARCHAR(20),
     old_data JSONB,
     new_data JSONB,
     created_at TIMESTAMP
);
-- Para seguimiento de pedidos en vivo (como Uber Eats)
CREATE TABLE IF NOT EXISTS order_tracking (
     id BIGSERIAL PRIMARY KEY,
     order_id BIGINT REFERENCES orders(id),
     courier_id BIGINT REFERENCES couriers(id),
     latitude DECIMAL(9,6),
     longitude DECIMAL(9,6),
     status VARCHAR(50),
     estimated_arrival TIMESTAMP,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- gestión de pagos, métodos de pago, transacciones
CREATE TABLE IF NOT EXISTS payment_methods (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT REFERENCES users(id),
     type VARCHAR(20), -- CARD, PAYPAL, CASH, WALLET
     provider VARCHAR(50), -- STRIPE, PAYPAL
     token VARCHAR(255), -- Token del proveedor de pago
     last_four VARCHAR(4),
     is_default BOOLEAN DEFAULT FALSE,
     is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS transactions (
     id BIGSERIAL PRIMARY KEY,
     order_id BIGINT REFERENCES orders(id),
     payment_method_id BIGINT REFERENCES payment_methods(id),
     amount DECIMAL(10,2),
     status VARCHAR(20), -- PENDING, COMPLETED, FAILED, REFUNDED
     provider_transaction_id VARCHAR(255),
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Pagos a restaurantes y couriers
CREATE TABLE IF NOT EXISTS payouts (
     id BIGSERIAL PRIMARY KEY,
     recipient_type VARCHAR(20), -- RESTAURANT, COURIER
     recipient_id BIGINT,
     amount DECIMAL(10,2),
     period_start DATE,
     period_end DATE,
     status VARCHAR(20), -- PENDING, PROCESSING, COMPLETED
     processed_at TIMESTAMP
);
-- Fundamental para crecimiento viral
CREATE TABLE IF NOT EXISTS referral_codes (
      id BIGSERIAL PRIMARY KEY,
      user_id BIGINT REFERENCES users(id),
      code VARCHAR(20) UNIQUE,
      discount_amount DECIMAL(10,2),
      times_used INT DEFAULT 0,
      max_uses INT
);

CREATE TABLE IF NOT EXISTS user_wallets (
      id BIGSERIAL PRIMARY KEY,
      user_id BIGINT REFERENCES users(id) UNIQUE,
      balance DECIMAL(10,2) DEFAULT 0,
      currency VARCHAR(3) DEFAULT 'EUR'
);

CREATE TABLE IF NOT EXISTS wallet_transactions (
     id BIGSERIAL PRIMARY KEY,
     wallet_id BIGINT REFERENCES user_wallets(id),
     type VARCHAR(20), -- CREDIT, DEBIT, REFUND, REFERRAL_BONUS
     amount DECIMAL(10,2),
     order_id BIGINT REFERENCES orders(id),
     description TEXT,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS notification_tokens (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT REFERENCES users(id),
     token VARCHAR(255),
     platform VARCHAR(20), -- IOS, ANDROID, WEB
     is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS notifications (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT REFERENCES users(id),
     type VARCHAR(50), -- ORDER_CONFIRMED, COURIER_ASSIGNED, DELIVERED
     title VARCHAR(255),
     message TEXT,
     data JSONB,
     is_read BOOLEAN DEFAULT FALSE,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS notification_tokens (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT REFERENCES users(id),
     token VARCHAR(255),
     platform VARCHAR(20), -- IOS, ANDROID, WEB
     is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS notifications (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT REFERENCES users(id),
     type VARCHAR(50), -- ORDER_CONFIRMED, COURIER_ASSIGNED, DELIVERED
     title VARCHAR(255),
     message TEXT,
     data JSONB,
     is_read BOOLEAN DEFAULT FALSE,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Reviews de couriers
CREATE TABLE IF NOT EXISTS courier_reviews (
      id BIGSERIAL PRIMARY KEY,
      courier_id BIGINT REFERENCES couriers(id),
      order_id BIGINT REFERENCES orders(id),
      user_id BIGINT REFERENCES users(id),
      rating INT CHECK (rating BETWEEN 1 AND 5),
      comment TEXT,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Reviews de platos específicos
CREATE TABLE IF NOT EXISTS dish_reviews (
      id BIGSERIAL PRIMARY KEY,
      dish_id BIGINT REFERENCES dishes(id),
      order_id BIGINT REFERENCES orders(id),
      user_id BIGINT REFERENCES users(id),
      rating INT CHECK (rating BETWEEN 1 AND 5),
      comment TEXT,
      tags VARCHAR(50)[], -- ['delicious', 'cold', 'small_portion']
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS user_favorites (
      id BIGSERIAL PRIMARY KEY,
      user_id BIGINT REFERENCES users(id),
      restaurant_id BIGINT REFERENCES restaurants(id),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      UNIQUE(user_id, restaurant_id)
);

CREATE TABLE IF NOT EXISTS user_saved_dishes (
      id BIGSERIAL PRIMARY KEY,
      user_id BIGINT REFERENCES users(id),
      dish_id BIGINT REFERENCES dishes(id),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      UNIQUE(user_id, dish_id)
);
CREATE TABLE IF NOT EXISTS order_status_history (
      id BIGSERIAL PRIMARY KEY,
      order_id BIGINT REFERENCES orders(id),
      status order_status NOT NULL,
      changed_by_user_id BIGINT REFERENCES users(id),
      notes TEXT,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS tips (
      id BIGSERIAL PRIMARY KEY,
      order_id BIGINT REFERENCES orders(id),
      courier_id BIGINT REFERENCES couriers(id),
      amount DECIMAL(10,2),
      tip_type VARCHAR(20), -- PRE_ORDER, POST_ORDER
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-------------------------------------- INDICES ----------------------------------
CREATE INDEX IF NOT EXISTS idx_orders_restaurant_id
    ON orders (restaurant_id);
--
CREATE INDEX IF NOT EXISTS idx_orders_status
    ON orders (status);
--
CREATE INDEX IF NOT EXISTS idx_orders_restaurant_status
    ON orders (restaurant_id, status);
--
CREATE INDEX IF NOT EXISTS idx_dishes_restaurant_id
    ON dishes (restaurant_id);
--
CREATE INDEX idx_order_tracking_order ON order_tracking(order_id);
CREATE INDEX idx_order_tracking_courier ON order_tracking(courier_id);
--
CREATE INDEX idx_order_status_history_order ON order_status_history(order_id);
------------------------------------------ TRIGGERS ----------------------------------
-- updated_at automático con trigger
-- Función que actualiza la columna updated_at
CREATE OR REPLACE FUNCTION set_updated_at()
    RETURNS trigger AS $$
BEGIN
    NEW.updated_at := now();   -- asignación correcta
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
-- Trigger para la tabla restaurants
CREATE OR REPLACE TRIGGER trg_restaurants_updated_at
    BEFORE UPDATE ON restaurants
               FOR EACH ROW
EXECUTE FUNCTION set_updated_at();
-- Trigger para la tabla menu_categories
CREATE OR REPLACE TRIGGER trg_menu_categories_updated_at
    BEFORE UPDATE ON menu_categories
               FOR EACH ROW
EXECUTE FUNCTION set_updated_at();
-- Trigger para la tabla dishes
CREATE TRIGGER trg_dishes_updated_at
    BEFORE UPDATE ON dishes
               FOR EACH ROW
EXECUTE FUNCTION set_updated_at();
-- Trigger para la tabla orders
CREATE OR REPLACE TRIGGER trg_orders_updated_at
    BEFORE UPDATE ON orders
               FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

----------------------------------------------------------- Full-text search para restaurantes y platos
ALTER TABLE restaurants ADD COLUMN search_vector tsvector;
ALTER TABLE dishes ADD COLUMN search_vector tsvector;

CREATE INDEX idx_restaurants_search ON restaurants USING gin(search_vector);
CREATE INDEX idx_dishes_search ON dishes USING gin(search_vector);

-- Trigger para actualizar search_vector
CREATE OR REPLACE FUNCTION update_restaurant_search_vector()
    RETURNS trigger AS $$
BEGIN
    NEW.search_vector :=
            setweight(to_tsvector('spanish', COALESCE(NEW.name, '')), 'A') ||
            setweight(to_tsvector('spanish', COALESCE(NEW.description, '')), 'B');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_restaurants_search_vector
    BEFORE INSERT OR UPDATE ON restaurants
    FOR EACH ROW
EXECUTE FUNCTION update_restaurant_search_vector();

----------------------------------------------------------------------------------------
-- Vista materializada para dashboards
CREATE MATERIALIZED VIEW restaurant_daily_stats AS
SELECT
    restaurant_id,
    DATE(order_date) as date,
    COUNT(*) as total_orders,
    SUM(total_amount) as total_revenue,
    AVG(total_amount) as avg_order_value,
    COUNT(CASE WHEN status = 'CANCELLED' THEN 1 END) as cancelled_orders,
    AVG(EXTRACT(EPOCH FROM (actual_delivery_time - order_date))/60) as avg_delivery_minutes
FROM orders
WHERE order_date >= CURRENT_DATE - INTERVAL '90 days'
GROUP BY restaurant_id, DATE(order_date);

CREATE UNIQUE INDEX ON restaurant_daily_stats (restaurant_id, date);
----------------------------------------------------------------------------------------