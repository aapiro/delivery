package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.common.NotFoundException;
import com.ilimitech.delivery.application.port.out.DishRepository;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.Order;
import com.ilimitech.delivery.application.usecase.OrderService;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.RestaurantEntity;
import com.ilimitech.delivery.application.port.out.RestaurantRepository;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/orders")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class OrderResource {

    @Inject
    OrderService orderService;

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    DishRepository dishRepository;

    @Inject
    SecurityIdentity securityIdentity;

    /**
     * Create a new order
     * POST /orders
     */
    @POST
    @Transactional
    public Response createOrder(Order order) {
        try {
            // For security/UX: userId comes from the JWT, not from the client payload.
            order.setUserId(currentUserId());
            
            if (order.getRestaurant() == null || order.getRestaurant().getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Restaurant ID is required")
                        .build();
            }

            // Validate restaurant exists
            Optional<RestaurantEntity> restaurantOpt = restaurantRepository.findByIdOptional(order.getRestaurant().getId());
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
            // Customers should only see their own orders.
            // (Admin ordering UI uses /admin/* endpoints.)
            List<Order> orders = orderService.getOrdersByUserId(currentUserId());
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
            // Backward compatible: ignore query userId and enforce JWT user.
            List<Order> orders = orderService.getOrdersByUserId(currentUserId());
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
                if (!currentUserId().equals(order.get().getUserId())) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
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
            Optional<Order> existing = orderService.getOrderById(id);
            if (existing.isPresent() && !currentUserId().equals(existing.get().getUserId())) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

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
                if (!currentUserId().equals(order.get().getUserId())) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
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

    private Long currentUserId() {
        if (securityIdentity == null || securityIdentity.isAnonymous()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        try {
            return Long.parseLong(securityIdentity.getPrincipal().getName());
        } catch (NumberFormatException e) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
}