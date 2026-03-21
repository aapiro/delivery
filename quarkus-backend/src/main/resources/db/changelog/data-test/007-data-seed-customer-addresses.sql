-- Direcciones dummy para el usuario CUSTOMER

INSERT INTO addresses (user_id, street, city, state, zipCode, country, is_default, delivery_instructions, address_type)
SELECT
    u.id,
    'Calle Principal 123',
    'Madrid',
    '',
    '28001',
    'Spain',
    TRUE,
    '',
    'HOME'
FROM users u
WHERE u.email = 'customer@delivery.local'
  AND NOT EXISTS (
    SELECT 1
    FROM addresses a
    WHERE a.user_id = u.id
      AND a.street = 'Calle Principal 123'
      AND a.zipCode = '28001'
  );

INSERT INTO addresses (user_id, street, city, state, zipCode, country, is_default, delivery_instructions, address_type)
SELECT
    u.id,
    'Calle Secundaria 456',
    'Barcelona',
    '',
    '08001',
    'Spain',
    FALSE,
    '',
    'WORK'
FROM users u
WHERE u.email = 'customer@delivery.local'
  AND NOT EXISTS (
    SELECT 1
    FROM addresses a
    WHERE a.user_id = u.id
      AND a.street = 'Calle Secundaria 456'
      AND a.zipCode = '08001'
  );

