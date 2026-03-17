-- SQL para crear tabla restaurants
CREATE TABLE IF NOT EXISTS restaurants (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  cuisine VARCHAR(100),
  rating NUMERIC(3,2),
  review_count INTEGER DEFAULT 0,
  delivery_time_min INTEGER,
  delivery_time_max INTEGER,
  delivery_fee NUMERIC(8,2),
  minimum_order NUMERIC(8,2),
  is_open BOOLEAN DEFAULT true,
  image_url VARCHAR(1024)
);

-- Índice de búsqueda por nombre
CREATE INDEX IF NOT EXISTS idx_restaurants_name ON restaurants (name);

