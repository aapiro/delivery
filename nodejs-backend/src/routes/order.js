const express = require('express');
const router = express.Router();
const Order = require('../models/Order');
const OrderItem = require('../models/OrderItem');

// Middleware for authentication (placeholder - will be implemented with JWT)
const authenticateToken = (req, res, next) => {
  // This is a placeholder. In a real implementation, this would verify JWT tokens
  // For now, we'll allow all requests to test functionality
  next();
};

/**
 * @swagger
 * /api/orders:
 *   post:
 *     summary: Create a new order
 *     tags: [Orders]
 *     security:
 *       - bearerAuth: []
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required:
 *               - restaurant_id
 *               - delivery_address
 *               - payment_method
 *               - items
 *             properties:
 *               restaurant_id:
 *                 type: integer
 *               delivery_address:
 *                 type: string
 *               delivery_instructions:
 *                 type: string
 *               estimated_delivery_time:
 *                 type: string
 *                 format: date-time
 *               payment_method:
 *                 type: string
 *                 enum: [cash, card, online]
 *               items:
 *                 type: array
 *                 items:
 *                   type: object
 *                   required:
 *                     - dish_id
 *                     - quantity
 *                   properties:
 *                     dish_id:
 *                       type: integer
 *                     quantity:
 *                       type: integer
 *                     special_instructions:
 *                       type: string
 *     responses:
 *       201:
 *         description: Order created successfully
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Order'
 *       400:
 *         description: Invalid request data
 *       500:
 *         description: Server error
 */
router.post('/', authenticateToken, async (req, res) => {
  try {
    const { restaurant_id, delivery_address, delivery_instructions, estimated_delivery_time, payment_method, items } = req.body;
    
    // Validate required fields
    if (!restaurant_id || !delivery_address || !payment_method || !items || !Array.isArray(items)) {
      return res.status(400).json({ 
        error: 'Missing required fields: restaurant_id, delivery_address, payment_method, items' 
      });
    }

    // Calculate total amount
    let totalAmount = 0;
    
    // Validate each item and calculate price
    const orderItems = [];
    for (const item of items) {
      if (!item.dish_id || !item.quantity) {
        return res.status(400).json({ 
          error: 'Each item must have dish_id and quantity' 
        });
      }
      
      // In a real implementation, we would fetch the dish to get its price
      // For now, we'll use a placeholder value for demonstration
      const unitPrice = 10.00; // Placeholder - in reality this should come from Dish model
      const totalPrice = unitPrice * item.quantity;
      
      orderItems.push({
        dish_id: item.dish_id,
        quantity: item.quantity,
        unit_price: unitPrice,
        total_price: totalPrice,
        special_instructions: item.special_instructions || ''
      });
      
      totalAmount += totalPrice;
    }

    // Create the order
    const order = await Order.create({
      user_id: req.user ? req.user.id : 1, // Placeholder - in real app this would come from JWT token
      restaurant_id,
      delivery_address,
      delivery_instructions,
      estimated_delivery_time,
      payment_method,
      total_amount: totalAmount,
      status: 'pending',
      payment_status: 'pending'
    });

    // Create order items
    const createdOrderItems = await Promise.all(
      orderItems.map(item => 
        OrderItem.create({
          ...item,
          order_id: order.id
        })
      )
    );

    // Return the complete order with items
    const fullOrder = {
      ...order.toJSON(),
      items: createdOrderItems
    };

    res.status(201).json(fullOrder);
  } catch (error) {
    console.error('Error creating order:', error);
    res.status(500).json({ 
      error: 'Failed to create order', 
      details: error.message 
    });
  }
});

/**
 * @swagger
 * /api/orders:
 *   get:
 *     summary: Get all orders for a user (or admin)
 *     tags: [Orders]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: query
 *         name: status
 *         schema:
 *           type: string
 *         description: Filter by order status
 *       - in: query
 *         name: limit
 *         schema:
 *           type: integer
 *         description: Limit number of results
 *       - in: query
 *         name: offset
 *         schema:
 *           type: integer
 *         description: Offset for pagination
 *     responses:
 *       200:
 *         description: List of orders
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 $ref: '#/components/schemas/Order'
 *       500:
 *         description: Server error
 */
