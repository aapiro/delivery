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

## 🛠️ Key Features

- **REST API**: Built with Express.js
- **Database**: Sequelize ORM with SQLite (development) / PostgreSQL (production)
- **Development Database**: SQLite in-memory database
- **Database Migrations**: Custom migration scripts
- **Security**: Helmet for security headers, CORS configuration

## 📁 Project Structure

```
src/
├── server.js          # Main application file
├── config/            # Configuration files
│   └── database.js    # Database connection setup
├── models/            # Database models
│   ├── Restaurant.js  # Restaurant entity model
│   └── Dish.js        # Dish entity model
├── routes/            # API route handlers
│   ├── restaurant.js  # Restaurant endpoints
│   └── dish.js        # Dish endpoints
└── scripts/           # Utility scripts
    └── run-migrations.js # Migration runner

migrations/            # Database migration files
test/                  # Test files
```

## 🗄️ Database Schema

The application uses a relational database schema with the following key tables:

1. **restaurants**: Stores restaurant information (id, name, description, cuisine_type, rating, etc.)
2. **dishes**: Lists all available menu items (id, restaurant_id, name, price, etc.)

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
3. Start the server: `npm start`

For production with PostgreSQL, update database configuration in `src/config/database.js`.

## 🔒 Security Considerations

- Helmet middleware provides security headers
- CORS is configured for development (allow all origins)
- Input validation should be added as needed