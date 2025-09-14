const express = require('express');
const router = express.Router();

// Mock admin data (in a real app, this would be in database)
let admins = [];

// POST /api/admin/auth/login - Admin login
router.post('/auth/login', async (req, res) => {
    try {
        const { email, password } = req.body;
        
        // In a real app, you would verify credentials against database
        // For now, we'll use mock data
        
        if (email === 'admin@deliveryapp.com' && password === 'admin123') {
            res.json({
                admin: { 
                    id: 1, 
                    name: 'Admin User', 
                    email: email,
                    role: 'SUPER_ADMIN',
                    permissions: ['VIEW_RESTAURANTS', 'CREATE_RESTAURANTS', 'EDIT_RESTAURANTS', 'DELETE_RESTAURANTS']
                },
                token: 'admin-mock-jwt-token-1',
                refreshToken: 'admin-mock-refresh-token-1'
            });
        } else {
            res.status(401).json({ error: 'Invalid admin credentials' });
        }
    } catch (error) {
        console.error('Error logging in admin:', error);
        res.status(500).json({ error: 'Failed to login admin' });
    }
});

// GET /api/admin/dashboard - Get dashboard stats
router.get('/dashboard', async (req, res) => {
    try {
        // Mock data for dashboard statistics
        const stats = {
            totalRestaurants: 15,
            activeRestaurants: 12,
            totalOrders: 420,
            todayOrders: 32,
            totalUsers: 890,
            activeUsers: 670,
            totalRevenue: 12500.50,
            todayRevenue: 890.25,
            averageOrderValue: 29.76,
            popularRestaurants: [
                { 
                    restaurant: { id: 1, name: 'Pizza Palace', rating: 4.8 },
                    orderCount: 45,
                    revenue: 2300.50
                }
            ],
            recentOrders: [
                {
                    id: 101,
                    userId: 123,
                    restaurantId: 1,
                    status: 'CONFIRMED',
                    total: 29.99,
                    createdAt: new Date().toISOString()
                }
            ]
        };
        
        res.json(stats);
    } catch (error) {
        console.error('Error fetching dashboard stats:', error);
        res.status(500).json({ error: 'Failed to fetch dashboard stats' });
    }
});

module.exports = router;