router.get('/', authenticateToken, async (req, res) => {
  try {
    const { status, limit = 10, offset = 0 } = req.query;
    
    // Build query conditions
    let whereClause = {};
    if (status) {
      whereClause.status = status;
    }
    
    // In a real implementation, we would filter by user_id for regular users
    // For now, we'll return all orders to demonstrate functionality
    
    const orders = await Order.findAll({
      where: whereClause,
      limit: parseInt(limit),
      offset: parseInt(offset),
      order: [['created_at', 'DESC']]
    });
    
    res.json(orders);
  } catch (error) {
    console.error('Error fetching orders:', error);
    res.status(500).json({ 
      error: 'Failed to fetch orders', 
      details: error.message 
    });
  }
});

/**
 * @swagger
 * /api/orders/{id}:
 *   get:
 *     summary: Get a specific order by ID
 *     tags: [Orders]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *     responses:
 *       200:
 *         description: Order details
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Order'
 *       404:
 *         description: Order not found
 *       500:
 *         description: Server error
 */
router.get('/:id', authenticateToken, async (req, res) => {
  try {
    const orderId = parseInt(req.params.id);
    
    if (!orderId) {
      return res.status(400).json({ error: 'Invalid order ID' });
    }
    
    const order = await Order.findByPk(orderId, {
      include: [{
        model: OrderItem,
        as: 'items'
      }]
    });
    
    if (!order) {
      return res.status(404).json({ error: 'Order not found' });
    }
    
    res.json(order);
  } catch (error) {
    console.error('Error fetching order:', error);
    res.status(500).json({ 
      error: 'Failed to fetch order', 
      details: error.message 
    });
  }
});

/**
 * @swagger
 * /api/orders/{id}:
 *   put:
 *     summary: Update an order status (admin only)
 *     tags: [Orders]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required:
 *               - status
 *             properties:
 *               status:
 *                 type: string
 *                 enum: [pending, confirmed, preparing, on_the_way, delivered, cancelled]
 *     responses:
 *       200:
 *         description: Order updated successfully
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Order'
 *       400:
 *         description: Invalid request data
 *       404:
 *         description: Order not found
 *       500:
 *         description: Server error
 */
router.put('/:id', authenticateToken, async (req, res) => {
  try {
    const orderId = parseInt(req.params.id);
    const { status } = req.body;
    
    if (!orderId) {
      return res.status(400).json({ error: 'Invalid order ID' });
    }
    
    // Validate status
    const validStatuses = ['pending', 'confirmed', 'preparing', 'on_the_way', 'delivered', 'cancelled'];
    if (status && !validStatuses.includes(status)) {
      return res.status(400).json({ 
        error: `Invalid status. Must be one of: ${validStatuses.join(', ')}` 
      });
    }
    
    const order = await Order.findByPk(orderId);
    
    if (!order) {
      return res.status(404).json({ error: 'Order not found' });
    }
    
    // Update the status
    if (status) {
      order.status = status;
      
      // Set actual delivery time when delivered
      if (status === 'delivered') {
        order.actual_delivery_time = new Date();
      }
      
      await order.save();
    }
    
    res.json(order);
  } catch (error) {
    console.error('Error updating order:', error);
    res.status(500).json({ 
      error: 'Failed to update order', 
      details: error.message 
    });
  }
});

/**
 * @swagger
 * /api/orders/{id}:
 *   delete:
 *     summary: Cancel an order (if not yet confirmed)
 *     tags: [Orders]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *     responses:
 *       200:
 *         description: Order cancelled successfully
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Order'
 *       400:
 *         description: Cannot cancel order
 *       404:
 *         description: Order not found
 *       500:
 *         description: Server error
 */
router.delete('/:id', authenticateToken, async (req, res) => {
  try {
    const orderId = parseInt(req.params.id);
    
    if (!orderId) {
      return res.status(400).json({ error: 'Invalid order ID' });
    }
    
    const order = await Order.findByPk(orderId);
    
    if (!order) {
      return res.status(404).json({ error: 'Order not found' });
    }
    
    // Only allow cancellation for pending orders
    if (order.status !== 'pending') {
      return res.status(400).json({ 
        error: 'Cannot cancel order. Order status must be "pending"' 
      });
    }
    
    // Update the status to cancelled
    order.status = 'cancelled';
    await order.save();
    
    res.json(order);
  } catch (error) {
    console.error('Error cancelling order:', error);
    res.status(500).json({ 
      error: 'Failed to cancel order', 
      details: error.message 
    });
  }
});

module.exports = router;