#!/usr/bin/env python3

import sqlite3
from pathlib import Path

def verify_database():
    """Verificar que la base de datos y tablas existen"""
    
    db_path = Path('database.sqlite')
    
    if not db_path.exists():
        print("ERROR: La base de datos no existe")
        return False
    
    try:
        conn = sqlite3.connect('database.sqlite')
        cursor = conn.cursor()
        
        # Verificar las tablas usando una consulta SQL
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table';")
        tables = cursor.fetchall()
        
        print("Tablas en la base de datos:")
        for table in tables:
            print(f"  - {table[0]}")
            
        # Verificar usuarios
        cursor.execute("SELECT COUNT(*) FROM users")
        user_count = cursor.fetchone()[0]
        print(f"Usuarios: {user_count}")
        
        # Verificar categorías
        cursor.execute("SELECT COUNT(*) FROM categories")
        category_count = cursor.fetchone()[0]
        print(f"Categorías: {category_count}")
        
        conn.close()
        return True
        
    except Exception as e:
        print(f"Error al verificar la base de datos: {e}")
        return False

if __name__ == "__main__":
    success = verify_database()
    if success:
        print("✓ Base de datos verificada correctamente")
    else:
        print("✗ Error en la verificación de la base de datos")