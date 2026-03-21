-- Items dummy para el carrito del usuario CUSTOMER
-- Dishs: California Roll (Sushi Zen)

INSERT INTO cart_items (user_id, dish_id, quantity, special_instructions)
SELECT
    u.id,
    d.id,
    1,
    'Sin cebolla'
FROM users u
JOIN restaurants r ON r.name = 'Sushi Zen'
JOIN dishes d ON d.restaurant_id = r.id AND d.name = 'California Roll'
WHERE u.email = 'customer@delivery.local'
ON CONFLICT (user_id, dish_id) DO NOTHING;

