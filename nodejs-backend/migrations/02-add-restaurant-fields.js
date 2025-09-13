const { Sequelize } = require('sequelize');
const sequelize = require('../src/config/database');

// Migration to add missing fields to restaurants and dishes tables

async function up() {
    try {
        // Add missing columns to restaurants table one by one (SQLite limitation)
        await sequelize.query(`
            ALTER TABLE restaurants 
            ADD COLUMN logo VARCHAR(512)
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            ADD COLUMN cover_image VARCHAR(512)
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            ADD COLUMN address TEXT
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            ADD COLUMN phone VARCHAR(20)
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            ADD COLUMN email VARCHAR(255)
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            ADD COLUMN category_id INTEGER
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        `);

        // Add missing columns to dishes table one by one (SQLite limitation)
        await sequelize.query(`
            ALTER TABLE dishes 
            ADD COLUMN image_url VARCHAR(512)
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            ADD COLUMN original_price DECIMAL(6,2)
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            ADD COLUMN preparation_time INTEGER
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            ADD COLUMN ingredients TEXT
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            ADD COLUMN allergens TEXT
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            ADD COLUMN is_popular BOOLEAN DEFAULT FALSE
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            ADD COLUMN category_id INTEGER
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        `);

        // Add categories table if it doesn't exist
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

        // Add restaurant_categories junction table if it doesn't exist
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
        // Remove added columns from restaurants table one by one
        await sequelize.query(`
            ALTER TABLE restaurants 
            DROP COLUMN logo
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            DROP COLUMN cover_image
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            DROP COLUMN address
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            DROP COLUMN phone
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            DROP COLUMN email
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            DROP COLUMN category_id
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            DROP COLUMN created_at
        `);

        await sequelize.query(`
            ALTER TABLE restaurants 
            DROP COLUMN updated_at
        `);

        // Remove added columns from dishes table one by one  
        await sequelize.query(`
            ALTER TABLE dishes 
            DROP COLUMN image_url
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            DROP COLUMN original_price
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            DROP COLUMN preparation_time
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            DROP COLUMN ingredients
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            DROP COLUMN allergens
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            DROP COLUMN is_popular
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            DROP COLUMN category_id
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            DROP COLUMN created_at
        `);

        await sequelize.query(`
            ALTER TABLE dishes 
            DROP COLUMN updated_at
        `);

        // Drop categories table (if it was added)
        await sequelize.query('DROP TABLE IF EXISTS categories');
        
        // Drop restaurant_categories table (if it was added)  
        await sequelize.query('DROP TABLE IF EXISTS restaurant_categories');

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