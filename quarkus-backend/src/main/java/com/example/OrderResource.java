package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Path("/orders")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderService orderService;

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    DishRepository dishRepository;

    /**
     * Create a new order
     * POST /orders
     */
    @POST
    @Transactional
    public Response createOrder(Order order) {
        try {
            // Validate required fields
            if (order.getUserId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("User ID is required")
                        .build();
            }
            
            if (order.getRestaurant() == null || order.getRestaurant().getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Restaurant ID is required")
                        .build();
            }

            // Validate restaurant exists
            Optional<Restaurant> restaurantOpt = restaurantRepository.findByIdOptional(order.getRestaurant().getId());
            if (!restaurantOpt.isPresent()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Restaurant not found with ID: " + order.getRestaurant().getId())
                        .build();
            }
            
            // Set the actual restaurant object
            order.setRestaurant(restaurantOpt.get());

            Order createdOrder = orderService.createOrder(order);
            return Response.status(Response.Status.CREATED).entity(createdOrder).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating order: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Get all orders
     * GET /orders
     */
    @GET
    public Response getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            return Response.ok(orders).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving orders: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Get orders by user ID
     * GET /orders?userId={id}
     */
    @GET
    @Path("/")
    public Response getOrdersByUserId(@QueryParam("userId") Long userId) {
        try {
            if (userId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("User ID query parameter is required")
                        .build();
            }
            
            List<Order> orders = orderService.getOrdersByUserId(userId);
            return Response.ok(orders).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving orders: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Get a specific order by ID
     * GET /orders/{id}
     */
    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") Long id) {
        try {
            Optional<Order> order = orderService.getOrderById(id);
            if (order.isPresent()) {
                return Response.ok(order.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Order not found with ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving order: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Update order status
     * PUT /orders/{id}
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateOrderStatus(@PathParam("id") Long id, Order updatedOrder) {
        try {
            if (updatedOrder.getStatus() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Status is required")
                        .build();
            }
            
            Order order = orderService.updateOrderStatus(id, updatedOrder.getStatus());
            return Response.ok(order).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating order: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Cancel an order
     * DELETE /orders/{id}
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response cancelOrder(@PathParam("id") Long id) {
        try {
            orderService.cancelOrder(id);
            return Response.ok().entity("Order cancelled successfully").build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error cancelling order: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Track an order
     * GET /orders/{id}/track
     */
    @GET
    @Path("/{id}/track")
    public Response trackOrder(@PathParam("id") Long id) {
        try {
            Optional<Order> order = orderService.getOrderById(id);
            if (order.isPresent()) {
                // Return order with status and delivery information
                return Response.ok(order.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Order not found with ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error tracking order: " + e.getMessage())
                    .build();
        }
    }
}