package com.ilimitech.delivery.restaurant;

import com.ilimitech.delivery.category.Category;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/restaurants")
public class RestaurantResource {

    @Inject
    RestaurantService restaurantService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantService.addRestaurant(restaurant);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Restaurant getRestaurantById(@PathParam("id") Long id) {
        return restaurantService.getRestaurantById(id);
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
    public void deleteRestaurant(@PathParam("id") Long id) {
        restaurantService.deleteRestaurant(id);
    }
    
    /**
     * Get restaurant categories
     * GET /restaurants/categories
     */
    @GET
    @Path("/categories")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> getRestaurantCategories() {
        return restaurantService.getAllCategories();
    }

    /**
     * Search restaurants by name or cuisine
     * GET /restaurants/search?name={name}&cuisine={cuisine}
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
