#!/usr/bin/env python3

import os
import sys
import sqlite3
from pathlib import Path

# Agregar el directorio actual al path para poder importar los módulos de migración
sys.path.insert(0, str(Path(__file__).parent))

def create_database():
    """Crear la base de datos SQLite y ejecutar las migraciones"""
    
    # Crear conexión a la base de datos
    conn = sqlite3.connect('database.sqlite')
    cursor = conn.cursor()
    
    print("Creando tablas...")
    
    # Ejecutar migración para crear tabla de usuarios
    create_user_table_sql = """
    CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        email TEXT UNIQUE NOT NULL,
        password TEXT NOT NULL,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );
    """
    
    cursor.execute(create_user_table_sql)
    
    # Ejecutar migración para crear tabla de restaurantes (si no existe ya)
    create_restaurant_tables_sql = """
    CREATE TABLE IF NOT EXISTS restaurants (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        description TEXT,
        logo TEXT,
        cover_image TEXT,
        address TEXT,
        phone TEXT,
        email TEXT,
        cuisine_type TEXT,
        rating REAL DEFAULT 0.0,
        review_count INTEGER DEFAULT 0,
        delivery_time_min INTEGER,
        delivery_time_max INTEGER,
        delivery_fee REAL DEFAULT 0.0,
        minimum_order REAL DEFAULT 0.0,
        is_open BOOLEAN DEFAULT 1,
        is_active BOOLEAN DEFAULT 1,
        category_id INTEGER,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );
    """
    
    cursor.execute(create_restaurant_tables_sql)
    
    # Ejecutar migración para crear tabla de categorías (si no existe ya)
    create_category_table_sql = """
    CREATE TABLE IF NOT EXISTS categories (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL UNIQUE,
        slug TEXT UNIQUE,
        icon TEXT,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );
    """
    
    cursor.execute(create_category_table_sql)
    
    # Ejecutar migración para crear tabla de platos (si no existe ya)
    create_dish_table_sql = """
    CREATE TABLE IF NOT EXISTS dishes (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        restaurant_id INTEGER,
        category_id INTEGER,
        name TEXT NOT NULL,
        description TEXT,
        price REAL NOT NULL,
        image_url TEXT,
        is_available BOOLEAN DEFAULT 1,
        is_popular BOOLEAN DEFAULT 0,
        preparation_time INTEGER,
        ingredients TEXT,
        allergens TEXT,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );
    """
    
    cursor.execute(create_dish_table_sql)
    
    # Ejecutar migración para crear tabla de pedidos (si no existe ya)
    create_order_table_sql = """
    CREATE TABLE IF NOT EXISTS orders (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        user_id INTEGER,
        restaurant_id INTEGER,
        status TEXT DEFAULT 'pending',
        total_amount REAL NOT NULL,
        delivery_address TEXT,
        delivery_fee REAL DEFAULT 0.0,
        tax_amount REAL DEFAULT 0.0,
        special_instructions TEXT,
        estimated_delivery_time DATETIME,
        actual_delivery_time DATETIME,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );
    """
    
    cursor.execute(create_order_table_sql)
    
    # Ejecutar migración para crear tabla de items de pedido (si no existe ya)
    create_order_item_table_sql = """
    CREATE TABLE IF NOT EXISTS order_items (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        order_id INTEGER,
        dish_id INTEGER,
        quantity INTEGER NOT NULL,
        unit_price REAL NOT NULL,
        total_price REAL NOT NULL,
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );
    """
    
    cursor.execute(create_order_item_table_sql)
    
    # Insertar datos de prueba si no existen
    print("Insertando datos de prueba...")
    
    try:
        # Insertar categorías
        categories = [
            ('Pizza', 'pizza', '🍕'),
            ('Hamburguesas', 'hamburguesas', '🍔'),
            ('Sushi', 'sushi', '🍣'),
            ('Ensaladas', 'ensaladas', '🥗'),
            ('Postres', 'postres', '🍰')
        ]
        
        for name, slug, icon in categories:
            cursor.execute(
                "INSERT OR IGNORE INTO categories (name, slug, icon) VALUES (?, ?, ?)",
                (name, slug, icon)
            )
            
        # Insertar usuarios de prueba
        test_users = [
            ('John Doe', 'john@example.com', '$2b$10$example_hashed_password_1'),
            ('Jane Smith', 'jane@example.com', '$2b$10$example_hashed_password_2')
        ]
        
        for name, email, password in test_users:
            cursor.execute(
                "INSERT OR IGNORE INTO users (name, email, password) VALUES (?, ?, ?)",
                (name, email, password)
            )
            
        print("Datos de prueba insertados correctamente")
        
    except Exception as e:
        print(f"Error al insertar datos de prueba: {e}")
    
    conn.commit()
    conn.close()
    
    print("Base de datos y tablas creadas exitosamente")

if __name__ == "__main__":
    create_database()