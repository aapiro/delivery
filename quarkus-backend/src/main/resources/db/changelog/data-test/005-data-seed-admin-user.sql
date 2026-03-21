-- Usuario administrador de desarrollo (login panel: admin@delivery.local / admin123)
-- Hash BCrypt (cost 12) generado con Node bcrypt; compatible con at.favre.lib.bcrypt.
INSERT INTO users (email, full_name, password_hash, user_type, is_verified, is_active)
VALUES (
    'admin@delivery.local',
    'Administrador',
    '$2b$12$p3uH48mOC4ImsMHbga959u.oL.gZ9LJ37cBntyMf0tmM3O7.K0yQe',
    'SUPER_ADMIN',
    TRUE,
    TRUE
)
ON CONFLICT (email) DO NOTHING;
