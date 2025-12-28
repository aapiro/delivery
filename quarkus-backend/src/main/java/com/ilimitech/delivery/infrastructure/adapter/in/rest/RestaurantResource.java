package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.infrastructure.adapter.out.persistence.Category;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.MenuCategory;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.RestaurantEntity;
import com.ilimitech.delivery.application.usecase.RestaurantService;
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
    public List<RestaurantEntity> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestaurantEntity addRestaurant(RestaurantEntity restaurantEntity) {
        return restaurantService.addRestaurant(restaurantEntity);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestaurantEntity getRestaurantById(@PathParam("id") Long id) {
        RestaurantEntity restaurantById = restaurantService.getRestaurantById(id);
        return restaurantById;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestaurantEntity updateRestaurant(@PathParam("id") Long id, RestaurantEntity updatedRestaurantEntity) {
        return restaurantService.updateRestaurant(id, updatedRestaurantEntity);
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
    public List<MenuCategory> getRestaurantCategories() {
        return restaurantService.getAllCategories();
    }

    /**
     * Search restaurants by name or cuisine
     * GET /restaurants/search?name={name}&cuisine={cuisine}
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestaurantEntity> searchRestaurants(
            @QueryParam("name") String name,
            @QueryParam("cuisine") String cuisine) {
        return restaurantService.searchRestaurants(name, cuisine);
    }
}
