const { runMigrations } = require('./run-migrations');
const Restaurant = require('../models/Restaurant');
const Dish = require('../models/Dish');
const Category = require('../models/Category');
const User = require('../models/User');
const bcrypt = require('bcrypt');

async function seedData() {
    try {
        console.log('Seeding database with dummy data...');
        
        // Run migrations first to ensure tables exist
        await runMigrations();
        
        // Create test users if they don't exist
        const existingUsers = await User.findAll();
        
        if (existingUsers.length === 0) {
            console.log('Creating test users...');
            
            // Hash passwords for test users
            const hashedPassword1 = await bcrypt.hash('password123', 10);
            const hashedPassword2 = await bcrypt.hash('mypassword', 10);
            
            await User.create({
                name: 'John Doe',
                email: 'john@example.com',
                password: hashedPassword1
            });
            
            await User.create({
                name: 'Jane Smith',
                email: 'jane@example.com',
                password: hashedPassword2
            });
            
            console.log('Test users created successfully');
        } else {
            console.log('Users already exist, skipping user creation');
        }
        
        // Create categories
        const categories = [
            { name: 'Pizza', slug: 'pizza', icon: '🍕' },
            { name: 'Hamburguesas', slug: 'hamburguesas', icon: '🍔' },
            { name: 'Sushi', slug: 'sushi', icon: '🍣' },
            { name: 'Ensaladas', slug: 'ensaladas', icon: '🥗' },
            { name: 'Postres', slug: 'postres', icon: '🍰' }
        ];
        
        const createdCategories = [];
        for (const categoryData of categories) {
            let category = await Category.findOne({ where: { name: categoryData.name } });
            if (!category) {
                category = await Category.create(categoryData);
            }
            createdCategories.push(category);
        }
        
        // Create restaurants
        const restaurants = [
            {
                name: 'Pizza Palace',
                description: 'La mejor pizza de la ciudad',
                logo: '/images/pizza-palace-logo.png',
                cover_image: '/images/pizza-palace-cover.jpg',
                address: 'Calle Mayor 123, Madrid',
                phone: '+34 912 345 678',
                email: 'info@pizzapalace.com',
                cuisine_type: 'Italiana',
                rating: 4.8,
                review_count: 120,
                delivery_time_min: 20,
                delivery_time_max: 35,
                delivery_fee: 2.99,
                minimum_order: 10.00,
                is_open: true,
                is_active: true,
                category_id: createdCategories[0].id
            },
            {
                name: 'Burger King',
                description: 'Hamburguesas gourmet',
                logo: '/images/burger-king-logo.png',
                cover_image: '/images/burger-king-cover.jpg',
                address: 'Avenida Principal 45, Madrid',
                phone: '+34 912 345 679',
                email: 'info@burgerking.com',
                cuisine_type: 'Americana',
                rating: 4.2,
                review_count: 85,
                delivery_time_min: 15,
                delivery_time_max: 30,
                delivery_fee: 1.99,
                minimum_order: 8.00,
                is_open: true,
                is_active: true,
                category_id: createdCategories[1].id
            },
            {
                name: 'Sushi Express',
                description: 'Sushi fresco y auténtico',
                logo: '/images/sushi-express-logo.png',
                cover_image: '/images/sushi-express-cover.jpg',
                address: 'Calle del Sol 78, Madrid',
                phone: '+34 912 345 680',
                email: 'info@sushexpress.com',
                cuisine_type: 'Japonesa',
                rating: 4.6,
                review_count: 92,
                delivery_time_min: 25,
                delivery_time_max: 40,
                delivery_fee: 3.99,
                minimum_order: 12.00,
                is_open: true,
                is_active: true,
                category_id: createdCategories[2].id
            }
        ];
        
        const createdRestaurants = [];
        for (const restaurantData of restaurants) {
            let restaurant = await Restaurant.findOne({ where: { name: restaurantData.name } });
            if (!restaurant) {
                restaurant = await Restaurant.create(restaurantData);
            }
            createdRestaurants.push(restaurant);
        }
        
        // Create dishes
        const dishes = [
            {
                restaurant_id: createdRestaurants[0].id,
                category_id: createdCategories[0].id,
                name: 'Pizza Margherita',
                description: 'Mozzarella, tomate y albahaca',
                price: 12.99,
                image_url: '/images/pizza-margherita.jpg',
                is_available: true,
                is_popular: true,
                preparation_time: 15,
                ingredients: 'Mozzarella, Tomate, Albahaca',
                allergens: 'Gluten, Lácteos'
            },
            {
                restaurant_id: createdRestaurants[0].id,
                category_id: createdCategories[0].id,
                name: 'Pizza Pepperoni',
                description: 'Mozzarella, tomate y pepperoni',
                price: 14.99,
                image_url: '/images/pizza-pepperoni.jpg',
                is_available: true,
                is_popular: false,
                preparation_time: 20,
                ingredients: 'Mozzarella, Tomate, Pepperoni',
                allergens: 'Gluten, Lácteos'
            },
            {
                restaurant_id: createdRestaurants[1].id,
                category_id: createdCategories[1].id,
                name: 'Big Burger',
                description: 'Hamburguesa con queso, lechuga y tomate',
                price: 9.99,
                image_url: '/images/big-burger.jpg',
                is_available: true,
                is_popular: true,
                preparation_time: 10,
                ingredients: 'Carne, Queso, Lechuga, Tomate',
                allergens: 'Gluten, Lácteos'
            },
            {
                restaurant_id: createdRestaurants[2].id,
                category_id: createdCategories[2].id,
                name: 'California Roll',
                description: 'Cangrejo, aguacate y pepino',
                price: 8.99,
                image_url: '/images/california-roll.jpg',
                is_available: true,
                is_popular: false,
                preparation_time: 12,
                ingredients: 'Cangrejo, Aguacate, Pepino',
                allergens: 'Gluten, Mariscos'
            }
        ];
        
        for (const dishData of dishes) {
            let dish = await Dish.findOne({ where: { name: dishData.name } });
            if (!dish) {
                await Dish.create(dishData);
            }
        }
        
        console.log('✅ Dummy data seeded successfully!');
    } catch (error) {
        console.error('❌ Error seeding data:', error);
        process.exit(1);
    }
}

if (require.main === module) {
    seedData();
}

module.exports = { seedData };