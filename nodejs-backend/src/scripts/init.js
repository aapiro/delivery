#!/usr/bin/env node

const { runMigrations } = require('./run-migrations');

async function init() {
    console.log('Initializing Delivery App Backend...');
    
    try {
        await runMigrations();
        console.log('✅ Initialization completed successfully!');
        process.exit(0);
    } catch (error) {
        console.error('❌ Initialization failed:', error);
        process.exit(1);
    }
}

init();