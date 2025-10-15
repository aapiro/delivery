package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/admin/restaurants")
public class RestaurantAdminResource {

    @Inject
    RestaurantService restaurantService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Restaurant> getAllRestaurants(
            @QueryParam("name") String name,
            @QueryParam("cuisine") String cuisine,
            @QueryParam("isOpen") Boolean isOpen) {
        return restaurantService.getAllRestaurantsFiltered(name, cuisine, isOpen);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Restaurant getRestaurantById(@PathParam("id") Long id) {
        return restaurantService.getRestaurantById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantService.addRestaurant(restaurant);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Restaurant updateRestaurant(@PathParam("id") Long id, Restaurant updatedRestaurant) {
        return restaurantService.updateRestaurant(id, updatedRestaurant);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRestaurant(@PathParam("id") Long id) {
        restaurantService.deleteRestaurant(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/toggle-status")
    @Produces(MediaType.APPLICATION_JSON)
    public Restaurant toggleRestaurantStatus(@PathParam("id") Long id) {
        return restaurantService.toggleRestaurantStatus(id);
    }
    
    /**
     * Search restaurants by name or cuisine (admin endpoint)
     * GET /admin/restaurants/search?name={name}&cuisine={cuisine}
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Restaurant> searchRestaurants(
            @QueryParam("name") String name,
            @QueryParam("cuisine") String cuisine) {
        return restaurantService.searchRestaurants(name, cuisine);
    }
}