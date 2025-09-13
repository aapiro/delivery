const { Sequelize } = require('sequelize');
const sequelize = require('../src/config/database');

// Complete migration that creates all tables with proper schema

async function up() {
    try {
        // Drop existing tables first (for development purposes)
        await sequelize.query('DROP TABLE IF EXISTS restaurant_categories');
        await sequelize.query('DROP TABLE IF EXISTS categories'); 
        await sequelize.query('DROP TABLE IF EXISTS dishes');
        await sequelize.query('DROP TABLE IF EXISTS restaurants');

        // Create restaurants table with all fields
        await sequelize.query(`
            CREATE TABLE IF NOT EXISTS restaurants (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name VARCHAR(255) NOT NULL,
                description TEXT,
                logo VARCHAR(512),
                cover_image VARCHAR(512),
                address TEXT,
                phone VARCHAR(20),
                email VARCHAR(255),
                cuisine_type VARCHAR(100),
                rating DECIMAL(3,1) DEFAULT 0.0,
                review_count INTEGER DEFAULT 0,
                delivery_time_min INTEGER,
                delivery_time_max INTEGER,
                delivery_fee DECIMAL(5,2) DEFAULT 0.00,
                minimum_order DECIMAL(6,2) DEFAULT 0.00,
                is_open BOOLEAN DEFAULT TRUE,
                is_active BOOLEAN DEFAULT TRUE,
                category_id INTEGER,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        `);

        // Create dishes table with all fields
        await sequelize.query(`
            CREATE TABLE IF NOT EXISTS dishes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                restaurant_id INTEGER NOT NULL,
                category_id INTEGER,
                name VARCHAR(255) NOT NULL,
                description TEXT,
                price DECIMAL(6,2) DEFAULT 0.00,
                image_url VARCHAR(512),
                original_price DECIMAL(6,2),
                is_available BOOLEAN DEFAULT TRUE,
                is_popular BOOLEAN DEFAULT FALSE,
                preparation_time INTEGER,
                ingredients TEXT,
                allergens TEXT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        `);

        // Create categories table
        await sequelize.query(`
            CREATE TABLE IF NOT EXISTS categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name VARCHAR(100) NOT NULL,
                slug VARCHAR(100),
                icon VARCHAR(255),
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        `);

        // Create restaurant_categories junction table
        await sequelize.query(`
            CREATE TABLE IF NOT EXISTS restaurant_categories (
                restaurant_id INTEGER,
                category_id INTEGER,
                FOREIGN KEY (restaurant_id) REFERENCES restaurants(id),
                FOREIGN KEY (category_id) REFERENCES categories(id)
            )
        `);

        console.log('Complete migration completed successfully');
    } catch (error) {
        console.error('Error running complete migration:', error);
        throw error;
    }
}

// Down function to rollback the migration
async function down() {
    try {
        await sequelize.query('DROP TABLE IF EXISTS restaurant_categories');
        await sequelize.query('DROP TABLE IF EXISTS categories'); 
        await sequelize.query('DROP TABLE IF EXISTS dishes');
        await sequelize.query('DROP TABLE IF EXISTS restaurants');
        console.log('Complete migration rolled back successfully');
    } catch (error) {
        console.error('Error rolling back complete migration:', error);
        throw error;
    }
}

// Run the up function if this file is executed directly
if (require.main === module) {
    up().catch(console.error);
}

module.exports = { up, down };