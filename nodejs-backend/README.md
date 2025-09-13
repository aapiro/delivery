# Delivery App Backend (Node.js)

This is the Node.js equivalent of the Quarkus backend for the DeliveryApp multi-restaurant delivery platform.

## 🚀 Getting Started

### Prerequisites
- Node.js 16+
- npm or yarn

### Installation

```bash
npm install
```

### Running in Development Mode

```bash
npm run dev
```

This starts the application with auto-reload at [http://localhost:59806](http://localhost:59806).

### Database Setup

The backend uses SQLite for development (similar to H2 in Quarkus). The database file will be created automatically as `database.sqlite`.

To run migrations:
```bash
node src/scripts/run-migrations.js
```

To seed the database with dummy data:
```bash
node src/scripts/seed-data.js
```

## 🛠️ Key Features

- **REST API**: Built with Express.js
- **Database**: Sequelize ORM with SQLite (development) / PostgreSQL (production)
- **Development Database**: SQLite in-memory database
- **Database Migrations**: Custom migration scripts
- **Security**: Helmet for security headers, CORS configuration
- **Authentication**: JWT-based authentication system
- **Admin Panel**: Basic admin endpoints

## 📁 Project Structure

```
src/
├── server.js          # Main application file
├── config/            # Configuration files
│   └── database.js    # Database connection setup
├── models/            # Database models
│   ├── Restaurant.js  # Restaurant entity model
│   ├── Dish.js        # Dish entity model
│   └── Category.js    # Category entity model
├── routes/            # API route handlers
│   ├── restaurant.js  # Restaurant endpoints
│   ├── dish.js        # Dish endpoints
│   ├── category.js    # Category endpoints
│   ├── search.js      # Search endpoints
│   ├── auth.js        # Authentication endpoints
│   └── admin.js       # Admin panel endpoints
└── scripts/           # Utility scripts
    ├── run-migrations.js # Migration runner
    └── seed-data.js     # Data seeding script

migrations/            # Database migration files
test/                  # Test files
```

## 🗄️ Database Schema

The application uses a relational database schema with the following key tables:

1. **restaurants**: Stores restaurant information (id, name, description, cuisine_type, rating, etc.)
2. **dishes**: Lists all available menu items (id, restaurant_id, name, price, etc.)
3. **categories**: Manages food categories for restaurants and dishes

## 📊 API Endpoints

### Restaurants
- `GET /api/restaurants` - Get all restaurants
- `POST /api/restaurants` - Create a new restaurant
- `GET /api/restaurants/:id` - Get a specific restaurant by ID
- `PUT /api/restaurants/:id` - Update a restaurant
- `DELETE /api/restaurants/:id` - Delete a restaurant

### Dishes
- `GET /api/dishes` - Get all dishes
- `POST /api/dishes` - Create a new dish
- `GET /api/dishes/:id` - Get a specific dish by ID
- `PUT /api/dishes/:id` - Update a dish
- `DELETE /api/dishes/:id` - Delete a dish

### Categories
- `GET /api/categories` - Get all categories
- `POST /api/categories` - Create a new category
- `GET /api/categories/:id` - Get a specific category by ID
- `PUT /api/categories/:id` - Update a category
- `DELETE /api/categories/:id` - Delete a category

### Search
- `GET /api/search/restaurants?q=searchTerm&category=categoryId` - Search restaurants
- `GET /api/search/dishes?q=searchTerm&restaurantId=restaurantId` - Search dishes

### Orders
- `POST /api/orders` - Create a new order
- `GET /api/orders` - Get all orders (for user or admin)
- `GET /api/orders/:id` - Get a specific order by ID
- `PUT /api/orders/:id` - Update an order status (admin only)
- `DELETE /api/orders/:id` - Cancel an order

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `POST /api/auth/refresh` - Refresh authentication token
- `POST /api/auth/logout` - Logout user

### Admin Panel
- `POST /api/admin/auth/login` - Admin login
- `GET /api/admin/dashboard` - Get dashboard statistics

## 🔧 Environment Variables

Create a `.env` file in the root directory with:
```
PORT=59806
```

## 🧪 Testing

```bash
npm test
```

## 🚀 Deployment

To build and run for production:

1. Install dependencies: `npm install --production`
2. Run migrations: `node src/scripts/run-migrations.js`
3. Seed data (optional): `node src/scripts/seed-data.js` 
4. Start the server: `npm start`

For production with PostgreSQL, update database configuration in `src/config/database.js`.

## 🔒 Security Considerations

- Helmet middleware provides security headers
- CORS is configured for development (allow all origins)
- Input validation should be added as needed

## TODO

The following features are planned or could be enhanced in future iterations:

1. **Shopping Cart Functionality** 
   - Implement add/remove items from cart
   - Add quantity management
   - Enable cart persistence between sessions

2. **Payment Processing Integration**
   - Integrate payment gateway (Stripe, PayPal)
   - Implement order confirmation flow
   - Add transaction logging

3. **Advanced User Profile Management**
   - Allow user information editing
   - Implement address book management
   - Add order history viewing capability

4. **Restaurant Owner Dashboard**
   - Create menu management interface
   - Implement order receiving notifications
   - Add performance analytics dashboard

5. **Push Notifications System**
   - Implement order status updates
   - Add promotional message delivery
   - Enable system alert notifications

6. **Enhanced Search & Filtering**
   - Advanced filtering by price range, cuisine type, delivery time
   - Sorting options (rating, price, popularity)
   - Location-based filtering capabilities

7. **Reviews and Ratings System**
   - Customer reviews for restaurants/dishes
   - Rating aggregation functionality
   - Review moderation tools

8. **Mobile Responsiveness Improvements**
   - Optimize UI for mobile devices
   - Implement touch-friendly interactions
   - Improve performance on mobile networks

9. **Security Enhancements**
    - Implement rate limiting for API endpoints
    - Add more comprehensive input validation
    - Enhance JWT token security features