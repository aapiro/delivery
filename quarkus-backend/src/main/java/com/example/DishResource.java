package com.example;

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
    public List<Dish> getAllDishes() {
        return dishService.getAllDishes();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Dish addDish(Dish dish) {
        return dishService.addDish(dish);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Dish getDishById(@PathParam("id") Long id) {
        return dishService.getDishById(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Dish updateDish(@PathParam("id") Long id, Dish updatedDish) {
        return dishService.updateDish(id, updatedDish);
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
    public List<Dish> searchDishes(
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