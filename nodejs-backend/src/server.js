const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
require('dotenv').config();

// Initialize Express app
const app = express();
const PORT = process.env.PORT || 59806;

// Middleware
app.use(helmet());
app.use(cors({
    origin: '*',
    methods: ['OPTIONS', 'GET', 'POST', 'PUT', 'DELETE', 'PATCH'],
    allowedHeaders: ['Origin', 'X-Requested-With', 'Content-Type', 'Accept', 'Authorization']
}));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Import routes
const restaurantRoutes = require('./routes/restaurant');
const dishRoutes = require('./routes/dish');
const categoryRoutes = require('./routes/category');
const searchRoutes = require('./routes/search');
const authRoutes = require('./routes/auth');
const adminRoutes = require('./routes/admin');

// Routes
app.use('/api/restaurants', restaurantRoutes);
app.use('/api/dishes', dishRoutes);
app.use('/api/categories', categoryRoutes);
app.use('/api/search', searchRoutes);
app.use('/api/auth', authRoutes);
app.use('/api/admin', adminRoutes);

// Health check endpoint
app.get('/health', (req, res) => {
    res.status(200).json({ status: 'OK', message: 'Delivery App Backend is running' });
});

// 404 handler
app.use('*', (req, res) => {
    res.status(404).json({ error: 'Route not found' });
});

// Error handling middleware
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({ error: 'Something went wrong!' });
});

// Start server
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});

module.exports = app;