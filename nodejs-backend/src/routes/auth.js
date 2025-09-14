const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const User = require('../models/User');

// POST /api/auth/register - Register new user
router.post('/register', async (req, res) => {
    try {
        const { name, email, password } = req.body;
        
        // Check if user already exists
        const existingUser = await User.findOne({ where: { email } });
        if (existingUser) {
            return res.status(400).json({ error: 'User already exists' });
        }
        
        // Hash password
        const hashedPassword = await bcrypt.hash(password, 10);
        
        // Create new user
        const newUser = await User.create({
            name,
            email,
            password: hashedPassword
        });
        
        res.status(201).json({
            user: { id: newUser.id, name: newUser.name, email: newUser.email },
            token: 'jwt-token-' + newUser.id,
            refreshToken: 'refresh-token-' + newUser.id
        });
    } catch (error) {
        console.error('Error registering user:', error);
        res.status(500).json({ error: 'Failed to register user' });
    }
});

// POST /api/auth/login - Login user
router.post('/login', async (req, res) => {
    try {
        const { email, password } = req.body;
        
        // Find user by email
        const user = await User.findOne({ where: { email } });
        if (!user) {
            return res.status(401).json({ error: 'Invalid credentials' });
        }
        
        // Check password
        const isValidPassword = await bcrypt.compare(password, user.password);
        if (!isValidPassword) {
            return res.status(401).json({ error: 'Invalid credentials' });
        }
        
        res.json({
            user: { id: user.id, name: user.name, email: user.email },
            token: 'jwt-token-' + user.id,
            refreshToken: 'refresh-token-' + user.id
        });
    } catch (error) {
        console.error('Error logging in:', error);
        res.status(500).json({ error: 'Failed to login' });
    }
});

// POST /api/auth/refresh - Refresh authentication token
router.post('/refresh', async (req, res) => {
    try {
        const { refreshToken } = req.body;
        
        // In a real app, you would verify the refresh token and generate new tokens
        
        if (!refreshToken || !refreshToken.startsWith('refresh-token-')) {
            return res.status(401).json({ error: 'Invalid refresh token' });
        }
        
        const userId = refreshToken.split('-').pop();
        res.json({
            token: 'new-jwt-token-' + userId,
            refreshToken: refreshToken
        });
    } catch (error) {
        console.error('Error refreshing token:', error);
        res.status(500).json({ error: 'Failed to refresh token' });
    }
});

// POST /api/auth/logout - Logout user
router.post('/logout', async (req, res) => {
    try {
        // In a real app, you would invalidate the tokens
        res.json({ message: 'Logged out successfully' });
    } catch (error) {
        console.error('Error logging out:', error);
        res.status(500).json({ error: 'Failed to logout' });
    }
});

module.exports = router;