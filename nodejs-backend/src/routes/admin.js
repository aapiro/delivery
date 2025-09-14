const express = require('express');
const router = express.Router();
const Restaurant = require('../models/Restaurant');
const Dish = require('../models/Dish');
const Category = require('../models/Category');
const Order = require('../models/Order');

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
        // Mock data for dashboard statistics (in a real app this would be from database queries)
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

// GET /api/admin/restaurants - Get all restaurants with pagination
router.get('/restaurants', async (req, res) => {
    try {
        const page = parseInt(req.query.page) || 1;
        const limit = parseInt(req.query.limit) || 20;
        const offset = (page - 1) * limit;

        const { count, rows: restaurants } = await Restaurant.findAndCountAll({
            limit,
            offset,
            order: [['created_at', 'DESC']]
        });

        const totalPages = Math.ceil(count / limit);
        
        res.json({
            success: true,
            data: {
                data: restaurants,
                pagination: {
                    page,
                    limit,
                    total: count,
                    totalPages,
                    hasNext: page < totalPages,
                    hasPrev: page > 1
                }
            },
            message: 'Restaurants fetched successfully'
        });
    } catch (error) {
        console.error('Error fetching restaurants:', error);
        res.status(500).json({
            success: false,
            data: null,
            errors: ['Failed to fetch restaurants']
        });
    }
});

// GET /api/admin/restaurants/:id - Get restaurant by ID
router.get('/restaurants/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const restaurant = await Restaurant.findByPk(id);
        
        if (!restaurant) {
            return res.status(404).json({
                success: false,
                data: null,
                errors: ['Restaurant not found']
            });
        }
        
        res.json({
            success: true,
            data: restaurant,
            message: 'Restaurant fetched successfully'
        });
    } catch (error) {
        console.error('Error fetching restaurant:', error);
        res.status(500).json({
            success: false,
            data: null,
            errors: ['Failed to fetch restaurant']
        });
    }
});

// POST /api/admin/restaurants - Create new restaurant
router.post('/restaurants', async (req, res) => {
    try {
        const restaurant = await Restaurant.create(req.body);
        res.status(201).json({
            success: true,
            data: restaurant,
            message: 'Restaurant created successfully'
        });
    } catch (error) {
        console.error('Error creating restaurant:', error);
        res.status(500).json({
            success: false,
            data: null,
            errors: ['Failed to create restaurant']
        });
    }
});

// PUT /api/admin/restaurants/:id - Update restaurant
router.put('/restaurants/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const [updated] = await Restaurant.update(req.body, {
            where: { id: id }
        });

        if (!updated) {
            return res.status(404).json({
                success: false,
                data: null,
                errors: ['Restaurant not found']
            });
        }

        const updatedRestaurant = await Restaurant.findByPk(id);
        res.json({
            success: true,
            data: updatedRestaurant,
            message: 'Restaurant updated successfully'
        });
    } catch (error) {
        console.error('Error updating restaurant:', error);
        res.status(500).json({
            success: false,
            data: null,
            errors: ['Failed to update restaurant']
        });
    }
});

// DELETE /api/admin/restaurants/:id - Delete restaurant
router.delete('/restaurants/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const deleted = await Restaurant.destroy({
            where: { id: id }
        });

        if (!deleted) {
            return res.status(404).json({
                success: false,
                data: null,
                errors: ['Restaurant not found']
            });
        }

        res.json({
            success: true,
            data: { message: 'Restaurant deleted successfully' },
            message: 'Restaurant deleted successfully'
        });
    } catch (error) {
        console.error('Error deleting restaurant:', error);
        res.status(500).json({
            success: false,
            data: null,
            errors: ['Failed to delete restaurant']
        });
    }
});

