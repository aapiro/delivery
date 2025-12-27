CREATE TABLE IF NOT EXISTS usuarios (
                                        id BIGSERIAL PRIMARY KEY,
                                        nombre VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    created_at TIMESTAMP
    );
