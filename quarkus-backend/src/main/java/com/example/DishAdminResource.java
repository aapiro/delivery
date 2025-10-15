package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/admin/dishes")
public class DishAdminResource {

    @Inject
    DishService dishService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dish> getAllDishes(
            @QueryParam("name") String name,
            @QueryParam("categoryId") Long categoryId,
            @QueryParam("isAvailable") Boolean isAvailable) {
        return dishService.getAllDishesFiltered(name, categoryId, isAvailable);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Dish getDishById(@PathParam("id") Long id) {
        return dishService.getDishById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Dish addDish(Dish dish) {
        return dishService.addDish(dish);
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
    public Response deleteDish(@PathParam("id") Long id) {
        dishService.deleteDish(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/toggle-availability")
    @Produces(MediaType.APPLICATION_JSON)
    public Dish toggleDishAvailability(@PathParam("id") Long id) {
        return dishService.toggleDishAvailability(id);
    }
    
    /**
     * Search dishes by name or category (admin endpoint)
     * GET /admin/dishes/search?name={name}&categoryId={categoryId}
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dish> searchDishes(
            @QueryParam("name") String name,
            @QueryParam("categoryId") Long categoryId) {
        return dishService.searchDishes(name, categoryId);
    }
}