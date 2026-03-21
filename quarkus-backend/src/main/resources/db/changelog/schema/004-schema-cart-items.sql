CREATE TABLE IF NOT EXISTS cart_items (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    dish_id BIGINT NOT NULL REFERENCES dishes(id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity > 0),
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_cart_user_dish UNIQUE (user_id, dish_id)
);

CREATE INDEX IF NOT EXISTS idx_cart_items_user_id ON cart_items(user_id);
CREATE INDEX IF NOT EXISTS idx_cart_items_dish_id ON cart_items(dish_id);

