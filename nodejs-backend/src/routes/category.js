const express = require('express');
const router = express.Router();
const Category = require('../models/Category');

// GET /api/categories - Get all categories
router.get('/', async (req, res) => {
    try {
        const categories = await Category.findAll();
        res.json(categories);
    } catch (error) {
        console.error('Error fetching categories:', error);
        res.status(500).json({ error: 'Failed to fetch categories' });
    }
});

// GET /api/categories/:id - Get category by ID
router.get('/:id', async (req, res) => {
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

// POST /api/categories - Create new category
router.post('/', async (req, res) => {
    try {
        const category = await Category.create(req.body);
        res.status(201).json(category);
    } catch (error) {
        console.error('Error creating category:', error);
        res.status(500).json({ error: 'Failed to create category' });
    }
});

// PUT /api/categories/:id - Update category
router.put('/:id', async (req, res) => {
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

// DELETE /api/categories/:id - Delete category
router.delete('/:id', async (req, res) => {
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

module.exports = router;