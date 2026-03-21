package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ilimitech.delivery.application.usecase.CartService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/cart")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class CartResource {

    @Inject
    CartService cartService;

    @Inject
    SecurityIdentity securityIdentity;

    @GET
    public Response getCart() {
        CartService.CartSnapshot cart = cartService.getCart(currentUserId());
        return Response.ok(Map.of(
                "message", "Cart retrieved successfully",
                "items", cart.items(),
                "total", cart.total(),
                "restaurantId", cart.restaurantId()
        )).build();
    }

    @POST
    @Path("/add")
    public Response addToCart(AddToCartBody body) {
        CartService.CartSnapshot cart = cartService.add(
                currentUserId(),
                body.dishId,
                body.quantity,
                body.specialInstructions
        );
        return Response.ok(Map.of(
                "message", "Item added to cart successfully",
                "items", cart.items(),
                "total", cart.total(),
                "restaurantId", cart.restaurantId()
        )).build();
    }

    @PUT
    @Path("/update")
    public Response updateCart(UpdateCartBody body) {
        CartService.CartSnapshot cart = cartService.update(
                currentUserId(),
                body.itemId,
                body.quantity
        );
        return Response.ok(Map.of(
                "message", "Cart updated successfully",
                "items", cart.items(),
                "total", cart.total(),
                "restaurantId", cart.restaurantId()
        )).build();
    }

    @DELETE
    @Path("/remove")
    public Response removeFromCart(RemoveFromCartBody body) {
        CartService.CartSnapshot cart = cartService.remove(
                currentUserId(),
                body.itemId
        );
        return Response.ok(Map.of(
                "message", "Item removed from cart successfully",
                "items", cart.items(),
                "total", cart.total(),
                "restaurantId", cart.restaurantId()
        )).build();
    }

    @DELETE
    @Path("/clear")
    public Response clearCart() {
        CartService.CartSnapshot cart = cartService.clear(currentUserId());
        return Response.ok(Map.of(
                "message", "Cart cleared successfully",
                "items", cart.items(),
                "total", cart.total(),
                "restaurantId", cart.restaurantId()
        )).build();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddToCartBody {
        public Long dishId;
        public Integer quantity;
        public String specialInstructions;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateCartBody {
        public Long itemId;
        public Integer quantity;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RemoveFromCartBody {
        public Long itemId;
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