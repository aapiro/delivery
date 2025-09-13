const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const Restaurant = sequelize.define('Restaurant', {
    id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    name: {
        type: DataTypes.STRING(255),
        allowNull: false
    },
    description: {
        type: DataTypes.TEXT
    },
    cuisine_type: {
        type: DataTypes.STRING(100)
    },
    rating: {
        type: DataTypes.DECIMAL(3, 1),
        defaultValue: 0.0
    },
    review_count: {
        type: DataTypes.INTEGER,
        defaultValue: 0
    },
    delivery_time_min: {
        type: DataTypes.INTEGER
    },
    delivery_time_max: {
        type: DataTypes.INTEGER
    },
    delivery_fee: {
        type: DataTypes.DECIMAL(5, 2),
        defaultValue: 0.00
    },
    minimum_order: {
        type: DataTypes.DECIMAL(6, 2),
        defaultValue: 0.00
    },
    is_open: {
        type: DataTypes.BOOLEAN,
        defaultValue: true
    },
    image_url: {
        type: DataTypes.STRING(512)
    }
}, {
    tableName: 'restaurants',
    timestamps: false
});

module.exports = Restaurant;