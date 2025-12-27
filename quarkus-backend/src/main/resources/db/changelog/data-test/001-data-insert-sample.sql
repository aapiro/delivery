-- Insert sample data for restaurants (Cambiado a plural: restaurants)
INSERT INTO restaurants (name, cuisine_type, description, image_url, is_open, minimum_order, delivery_fee, delivery_time_min, delivery_time_max, rating, review_count) VALUES
    ('Sample Restaurant', 'Italian', 'Sample restaurant description', 'https://example.com/sample.jpg', TRUE, 10.00, 2.99, 30, 45, 4.5, 120);

-- Insert sample data for categories (Cambiado a plural: categories)
-- Usamos una subconsulta para obtener el ID del restaurante recién insertado o asumimos el 1
INSERT INTO categories (restaurant_id, name, slug, icon, display_order, is_active) VALUES
    (1, 'Italian', 'italian', 'italian', 1, TRUE);

-- Insert sample data for dishes (Asegúrate de que los nombres de columnas coincidan)
INSERT INTO dishes (restaurant_id, category_id, name, description, price, image, is_available, is_popular, preparation_time) VALUES
    (1, 1, 'Spaghetti Carbonara', 'Classic Italian pasta dish', 12.99, 'https://example.com/spaghetti.jpg', TRUE, TRUE, 15);