// PATCH /api/admin/restaurants/:id/toggle-status - Toggle restaurant status
router.patch('/restaurants/:id/toggle-status', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const restaurant = await Restaurant.findByPk(id);

        if (!restaurant) {
            return res.status(404).json({
                success: false,
                data: null,
                errors: ['Restaurant not found']
            });
        }

        // Toggle the is_active status
        const updatedRestaurant = await restaurant.update({
            is_active: !restaurant.is_active
        });

        res.json({
            success: true,
            data: updatedRestaurant,
            message: 'Restaurant status toggled successfully'
        });
    } catch (error) {
        console.error('Error toggling restaurant status:', error);
        res.status(500).json({
            success: false,
            data: null,
            errors: ['Failed to toggle restaurant status']
        });
    }
});

// GET /api/admin/dishes - Get all dishes with pagination and filters
router.get('/dishes', async (req, res) => {
    try {
        const page = parseInt(req.query.page) || 1;
        const limit = parseInt(req.query.limit) || 20;
        const offset = (page - 1) * limit;

        // Build where clause based on filters
        let whereClause = {};
        
        if (req.query.restaurantId) {
            whereClause.restaurant_id = req.query.restaurantId;
        }
        
        if (req.query.search) {
            whereClause.name = { [require('sequelize').Op.like]: `%${req.query.search}%` };
        }

        const { count, rows: dishes } = await Dish.findAndCountAll({
            where: whereClause,
            limit,
            offset,
            order: [['created_at', 'DESC']]
        });

        const totalPages = Math.ceil(count / limit);
        
        res.json({
            data: dishes,
            pagination: {
                page,
                limit,
                total: count,
                totalPages,
                hasNext: page < totalPages,
                hasPrev: page > 1
            }
        });
    } catch (error) {
        console.error('Error fetching dishes:', error);
        res.status(500).json({ error: 'Failed to fetch dishes' });
    }
});

// GET /api/admin/dishes/:id - Get dish by ID
router.get('/dishes/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const dish = await Dish.findByPk(id);
        
        if (!dish) {
            return res.status(404).json({ error: 'Dish not found' });
        }
        
        res.json(dish);
    } catch (error) {
        console.error('Error fetching dish:', error);
        res.status(500).json({ error: 'Failed to fetch dish' });
    }
});

// POST /api/admin/dishes - Create new dish
router.post('/dishes', async (req, res) => {
    try {
        const dish = await Dish.create(req.body);
        res.status(201).json(dish);
    } catch (error) {
        console.error('Error creating dish:', error);
        res.status(500).json({ error: 'Failed to create dish' });
    }
});

// PUT /api/admin/dishes/:id - Update dish
router.put('/dishes/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const [updated] = await Dish.update(req.body, {
            where: { id: id }
        });

        if (!updated) {
            return res.status(404).json({ error: 'Dish not found' });
        }

        const updatedDish = await Dish.findByPk(id);
        res.json(updatedDish);
    } catch (error) {
        console.error('Error updating dish:', error);
        res.status(500).json({ error: 'Failed to update dish' });
    }
});

// DELETE /api/admin/dishes/:id - Delete dish
router.delete('/dishes/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const deleted = await Dish.destroy({
            where: { id: id }
        });

        if (!deleted) {
            return res.status(404).json({ error: 'Dish not found' });
        }

        res.json({ message: 'Dish deleted successfully' });
    } catch (error) {
        console.error('Error deleting dish:', error);
        res.status(500).json({ error: 'Failed to delete dish' });
    }
});

// PATCH /api/admin/dishes/:id/toggle-availability - Toggle dish availability
router.patch('/dishes/:id/toggle-availability', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const dish = await Dish.findByPk(id);

        if (!dish) {
            return res.status(404).json({ error: 'Dish not found' });
        }

        // Toggle the is_available status
        const updatedDish = await dish.update({
            is_available: !dish.is_available
        });

        res.json(updatedDish);
    } catch (error) {
        console.error('Error toggling dish availability:', error);
        res.status(500).json({ error: 'Failed to toggle dish availability' });
    }
});

