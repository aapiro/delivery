const express = require('express');
const router = express.Router();
const Restaurant = require('../models/Restaurant');
const Dish = require('../models/Dish');

// GET /api/search/restaurants - Search restaurants by name or description
router.get('/restaurants', async (req, res) => {
    try {
        const { q, category, minRating, isOpen } = req.query;
        
        let whereClause = {};
        
        // Add search term if provided
        if (q) {
            whereClause.$or = [
                { name: { $like: `%${q}%` } },
                { description: { $like: `%${q}%` } }
            ];
        }
        
        // Add category filter if provided
        if (category) {
            whereClause.category_id = category;
        }
        
        // Add rating filter if provided
        if (minRating) {
            whereClause.rating = { $gte: parseFloat(minRating) };
        }
        
        // Add open status filter if provided
        if (isOpen !== undefined) {
            whereClause.is_open = isOpen === 'true';
        }

        const restaurants = await Restaurant.findAll({
            where: whereClause,
            include: [{
                model: Dish,
                as: 'dishes',
                attributes: ['id', 'name', 'description', 'price', 'image_url', 'is_available']
            }]
        });
        
        res.json(restaurants);
    } catch (error) {
        console.error('Error searching restaurants:', error);
        res.status(500).json({ error: 'Failed to search restaurants' });
    }
});

// GET /api/search/dishes - Search dishes by name or description
router.get('/dishes', async (req, res) => {
    try {
        const { q, restaurantId, categoryId } = req.query;
        
        let whereClause = {};
        
        // Add search term if provided
        if (q) {
            whereClause.$or = [
                { name: { $like: `%${q}%` } },
                { description: { $like: `%${q}%` } }
            ];
        }
        
        // Add restaurant filter if provided
        if (restaurantId) {
            whereClause.restaurant_id = parseInt(restaurantId);
        }
        
        // Add category filter if provided
        if (categoryId) {
            whereClause.category_id = categoryId;
        }

        const dishes = await Dish.findAll({
            where: whereClause
        });
        
        res.json(dishes);
    } catch (error) {
        console.error('Error searching dishes:', error);
        res.status(500).json({ error: 'Failed to search dishes' });
    }
});

module.exports = router;