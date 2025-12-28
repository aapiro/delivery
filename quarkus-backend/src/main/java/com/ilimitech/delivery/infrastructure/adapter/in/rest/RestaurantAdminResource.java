package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.infrastructure.adapter.out.persistence.RestaurantEntity;
import com.ilimitech.delivery.application.usecase.RestaurantService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/admin/restaurants")
public class RestaurantAdminResource {

    @Inject
    RestaurantService restaurantService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestaurantEntity> getAllRestaurants(
            @QueryParam("name") String name,
            @QueryParam("cuisine") String cuisine,
            @QueryParam("isOpen") Boolean isOpen) {
        return restaurantService.getAllRestaurantsFiltered(name, cuisine, isOpen);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurantById(@PathParam("id") Long id) {
        return Optional.ofNullable(restaurantService.getRestaurantById(id))
                .map(entity -> Response.status(Response.Status.OK)
                        .entity(entity)
                        .build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRestaurant(RestaurantEntity restaurantEntity) {
        RestaurantEntity createdRestaurantEntity = restaurantService.addRestaurant(restaurantEntity);
        return Response.status(Response.Status.CREATED)
                .entity(createdRestaurantEntity)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRestaurant(@PathParam("id") Long id, RestaurantEntity updatedRestaurantEntity) {
        return Optional.ofNullable(restaurantService.updateRestaurant(id, updatedRestaurantEntity))
                .map(entity -> Response.status(Response.Status.OK)
                        .entity(entity)
                        .build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
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
    public RestaurantEntity toggleRestaurantStatus(@PathParam("id") Long id) {
        return restaurantService.toggleRestaurantStatus(id);
    }
    
    /**
     * Search restaurants by name or cuisine (admin endpoint)
     * GET /admin/restaurants/search?name={name}&cuisine={cuisine}
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