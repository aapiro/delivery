-- Items dummy para el pedido del usuario CUSTOMER
-- Dishs referenciados: California Roll, Dragon Roll (Sushi Zen)

INSERT INTO order_items (order_id, dish_id, quantity, price)
SELECT
    o.id,
    d.id,
    2,
    d.price
FROM orders o
JOIN users u ON u.id = o.user_id
JOIN restaurants r ON r.id = o.restaurant_id
JOIN dishes d ON d.restaurant_id = r.id AND d.name = 'California Roll'
WHERE u.email = 'customer@delivery.local'
  AND o.delivery_address = 'Calle Principal 123, Madrid 28001'
  AND NOT EXISTS (
      SELECT 1
      FROM order_items oi
      WHERE oi.order_id = o.id
        AND oi.dish_id = d.id
  );

INSERT INTO order_items (order_id, dish_id, quantity, price)
SELECT
    o.id,
    d.id,
    1,
    d.price
FROM orders o
JOIN users u ON u.id = o.user_id
JOIN restaurants r ON r.id = o.restaurant_id
JOIN dishes d ON d.restaurant_id = r.id AND d.name = 'Dragon Roll'
WHERE u.email = 'customer@delivery.local'
  AND o.delivery_address = 'Calle Principal 123, Madrid 28001'
  AND NOT EXISTS (
      SELECT 1
      FROM order_items oi
      WHERE oi.order_id = o.id
        AND oi.dish_id = d.id
  );

