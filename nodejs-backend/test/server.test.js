const request = require('supertest');
const app = require('../src/server');

describe('Server Tests', () => {
    test('should return health check response', async () => {
        const res = await request(app)
            .get('/health')
            .expect(200);
        
        expect(res.body).toHaveProperty('status', 'OK');
        expect(res.body).toHaveProperty('message', 'Delivery App Backend is running');
    });

    test('should return 404 for non-existent route', async () => {
        const res = await request(app)
            .get('/non-existent')
            .expect(404);
        
        expect(res.body).toHaveProperty('error', 'Route not found');
    });
});