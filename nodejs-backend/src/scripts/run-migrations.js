const fs = require('fs');
const path = require('path');

async function runMigrations() {
    try {
        console.log('Running database migrations...');
        
        // Get all migration files
        const migrationDir = path.join(__dirname, '../../migrations');
        const migrationFiles = fs.readdirSync(migrationDir)
            .filter(file => file.endsWith('.js'))
            .sort();
            
        for (const file of migrationFiles) {
            const migrationPath = path.join(migrationDir, file);
            const migration = require(migrationPath);
            
            if (typeof migration.up === 'function') {
                console.log(`Running ${file}...`);
                await migration.up();
                console.log(`${file} completed successfully`);
            }
        }
        
        console.log('All database migrations completed successfully');
    } catch (error) {
        console.error('Error running migrations:', error);
        process.exit(1);
    }
}

if (require.main === module) {
    runMigrations();
}

module.exports = { runMigrations };