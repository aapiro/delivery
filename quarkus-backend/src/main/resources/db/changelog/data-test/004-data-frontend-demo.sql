-- =============================================================================
-- Datos de demostración para el frontend (imágenes: Unsplash — uso gratuito)
-- https://unsplash.com/license — Idempotente: no duplica si ya existen por nombre.
-- =============================================================================

-- Tipos de cocina adicionales
INSERT INTO cuisine_types (name) VALUES
    ('Mexican'),
    ('Japanese'),
    ('Mediterranean'),
    ('American'),
    ('Indian'),
    ('Fast Food')
ON CONFLICT (name) DO NOTHING;

-- Corregir imagen placeholder del seed inicial
UPDATE dishes
SET image_url = 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=800&q=80'
WHERE image_url = 'https://example.com/spaghetti.jpg';

-- ---------------------------------------------------------------------------
-- Restaurante: Sushi Zen (japonés)
-- ---------------------------------------------------------------------------
INSERT INTO restaurants (name, description, image_url, is_open, minimum_order, delivery_fee, delivery_time_min, delivery_time_max, rating, review_count)
SELECT
    'Sushi Zen',
    'Rollos, nigiri y sashimi con pescado fresco. Delivery rápido.',
    'https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=800&q=80',
    TRUE,
    15.00,
    3.50,
    25,
    40,
    4.8,
    89
WHERE NOT EXISTS (SELECT 1 FROM restaurants WHERE name = 'Sushi Zen');

INSERT INTO restaurant_cuisines (restaurant_id, cuisine_id)
SELECT r.id, ct.id
FROM restaurants r
JOIN cuisine_types ct ON ct.name = 'Japanese'
WHERE r.name = 'Sushi Zen'
ON CONFLICT DO NOTHING;

INSERT INTO menu_categories (restaurant_id, name, slug, icon, display_order, is_active)
SELECT r.id, 'Rolls', 'rolls', 'sushi', 1, TRUE
FROM restaurants r
WHERE r.name = 'Sushi Zen'
  AND NOT EXISTS (SELECT 1 FROM menu_categories mc WHERE mc.restaurant_id = r.id AND mc.slug = 'rolls');

INSERT INTO menu_categories (restaurant_id, name, slug, icon, display_order, is_active)
SELECT r.id, 'Nigiri', 'nigiri', 'nigiri', 2, TRUE
FROM restaurants r
WHERE r.name = 'Sushi Zen'
  AND NOT EXISTS (SELECT 1 FROM menu_categories mc WHERE mc.restaurant_id = r.id AND mc.slug = 'nigiri');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'California Roll', 'Cangrejo, aguacate y pepino envueltos en alga nori.', 9.95,
       'https://images.unsplash.com/photo-1617196034184-9e8d8b5c6c0d?w=800&q=80', TRUE, TRUE, 12
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'rolls'
WHERE r.name = 'Sushi Zen'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'California Roll');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Dragon Roll', 'Anguila, aguacate y salsa de anguila.', 12.50,
       'https://images.unsplash.com/photo-1611143669185-af224c5e3252?w=800&q=80', TRUE, TRUE, 15
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'rolls'
WHERE r.name = 'Sushi Zen'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Dragon Roll');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Salmón Nigiri', 'Dos piezas de nigiri de salmón.', 7.50,
       'https://images.unsplash.com/photo-1617196034796-73a7b6040a84?w=800&q=80', TRUE, FALSE, 10
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'nigiri'
WHERE r.name = 'Sushi Zen'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Salmón Nigiri');

-- ---------------------------------------------------------------------------
-- Restaurante: Burger House (americana / fast food)
-- ---------------------------------------------------------------------------
INSERT INTO restaurants (name, description, image_url, is_open, minimum_order, delivery_fee, delivery_time_min, delivery_time_max, rating, review_count)
SELECT
    'Burger House',
    'Hamburguesas smash, patatas y batidos. Todo a la parrilla.',
    'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&q=80',
    TRUE,
    8.00,
    2.49,
    20,
    35,
    4.6,
    210
WHERE NOT EXISTS (SELECT 1 FROM restaurants WHERE name = 'Burger House');

INSERT INTO restaurant_cuisines (restaurant_id, cuisine_id)
SELECT r.id, ct.id
FROM restaurants r
JOIN cuisine_types ct ON ct.name = 'American'
WHERE r.name = 'Burger House'
ON CONFLICT DO NOTHING;

INSERT INTO restaurant_cuisines (restaurant_id, cuisine_id)
SELECT r.id, ct.id
FROM restaurants r
JOIN cuisine_types ct ON ct.name = 'Fast Food'
WHERE r.name = 'Burger House'
ON CONFLICT DO NOTHING;

INSERT INTO menu_categories (restaurant_id, name, slug, icon, display_order, is_active)
SELECT r.id, 'Hamburguesas', 'burgers', 'burger', 1, TRUE
FROM restaurants r
WHERE r.name = 'Burger House'
  AND NOT EXISTS (SELECT 1 FROM menu_categories mc WHERE mc.restaurant_id = r.id AND mc.slug = 'burgers');