// GET /api/admin/categories - Get all categories with pagination and filters
router.get('/categories', async (req, res) => {
    try {
        const page = parseInt(req.query.page) || 1;
        const limit = parseInt(req.query.limit) || 20;
        const offset = (page - 1) * limit;

        // Build where clause based on filters
        let whereClause = {};
        
        if (req.query.search) {
            whereClause.name = { [require('sequelize').Op.like]: `%${req.query.search}%` };
        }

        const { count, rows: categories } = await Category.findAndCountAll({
            where: whereClause,
            limit,
            offset,
            order: [['created_at', 'DESC']]
        });

        const totalPages = Math.ceil(count / limit);
        
        res.json({
            data: categories,
            pagination: {
                page,
                limit,
                total: count,
                totalPages,
                hasNext: page < totalPages,
                hasPrev: page > 1
            }
        });
    } catch (error) {
        console.error('Error fetching categories:', error);
        res.status(500).json({ error: 'Failed to fetch categories' });
    }
});

// GET /api/admin/categories/:id - Get category by ID
router.get('/categories/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const category = await Category.findByPk(id);
        
        if (!category) {
            return res.status(404).json({ error: 'Category not found' });
        }
        
        res.json(category);
    } catch (error) {
        console.error('Error fetching category:', error);
        res.status(500).json({ error: 'Failed to fetch category' });
    }
});

// POST /api/admin/categories - Create new category
router.post('/categories', async (req, res) => {
    try {
        const category = await Category.create(req.body);
        res.status(201).json(category);
    } catch (error) {
        console.error('Error creating category:', error);
        res.status(500).json({ error: 'Failed to create category' });
    }
});

// PUT /api/admin/categories/:id - Update category
router.put('/categories/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const [updated] = await Category.update(req.body, {
            where: { id: id }
        });

        if (!updated) {
            return res.status(404).json({ error: 'Category not found' });
        }

        const updatedCategory = await Category.findByPk(id);
        res.json(updatedCategory);
    } catch (error) {
        console.error('Error updating category:', error);
        res.status(500).json({ error: 'Failed to update category' });
    }
});

// DELETE /api/admin/categories/:id - Delete category
router.delete('/categories/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const deleted = await Category.destroy({
            where: { id: id }
        });

        if (!deleted) {
            return res.status(404).json({ error: 'Category not found' });
        }

        res.json({ message: 'Category deleted successfully' });
    } catch (error) {
        console.error('Error deleting category:', error);
        res.status(500).json({ error: 'Failed to delete category' });
    }
});

// PATCH /api/admin/categories/:id/toggle-status - Toggle category status
router.patch('/categories/:id/toggle-status', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const category = await Category.findByPk(id);

        if (!category) {
            return res.status(404).json({ error: 'Category not found' });
        }

        // Toggle the status (in a real app you might have an is_active field)
        // For now we'll just return it as toggled
        const updatedCategory = await category.update({
            name: category.name // This would be your actual toggle logic
        });

        res.json(updatedCategory);
    } catch (error) {
        console.error('Error toggling category status:', error);
        res.status(500).json({ error: 'Failed to toggle category status' });
    }
});

// GET /api/admin/orders - Get all orders with pagination and filters
router.get('/orders', async (req, res) => {
    try {
        const page = parseInt(req.query.page) || 1;
        const limit = parseInt(req.query.limit) || 20;
        const offset = (page - 1) * limit;

        // Build where clause based on filters
        let whereClause = {};
        
        if (req.query.status) {
            whereClause.status = req.query.status;
        }
        
        if (req.query.restaurantId) {
            whereClause.restaurant_id = req.query.restaurantId;
        }

        const { count, rows: orders } = await Order.findAndCountAll({
            where: whereClause,
            limit,
            offset,
            order: [['created_at', 'DESC']]
        });

        const totalPages = Math.ceil(count / limit);
        
        res.json({
            data: orders,
            pagination: {
                page,
                limit,
                total: count,
                totalPages,
                hasNext: page < totalPages,
                hasPrev: page > 1
            }
        });
    } catch (error) {
        console.error('Error fetching orders:', error);
        res.status(500).json({ error: 'Failed to fetch orders' });
    }
});

