const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const Dish = sequelize.define('Dish', {
    id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    restaurant_id: {
        type: DataTypes.INTEGER,
        allowNull: false
    },
    category_id: {
        type: DataTypes.INTEGER
    },
    name: {
        type: DataTypes.STRING(255),
        allowNull: false
    },
    description: {
        type: DataTypes.TEXT
    },
    price: {
        type: DataTypes.DECIMAL(6, 2),
        defaultValue: 0.00
    },
    image_url: {
        type: DataTypes.STRING(512)
    },
    original_price: {
        type: DataTypes.DECIMAL(6, 2)
    },
    is_available: {
        type: DataTypes.BOOLEAN,
        defaultValue: true
    },
    is_popular: {
        type: DataTypes.BOOLEAN,
        defaultValue: false
    },
    preparation_time: {
        type: DataTypes.INTEGER
    },
    ingredients: {
        type: DataTypes.TEXT
    },
    allergens: {
        type: DataTypes.TEXT
    },
    created_at: {
        type: DataTypes.DATE,
        defaultValue: DataTypes.NOW
    },
    updated_at: {
        type: DataTypes.DATE,
        defaultValue: DataTypes.NOW
    }
}, {
    tableName: 'dishes',
    timestamps: false
});

module.exports = Dish;