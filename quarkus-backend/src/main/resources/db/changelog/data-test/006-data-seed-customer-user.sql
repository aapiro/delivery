-- Usuario CUSTOMER de demo (login público)
-- Recomendado para pruebas de integración:
--   email: customer@delivery.local
--   password: admin123
--
-- Nota: reutilizamos el mismo password_hash del admin seed para evitar generar un bcrypt nuevo.

INSERT INTO users (email, full_name, password_hash, user_type, is_verified, is_active)
VALUES (
    'customer@delivery.local',
    'Customer Demo',
    '$2b$12$p3uH48mOC4ImsMHbga959u.oL.gZ9LJ37cBntyMf0tmM3O7.K0yQe',
    'CUSTOMER',
    TRUE,
    TRUE
)
ON CONFLICT (email) DO NOTHING;

