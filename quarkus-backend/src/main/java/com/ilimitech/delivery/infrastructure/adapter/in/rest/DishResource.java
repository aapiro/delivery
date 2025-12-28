package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.infrastructure.adapter.out.persistence.Category;
import com.ilimitech.delivery.application.usecase.DishService;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.DishEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/dishes")
public class DishResource {

    @Inject
    DishService dishService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DishEntity> getAllDishes() {
        return dishService.getAllDishes();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DishEntity addDish(DishEntity dishEntity) {
        return dishService.addDish(dishEntity);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DishEntity getDishById(@PathParam("id") Long id) {
        return dishService.getDishById(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DishEntity updateDish(@PathParam("id") Long id, DishEntity updatedDishEntity) {
        return dishService.updateDish(id, updatedDishEntity);
    }

    @DELETE
    @Path("/{id}")
    public void deleteDish(@PathParam("id") Long id) {
        dishService.deleteDish(id);
    }
    
    /**
     * Search dishes by name or category
     * GET /dishes/search?name={name}&categoryId={categoryId}
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DishEntity> searchDishes(
            @QueryParam("name") String name,
            @QueryParam("categoryId") Long categoryId) {
        return dishService.searchDishes(name, categoryId);
    }

    /**
     * Get dish categories for a specific restaurant
     * GET /restaurants/{restaurantId}/dish-categories
     */
    @GET
    @Path("/restaurants/{restaurantId}/dish-categories")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> getRestaurantDishCategories(@PathParam("restaurantId") Long restaurantId) {
        return dishService.getRestaurantDishCategories(restaurantId);
    }
}