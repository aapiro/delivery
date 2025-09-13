const express = require('express');
const router = express.Router();
const Restaurant = require('../models/Restaurant');
const Dish = require('../models/Dish');

// GET /api/restaurants - Get all restaurants
router.get('/', async (req, res) => {
    try {
        const restaurants = await Restaurant.findAll({
            include: [{
                model: Dish,
                as: 'dishes',
                attributes: ['id', 'name', 'description', 'price', 'image_url', 'is_available']
            }]
        });
        res.json(restaurants);
    } catch (error) {
        console.error('Error fetching restaurants:', error);
        res.status(500).json({ error: 'Failed to fetch restaurants' });
    }
});

// GET /api/restaurants/:id - Get restaurant by ID
router.get('/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const restaurant = await Restaurant.findByPk(id, {
            include: [{
                model: Dish,
                as: 'dishes',
                attributes: ['id', 'name', 'description', 'price', 'image_url', 'is_available']
            }]
        });
        
        if (!restaurant) {
            return res.status(404).json({ error: 'Restaurant not found' });
        }
        
        res.json(restaurant);
    } catch (error) {
        console.error('Error fetching restaurant:', error);
        res.status(500).json({ error: 'Failed to fetch restaurant' });
    }
});

// POST /api/restaurants - Create new restaurant
router.post('/', async (req, res) => {
    try {
        const restaurant = await Restaurant.create(req.body);
        res.status(201).json(restaurant);
    } catch (error) {
        console.error('Error creating restaurant:', error);
        res.status(500).json({ error: 'Failed to create restaurant' });
    }
});

// PUT /api/restaurants/:id - Update restaurant
router.put('/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const [updated] = await Restaurant.update(req.body, {
            where: { id: id }
        });

        if (!updated) {
            return res.status(404).json({ error: 'Restaurant not found' });
        }

        const updatedRestaurant = await Restaurant.findByPk(id);
        res.json(updatedRestaurant);
    } catch (error) {
        console.error('Error updating restaurant:', error);
        res.status(500).json({ error: 'Failed to update restaurant' });
    }
});

// DELETE /api/restaurants/:id - Delete restaurant
router.delete('/:id', async (req, res) => {
    try {
        const id = parseInt(req.params.id);
        const deleted = await Restaurant.destroy({
            where: { id: id }
        });

        if (!deleted) {
            return res.status(404).json({ error: 'Restaurant not found' });
        }

        res.json({ message: 'Restaurant deleted successfully' });
    } catch (error) {
        console.error('Error deleting restaurant:', error);
        res.status(500).json({ error: 'Failed to delete restaurant' });
    }
});

module.exports = router;