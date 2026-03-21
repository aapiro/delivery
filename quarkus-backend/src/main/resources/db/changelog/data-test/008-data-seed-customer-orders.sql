-- Pedidos dummy para el usuario CUSTOMER
-- Restaurant referenciado: Sushi Zen

INSERT INTO orders (user_id, restaurant_id, total_amount, status, delivery_address, order_date, estimated_delivery_time)
SELECT
    u.id,
    r.id,
    32.40,
    'PENDING',
    'Calle Principal 123, Madrid 28001',
    CURRENT_TIMESTAMP - INTERVAL '2 hours',
    CURRENT_TIMESTAMP + INTERVAL '35 minutes'
FROM users u
JOIN restaurants r ON r.name = 'Sushi Zen'
WHERE u.email = 'customer@delivery.local'
  AND NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.user_id = u.id
      AND o.restaurant_id = r.id
      AND o.delivery_address = 'Calle Principal 123, Madrid 28001'
  );

