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
    logo: {
        type: DataTypes.STRING(512)
    },
    cover_image: {
        type: DataTypes.STRING(512)
    },
    address: {
        type: DataTypes.TEXT
    },
    phone: {
        type: DataTypes.STRING(20)
    },
    email: {
        type: DataTypes.STRING(255)
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
    is_active: {
        type: DataTypes.BOOLEAN,
        defaultValue: true
    },
    category_id: {
        type: DataTypes.INTEGER
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
    tableName: 'restaurants',
    timestamps: false
});

module.exports = Restaurant;