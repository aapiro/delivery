const express = require('express');
const router = express.Router();
const Dish = require('../models/Dish');

// GET /api/dishes - Get all dishes
router.get('/', async (req, res) => {
    try {
        const dishes = await Dish.findAll();
        res.json(dishes);
    } catch (error) {
        console.error('Error fetching dishes:', error);
        res.status(500).json({ error: 'Failed to fetch dishes' });
    }
});

// GET /api/dishes/:id - Get dish by ID
router.get('/:id', async (req, res) => {
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

// POST /api/dishes - Create new dish
router.post('/', async (req, res) => {
    try {
        const dish = await Dish.create(req.body);
        res.status(201).json(dish);
    } catch (error) {
        console.error('Error creating dish:', error);
        res.status(500).json({ error: 'Failed to create dish' });
    }
});

// PUT /api/dishes/:id - Update dish
router.put('/:id', async (req, res) => {
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

// DELETE /api/dishes/:id - Delete dish
router.delete('/:id', async (req, res) => {
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

module.exports = router;