INSERT INTO menu_categories (restaurant_id, name, slug, icon, display_order, is_active)
SELECT r.id, 'Sides', 'sides', 'fries', 2, TRUE
FROM restaurants r
WHERE r.name = 'Burger House'
  AND NOT EXISTS (SELECT 1 FROM menu_categories mc WHERE mc.restaurant_id = r.id AND mc.slug = 'sides');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Classic Double', 'Dos carne smash, queso cheddar y salsa especial.', 11.90,
       'https://images.unsplash.com/photo-1550547660-d9450f859349?w=800&q=80', TRUE, TRUE, 18
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'burgers'
WHERE r.name = 'Burger House'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Classic Double');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Bacon BBQ', 'Carne, bacon crujiente, cebolla caramelizada y BBQ.', 13.50,
       'https://images.unsplash.com/photo-1594212699903-ec8a3eca50f5?w=800&q=80', TRUE, TRUE, 20
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'burgers'
WHERE r.name = 'Burger House'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Bacon BBQ');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Patatas caseras', 'Patatas con piel y salsa a elegir.', 4.50,
       'https://images.unsplash.com/photo-1573080496219-bb080dd4d626?w=800&q=80', TRUE, FALSE, 10
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'sides'
WHERE r.name = 'Burger House'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Patatas caseras');

-- ---------------------------------------------------------------------------
-- Restaurante: Taquería El Sol (mexicana)
-- ---------------------------------------------------------------------------
INSERT INTO restaurants (name, description, image_url, is_open, minimum_order, delivery_fee, delivery_time_min, delivery_time_max, rating, review_count)
SELECT
    'Taquería El Sol',
    'Tacos al pastor, carnitas y salsas frescas.',
    'https://images.unsplash.com/photo-1565299586573-866561916b88?w=800&q=80',
    TRUE,
    10.00,
    2.99,
    22,
    38,
    4.7,
    156
WHERE NOT EXISTS (SELECT 1 FROM restaurants WHERE name = 'Taquería El Sol');

INSERT INTO restaurant_cuisines (restaurant_id, cuisine_id)
SELECT r.id, ct.id
FROM restaurants r
JOIN cuisine_types ct ON ct.name = 'Mexican'
WHERE r.name = 'Taquería El Sol'
ON CONFLICT DO NOTHING;

INSERT INTO menu_categories (restaurant_id, name, slug, icon, display_order, is_active)
SELECT r.id, 'Tacos', 'tacos', 'taco', 1, TRUE
FROM restaurants r
WHERE r.name = 'Taquería El Sol'
  AND NOT EXISTS (SELECT 1 FROM menu_categories mc WHERE mc.restaurant_id = r.id AND mc.slug = 'tacos');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Tacos al pastor', 'Tres tacos con piña, cebolla y cilantro.', 8.50,
       'https://images.unsplash.com/photo-1551504734-5ee1c4a1479b?w=800&q=80', TRUE, TRUE, 14
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'tacos'
WHERE r.name = 'Taquería El Sol'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Tacos al pastor');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Nachos con guacamole', 'Totopos, queso, jalapeños y guacamole.', 7.00,
       'https://images.unsplash.com/photo-1513456852971-30c0b8199d4d?w=800&q=80', TRUE, FALSE, 12
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'tacos'
WHERE r.name = 'Taquería El Sol'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Nachos con guacamole');

-- ---------------------------------------------------------------------------
-- Restaurante: Green Bowl (mediterráneo / saludable)
-- ---------------------------------------------------------------------------
INSERT INTO restaurants (name, description, image_url, is_open, minimum_order, delivery_fee, delivery_time_min, delivery_time_max, rating, review_count)
SELECT
    'Green Bowl',
    'Ensaladas, bowls y hummus. Ingredientes frescos.',
    'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800&q=80',
    TRUE,
    12.00,
    2.00,
    18,
    32,
    4.5,
    98
WHERE NOT EXISTS (SELECT 1 FROM restaurants WHERE name = 'Green Bowl');

INSERT INTO restaurant_cuisines (restaurant_id, cuisine_id)
SELECT r.id, ct.id
FROM restaurants r
JOIN cuisine_types ct ON ct.name = 'Mediterranean'
WHERE r.name = 'Green Bowl'
ON CONFLICT DO NOTHING;

INSERT INTO menu_categories (restaurant_id, name, slug, icon, display_order, is_active)
SELECT r.id, 'Bowls', 'bowls', 'bowl', 1, TRUE
FROM restaurants r
WHERE r.name = 'Green Bowl'
  AND NOT EXISTS (SELECT 1 FROM menu_categories mc WHERE mc.restaurant_id = r.id AND mc.slug = 'bowls');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Bowl griego', 'Quinoa, feta, tomate, pepino y aceitunas.', 10.50,
       'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800&q=80', TRUE, TRUE, 12
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'bowls'
WHERE r.name = 'Green Bowl'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Bowl griego');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Hummus y pan de pita', 'Hummus con pimentón y pan caliente.', 6.50,
       'https://images.unsplash.com/photo-1577805947697-89e18249d767?w=800&q=80', TRUE, FALSE, 8
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'bowls'
WHERE r.name = 'Green Bowl'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Hummus y pan de pita');

