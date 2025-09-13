const { Sequelize } = require('sequelize');
require('dotenv').config();

// Create sequelize instance - using SQLite for simplicity (similar to H2 in Quarkus)
const sequelize = new Sequelize({
    dialect: 'sqlite',
    storage: './database.sqlite',
    logging: false // Set to true for SQL query logging
});

module.exports = sequelize;