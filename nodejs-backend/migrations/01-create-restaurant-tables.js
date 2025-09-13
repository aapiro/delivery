const { Sequelize } = require('sequelize');
const sequelize = require('../src/config/database');

// Migration to create tables similar to the Liquibase schema

async function up() {
    try {
        // Create restaurants table
        await sequelize.query(`
            CREATE TABLE IF NOT EXISTS restaurants (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name VARCHAR(255) NOT NULL,
                description TEXT,
                cuisine_type VARCHAR(100),
                rating DECIMAL(3,1) DEFAULT 0.0,
                review_count INTEGER DEFAULT 0,
                delivery_time_min INTEGER,
                delivery_time_max INTEGER,
                delivery_fee DECIMAL(5,2) DEFAULT 0.00,
                minimum_order DECIMAL(6,2) DEFAULT 0.00,
                is_open BOOLEAN DEFAULT TRUE,
                image_url VARCHAR(512)
            )
        `);

        // Create dishes table
        await sequelize.query(`
            CREATE TABLE IF NOT EXISTS dishes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                restaurant_id INTEGER NOT NULL,
                name VARCHAR(255) NOT NULL,
                description TEXT,
                price DECIMAL(6,2) DEFAULT 0.00,
                image_url VARCHAR(512),
                is_available BOOLEAN DEFAULT TRUE
            )
        `);

        // Create categories table (if needed)
        await sequelize.query(`
            CREATE TABLE IF NOT EXISTS categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name VARCHAR(100) NOT NULL
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

        console.log('Migration completed successfully');
    } catch (error) {
        console.error('Error running migration:', error);
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
        console.log('Migration rolled back successfully');
    } catch (error) {
        console.error('Error rolling back migration:', error);
        throw error;
    }
}

// Run the up function if this file is executed directly
if (require.main === module) {
    up().catch(console.error);
}

module.exports = { up, down };