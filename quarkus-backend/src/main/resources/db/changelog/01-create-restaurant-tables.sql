-- Create restaurants table
CREATE TABLE IF NOT EXISTS restaurants (
                                           id BIGSERIAL PRIMARY KEY, -- Se cambia BIGINT AUTO_INCREMENT por BIGSERIAL
                                           name VARCHAR(255) NOT NULL,
    cuisine_type VARCHAR(255),
    description TEXT,
    image_url VARCHAR(255),
    is_open BOOLEAN DEFAULT TRUE,
    minimum_order DECIMAL(10,2) DEFAULT 0,
    delivery_fee DECIMAL(10,2) DEFAULT 0,
    delivery_time_min INT,
    delivery_time_max INT,
    rating DECIMAL(3,2) DEFAULT 0,
    review_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
                                          id BIGSERIAL PRIMARY KEY,
                                          restaurant_id BIGINT,
                                          name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    icon VARCHAR(255),
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) -- Corregido a "restaurants"
    );

-- Create dishes table
CREATE TABLE IF NOT EXISTS dishes (
                                      id BIGSERIAL PRIMARY KEY,
                                      restaurant_id BIGINT,
                                      category_id BIGINT,
                                      name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE,
    is_popular BOOLEAN DEFAULT FALSE, -- Cambiado de isPopular a is_popular (estilo Postgres)
    preparation_time INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_dish_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id), -- Corregido a "restaurants"
    CONSTRAINT fk_dish_category FOREIGN KEY (category_id) REFERENCES categories(id) -- Corregido a "categories"
    );