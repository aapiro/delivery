const { up } = require('../../migrations/01-create-restaurant-tables');

async function runMigrations() {
    try {
        console.log('Running database migrations...');
        await up();
        console.log('Database migrations completed successfully');
    } catch (error) {
        console.error('Error running migrations:', error);
        process.exit(1);
    }
}

if (require.main === module) {
    runMigrations();
}

module.exports = { runMigrations };