// GET /api/admin/orders/:id - Get order by ID
router.get('/orders/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const order = await Order.findByPk(id);
        
        if (!order) {
            return res.status(404).json({ error: 'Order not found' });
        }
        
        res.json(order);
    } catch (error) {
        console.error('Error fetching order:', error);
        res.status(500).json({ error: 'Failed to fetch order' });
    }
});

// PATCH /api/admin/orders/:id/status - Update order status
router.patch('/orders/:id/status', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const { status } = req.body;
        
        if (!status) {
            return res.status(400).json({ error: 'Status is required' });
        }

        const order = await Order.findByPk(id);

        if (!order) {
            return res.status(404).json({ error: 'Order not found' });
        }

        const updatedOrder = await order.update({
            status
        });

        res.json(updatedOrder);
    } catch (error) {
        console.error('Error updating order status:', error);
        res.status(500).json({ error: 'Failed to update order status' });
    }
});

// PATCH /api/admin/orders/:id/cancel - Cancel an order
router.patch('/orders/:id/cancel', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const { reason } = req.body;
        
        const order = await Order.findByPk(id);

        if (!order) {
            return res.status(404).json({ error: 'Order not found' });
        }

        // Cancel the order
        const updatedOrder = await order.update({
            status: 'cancelled',
            payment_status: 'refunded'
        });

        res.json(updatedOrder);
    } catch (error) {
        console.error('Error cancelling order:', error);
        res.status(500).json({ error: 'Failed to cancel order' });
    }
});

// PATCH /api/admin/orders/:id/refund - Refund an order
router.patch('/orders/:id/refund', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const { amount, reason } = req.body;
        
        const order = await Order.findByPk(id);

        if (!order) {
            return res.status(404).json({ error: 'Order not found' });
        }

        // Process refund
        const updatedOrder = await order.update({
            payment_status: 'refunded'
        });

        res.json(updatedOrder);
    } catch (error) {
        console.error('Error processing refund:', error);
        res.status(500).json({ error: 'Failed to process refund' });
    }
});

// GET /api/admin/users - Get all users with pagination and filters
router.get('/users', async (req, res) => {
    try {
        // In a real app this would be from the database
        // For now we'll return mock data
        
        const page = parseInt(req.query.page) || 1;
        const limit = parseInt(req.query.limit) || 20;
        const offset = (page - 1) * limit;

        // Mock users data
        const mockUsers = [
            { id: 1, name: 'John Doe', email: 'john@example.com', role: 'USER', is_active: true, created_at: new Date() },
            { id: 2, name: 'Jane Smith', email: 'jane@example.com', role: 'USER', is_active: true, created_at: new Date() },
            { id: 3, name: 'Bob Johnson', email: 'bob@example.com', role: 'USER', is_active: false, created_at: new Date() }
        ];

        const total = mockUsers.length;
        
        res.json({
            data: mockUsers.slice(offset, offset + limit),
            pagination: {
                page,
                limit,
                total,
                pages: Math.ceil(total / limit)
            }
        });
    } catch (error) {
        console.error('Error fetching users:', error);
        res.status(500).json({ error: 'Failed to fetch users' });
    }
});

// GET /api/admin/users/:id - Get user by ID
router.get('/users/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        
        // Mock user data
        const mockUser = { 
            id: 1, 
            name: 'John Doe', 
            email: 'john@example.com',
            role: 'USER',
            is_active: true,
            created_at: new Date(),
            orders_count: 5,
            total_spent: 120.50
        };
        
        if (id !== 1) {
            return res.status(404).json({ error: 'User not found' });
        }
        
        res.json(mockUser);
    } catch (error) {
        console.error('Error fetching user:', error);
        res.status(500).json({ error: 'Failed to fetch user' });
    }
});

// PUT /api/admin/users/:id - Update user
router.put('/users/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        
        // Mock update response
        const updatedUser = { 
            ...req.body,
            id: id,
            created_at: new Date()
        };
        
        if (id !== 1) {
            return res.status(404).json({ error: 'User not found' });
        }
        
        res.json(updatedUser);
    } catch (error) {
        console.error('Error updating user:', error);
        res.status(500).json({ error: 'Failed to update user' });
    }
});

