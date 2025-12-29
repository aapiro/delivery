-- 1. Insertar tipos de cocina globales
INSERT INTO cuisine_types (name) VALUES ('Italian'); -- Asumimos que genera ID 1

-- 2. Insertar restaurante (SIN cuisine_type)
INSERT INTO restaurants (name, description, image_url, is_open, minimum_order, delivery_fee, delivery_time_min, delivery_time_max, rating, review_count)
VALUES ('Sample Restaurant', 'Sample restaurant description', 'https://images.unsplash.com/photo-1514933651103-005eec06c04b?w=400&h=250&fit=crop', TRUE, 10.00, 2.99, 30, 45, 4.5, 120), -- Asumimos que genera ID 1
       ('Sample Restaurant 2', 'Sample restaurant description', 'https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400&h=250&fit=crop', TRUE, 10.00, 2.99, 30, 45, 4.5, 120); -- Asumimos que genera ID 2

-- 3. Vincular el restaurante con su tipo de cocina (Muchos a Muchos)
INSERT INTO restaurant_cuisines (restaurant_id, cuisine_id) VALUES (1, 1);

-- 4. Insertar secciones del menú (Cambiado a menu_categories)
INSERT INTO menu_categories (restaurant_id, name, slug, icon, display_order, is_active)
VALUES (1, 'Pasta', 'pasta', 'pasta-icon', 1, TRUE); -- Asumimos que genera ID 1

-- 5. Insertar platos vinculados a la categoría y al restaurante
INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
VALUES (1, 1, 'Spaghetti Carbonara', 'Classic Italian pasta dish', 12.99, 'https://example.com/spaghetti.jpg', TRUE, TRUE, 15);