package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.application.usecase.DishService;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.DishEntity;
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
    public List<DishEntity> getAllDishes(
            @QueryParam("name") String name,
            @QueryParam("categoryId") Long categoryId,
            @QueryParam("isAvailable") Boolean isAvailable) {
        return dishService.getAllDishesFiltered(name, categoryId, isAvailable);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DishEntity getDishById(@PathParam("id") Long id) {
        return dishService.getDishById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DishEntity addDish(DishEntity dishEntity) {
        return dishService.addDish(dishEntity);
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
    public Response deleteDish(@PathParam("id") Long id) {
        dishService.deleteDish(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/toggle-availability")
    @Produces(MediaType.APPLICATION_JSON)
    public DishEntity toggleDishAvailability(@PathParam("id") Long id) {
        return dishService.toggleDishAvailability(id);
    }
    
    /**
     * Search dishes by name or category (admin endpoint)
     * GET /admin/dishes/search?name={name}&categoryId={categoryId}
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DishEntity> searchDishes(
            @QueryParam("name") String name,
            @QueryParam("categoryId") Long categoryId) {
        return dishService.searchDishes(name, categoryId);
    }
}