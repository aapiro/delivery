// Integration tests for the quarkus-backend API

const request = require('supertest');
const app = require('../src/server');

describe('API Integration Tests', () => {
    test('should return health check status', async () => {
        const response = await request(app)
            .get('/health')
            .expect(200);
            
        expect(response.body.status).toBe('OK');
        expect(response.body.message).toBe('Delivery App Backend is running');
    });

    test('should get all restaurants with dishes included', async () => {
        const response = await request(app)
            .get('/api/restaurants')
            .expect(200);
            
        expect(Array.isArray(response.body)).toBe(true);
        if (response.body.length > 0) {
            const restaurant = response.body[0];
            // Check that the restaurant has all expected fields from frontend types
            expect(restaurant).toHaveProperty('id');
            expect(restaurant).toHaveProperty('name');
            expect(restaurant).toHaveProperty('description');
            expect(restaurant).toHaveProperty('logo');
            expect(restaurant).toHaveProperty('cover_image');
            expect(restaurant).toHaveProperty('address');
            expect(restaurant).toHaveProperty('phone');
            expect(restaurant).toHaveProperty('email');
            expect(restaurant).toHaveProperty('cuisine_type');
            expect(restaurant).toHaveProperty('rating');
            expect(restaurant).toHaveProperty('review_count');
            expect(restaurant).toHaveProperty('delivery_time_min');
            expect(restaurant).toHaveProperty('delivery_time_max');
            expect(restaurant).toHaveProperty('delivery_fee');
            expect(restaurant).toHaveProperty('minimum_order');
            expect(restaurant).toHaveProperty('is_open');
            expect(restaurant).toHaveProperty('is_active');
            expect(restaurant).toHaveProperty('created_at');
            expect(restaurant).toHaveProperty('updated_at');
            
            // Check that dishes are included
            if (restaurant.dishes) {
                expect(Array.isArray(restaurant.dishes)).toBe(true);
            }
        }
    });

    test('should get all categories', async () => {
        const response = await request(app)
            .get('/api/categories')
            .expect(200);
            
        expect(Array.isArray(response.body)).toBe(true);
    });

    test('should get all dishes', async () => {
        const response = await request(app)
            .get('/api/dishes')
            .expect(200);
            
        expect(Array.isArray(response.body)).toBe(true);
        if (response.body.length > 0) {
            const dish = response.body[0];
            // Check that the dish has all expected fields from frontend types
            expect(dish).toHaveProperty('id');
            expect(dish).toHaveProperty('restaurant_id');
            expect(dish).toHaveProperty('category_id');
            expect(dish).toHaveProperty('name');
            expect(dish).toHaveProperty('description');
            expect(dish).toHaveProperty('price');
            expect(dish).toHaveProperty('image_url');
            expect(dish).toHaveProperty('is_available');
            expect(dish).toHaveProperty('is_popular');
            expect(dish).toHaveProperty('preparation_time');
            expect(dish).toHaveProperty('ingredients');
            expect(dish).toHaveProperty('allergens');
            expect(dish).toHaveProperty('created_at');
            expect(dish).toHaveProperty('updated_at');
        }
    });

    test('should search restaurants', async () => {
        const response = await request(app)
            .get('/api/search/restaurants')
            .expect(200);
            
        expect(Array.isArray(response.body)).toBe(true);
    });
});