// PATCH /api/admin/users/:id/toggle-status - Toggle user status
router.patch('/users/:id/toggle-status', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        
        // Mock toggle response
        if (id !== 1) {
            return res.status(404).json({ error: 'User not found' });
        }
        
        res.json({
            id: id,
            is_active: true, // This would be toggled in a real implementation
            message: 'User status updated successfully'
        });
    } catch (error) {
        console.error('Error toggling user status:', error);
        res.status(500).json({ error: 'Failed to toggle user status' });
    }
});

// DELETE /api/admin/users/:id - Delete user
router.delete('/users/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        
        if (id !== 1) {
            return res.status(404).json({ error: 'User not found' });
        }
        
        res.json({ message: 'User deleted successfully' });
    } catch (error) {
        console.error('Error deleting user:', error);
        res.status(500).json({ error: 'Failed to delete user' });
    }
});

// GET /api/admin/reports/sales - Get sales report
router.get('/reports/sales', async (req, res) => {
    try {
        // Mock sales data for reports
        const salesReport = {
            period: req.query.period || 'week',
            total_orders: 120,
            total_revenue: 3450.75,
            average_order_value: 28.76,
            orders_by_status: [
                { status: 'pending', count: 15, percentage: 12.5 },
                { status: 'confirmed', count: 35, percentage: 29.2 },
                { status: 'preparing', count: 20, percentage: 16.7 },
                { status: 'on_the_way', count: 25, percentage: 20.8 },
                { status: 'delivered', count: 25, percentage: 20.8 }
            ],
            daily_revenue: [
                { date: '2023-10-01', revenue: 250.50 },
                { date: '2023-10-02', revenue: 320.75 },
                { date: '2023-10-03', revenue: 410.25 }
            ]
        };
        
        res.json(salesReport);
    } catch (error) {
        console.error('Error fetching sales report:', error);
        res.status(500).json({ error: 'Failed to fetch sales report' });
    }
});

// GET /api/admin/reports/users - Get users report
router.get('/reports/users', async (req, res) => {
    try {
        // Mock user data for reports
        const usersReport = {
            total_users: 890,
            active_users: 670,
            new_users_this_week: 45,
            user_growth_rate: 3.2,
            top_users_by_spending: [
                { id: 1, name: 'John Doe', email: 'john@example.com', total_spent: 120.50 },
                { id: 2, name: 'Jane Smith', email: 'jane@example.com', total_spent: 98.75 }
            ]
        };
        
        res.json(usersReport);
    } catch (error) {
        console.error('Error fetching users report:', error);
        res.status(500).json({ error: 'Failed to fetch users report' });
    }
});

// GET /api/admin/system/admins - Get all admins
router.get('/system/admins', async (req, res) => {
    try {
        // Mock admin data
        const mockAdmins = [
            { id: 1, name: 'Admin User', email: 'admin@deliveryapp.com', role: 'SUPER_ADMIN' },
            { id: 2, name: 'Manager User', email: 'manager@deliveryapp.com', role: 'MANAGER' }
        ];
        
        res.json(mockAdmins);
    } catch (error) {
        console.error('Error fetching admins:', error);
        res.status(500).json({ error: 'Failed to fetch admins' });
    }
});

// GET /api/admin/system/settings - Get system settings
router.get('/system/settings', async (req, res) => {
    try {
        // Mock system settings
        const settings = {
            app_name: 'DeliveryApp',
            version: '1.0.0',
            maintenance_mode: false,
            email_notifications_enabled: true,
            sms_notifications_enabled: false,
            default_delivery_fee: 2.99,
            minimum_order_amount: 10.00
        };
        
        res.json(settings);
    } catch (error) {
        console.error('Error fetching settings:', error);
        res.status(500).json({ error: 'Failed to fetch settings' });
    }
});

module.exports = router;