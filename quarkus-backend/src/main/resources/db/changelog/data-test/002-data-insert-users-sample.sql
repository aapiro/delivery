-- ========================================
-- DATOS DE PRUEBA PARA TESTS
-- Archivo: src/main/resources/db/changelog/data/test-data.sql
-- ========================================

-- Usuarios de prueba básicos
INSERT INTO usuarios (nombre, email, created_at) VALUES
                                                     ('Juan Pérez', 'juan.perez@test.com', CURRENT_TIMESTAMP),
                                                     ('María García', 'maria.garcia@test.com', CURRENT_TIMESTAMP),
                                                     ('Carlos López', 'carlos.lopez@test.com', CURRENT_TIMESTAMP),
                                                     ('Ana Martínez', 'ana.martinez@test.com', CURRENT_TIMESTAMP),
                                                     ('Pedro Rodríguez', 'pedro.rodriguez@test.com', CURRENT_TIMESTAMP)
    ON CONFLICT (email) DO NOTHING;

-- Usuarios con casos especiales
INSERT INTO usuarios (nombre, email, created_at) VALUES
                                                     ('Usuario con Ñ', 'usuario.ñ@test.com', CURRENT_TIMESTAMP),
                                                     ('Usuario Accentué', 'usuario.é@test.com', CURRENT_TIMESTAMP),
                                                     ('User English', 'user.english@test.com', CURRENT_TIMESTAMP - INTERVAL '30 days'),
                                                     ('Usuario Antiguo', 'antiguo@test.com', CURRENT_TIMESTAMP - INTERVAL '1 year')
    ON CONFLICT (email) DO NOTHING;

-- Usuarios para tests específicos
INSERT INTO usuarios (nombre, email, created_at) VALUES
                                                     ('Test Delete', 'test.delete@test.com', CURRENT_TIMESTAMP),
                                                     ('Test Update', 'test.update@test.com', CURRENT_TIMESTAMP),
                                                     ('Test Query', 'test.query@test.com', CURRENT_TIMESTAMP)
    ON CONFLICT (email) DO NOTHING;

-- Usuarios con diferentes dominios (para tests de búsqueda)
INSERT INTO usuarios (nombre, email, created_at) VALUES
                                                     ('Usuario Gmail', 'user@gmail.com', CURRENT_TIMESTAMP),
                                                     ('Usuario Outlook', 'user@outlook.com', CURRENT_TIMESTAMP),
                                                     ('Usuario Yahoo', 'user@yahoo.com', CURRENT_TIMESTAMP),
                                                     ('Usuario Corporativo', 'user@company.com', CURRENT_TIMESTAMP)
    ON CONFLICT (email) DO NOTHING;

-- Dataset grande para tests de performance (opcional)
-- Descomenta si necesitas muchos registros
/*
INSERT INTO usuarios (nombre, email, created_at)
SELECT
    'Usuario ' || i,
    'usuario' || i || '@bulk.com',
    CURRENT_TIMESTAMP - (random() * INTERVAL '365 days')
FROM generate_series(1, 1000) AS i
ON CONFLICT (email) DO NOTHING;
*/