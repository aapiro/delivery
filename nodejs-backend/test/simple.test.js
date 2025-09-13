// Simple tests without starting the full server

describe('Basic Setup Tests', () => {
    test('should have required files and directories', () => {
        const fs = require('fs');
        const path = require('path');
        
        // Check main directories exist
        expect(fs.existsSync('./src')).toBe(true);
        expect(fs.existsSync('./migrations')).toBe(true);
        expect(fs.existsSync('./test')).toBe(true);
        
        // Check key files exist
        expect(fs.existsSync('./src/server.js')).toBe(true);
        expect(fs.existsSync('./src/models/Restaurant.js')).toBe(true);
        expect(fs.existsSync('./src/models/Dish.js')).toBe(true);
        expect(fs.existsSync('./src/routes/restaurant.js')).toBe(true);
        expect(fs.existsSync('./src/routes/dish.js')).toBe(true);
        expect(fs.existsSync('./migrations/01-create-restaurant-tables.js')).toBe(true);
    });
    
    test('should have valid package.json', () => {
        const fs = require('fs');
        const packageJson = JSON.parse(fs.readFileSync('./package.json', 'utf8'));
        
        expect(packageJson.name).toBe('delivery-app-backend');
        expect(packageJson.version).toBe('1.0.0');
        expect(packageJson.dependencies).toBeDefined();
        expect(packageJson.devDependencies).toBeDefined();
    });
});