-- ---------------------------------------------------------------------------
-- Restaurante: Curry House (india)
-- ---------------------------------------------------------------------------
INSERT INTO restaurants (name, description, image_url, is_open, minimum_order, delivery_fee, delivery_time_min, delivery_time_max, rating, review_count)
SELECT
    'Curry House',
    'Currys, naan recién horneado y arroz basmati.',
    'https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=800&q=80',
    TRUE,
    14.00,
    3.00,
    28,
    45,
    4.6,
    134
WHERE NOT EXISTS (SELECT 1 FROM restaurants WHERE name = 'Curry House');

INSERT INTO restaurant_cuisines (restaurant_id, cuisine_id)
SELECT r.id, ct.id
FROM restaurants r
JOIN cuisine_types ct ON ct.name = 'Indian'
WHERE r.name = 'Curry House'
ON CONFLICT DO NOTHING;

INSERT INTO menu_categories (restaurant_id, name, slug, icon, display_order, is_active)
SELECT r.id, 'Currys', 'currys', 'curry', 1, TRUE
FROM restaurants r
WHERE r.name = 'Curry House'
  AND NOT EXISTS (SELECT 1 FROM menu_categories mc WHERE mc.restaurant_id = r.id AND mc.slug = 'currys');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Pollo tikka masala', 'Pollo en salsa cremosa de tomate y especias.', 11.50,
       'https://images.unsplash.com/photo-1618449840615-9d1e0e7d0e0e?w=800&q=80', TRUE, TRUE, 22
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'currys'
WHERE r.name = 'Curry House'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Pollo tikka masala');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Dal tadka', 'Lentejas amarillas con especias aromáticas.', 9.00,
       'https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=800&q=80', TRUE, FALSE, 18
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'currys'
WHERE r.name = 'Curry House'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Dal tadka');

-- ---------------------------------------------------------------------------
-- Ampliar restaurantes del seed inicial (Sample Restaurant / Sample Restaurant 2)
-- ---------------------------------------------------------------------------
INSERT INTO menu_categories (restaurant_id, name, slug, icon, display_order, is_active)
SELECT r.id, 'Pizza', 'pizza', 'pizza', 2, TRUE
FROM restaurants r
WHERE r.name = 'Sample Restaurant'
  AND NOT EXISTS (SELECT 1 FROM menu_categories mc WHERE mc.restaurant_id = r.id AND mc.slug = 'pizza');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Pizza Margherita', 'Tomate, mozzarella fresca y albahaca.', 10.50,
       'https://images.unsplash.com/photo-1513104890138-7c749659a591?w=800&q=80', TRUE, TRUE, 20
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'pizza'
WHERE r.name = 'Sample Restaurant'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Pizza Margherita');

INSERT INTO menu_categories (restaurant_id, name, slug, icon, display_order, is_active)
SELECT r.id, 'Postres', 'desserts', 'cake', 3, TRUE
FROM restaurants r
WHERE r.name = 'Sample Restaurant'
  AND NOT EXISTS (SELECT 1 FROM menu_categories mc WHERE mc.restaurant_id = r.id AND mc.slug = 'desserts');

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Tiramisú', 'Capas de mascarpone y café.', 5.90,
       'https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=800&q=80', TRUE, FALSE, 5
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'desserts'
WHERE r.name = 'Sample Restaurant'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Tiramisú');

INSERT INTO menu_categories (restaurant_id, name, slug, icon, display_order, is_active)
SELECT r.id, 'Ensaladas', 'salads', 'salad', 1, TRUE
FROM restaurants r
WHERE r.name = 'Sample Restaurant 2'
  AND NOT EXISTS (SELECT 1 FROM menu_categories mc WHERE mc.restaurant_id = r.id AND mc.slug = 'salads');

INSERT INTO restaurant_cuisines (restaurant_id, cuisine_id)
SELECT r.id, ct.id
FROM restaurants r
JOIN cuisine_types ct ON ct.name = 'Mediterranean'
WHERE r.name = 'Sample Restaurant 2'
ON CONFLICT DO NOTHING;

INSERT INTO dishes (restaurant_id, category_id, name, description, price, image_url, is_available, is_popular, preparation_time)
SELECT r.id, mc.id, 'Ensalada César', 'Pollo a la Parrilla, parmesano y picatostes.', 9.50,
       'https://images.unsplash.com/photo-1546793665-c74683f339c1?w=800&q=80', TRUE, TRUE, 10
FROM restaurants r
JOIN menu_categories mc ON mc.restaurant_id = r.id AND mc.slug = 'salads'
WHERE r.name = 'Sample Restaurant 2'
  AND NOT EXISTS (SELECT 1 FROM dishes d WHERE d.restaurant_id = r.id AND d.name = 'Ensalada César');
