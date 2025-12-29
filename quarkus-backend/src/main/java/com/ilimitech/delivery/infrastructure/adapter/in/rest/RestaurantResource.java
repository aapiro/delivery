package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.infrastructure.adapter.in.rest.generated.model.CreateRestaurantRequest;
import com.ilimitech.delivery.infrastructure.adapter.in.rest.generated.model.PagedRestaurantResponse;
import com.ilimitech.delivery.infrastructure.adapter.in.rest.generated.model.Restaurant;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.MenuCategory;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.RestaurantEntity;
import com.ilimitech.delivery.application.usecase.RestaurantService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/restaurants")
public class RestaurantResource {

    @Inject
    RestaurantService restaurantService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurants(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        PagedRestaurantResponse pagedResponse = restaurantService.getRestaurants(page, size);
        return Response.ok(pagedResponse).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Restaurant addRestaurant(CreateRestaurantRequest createRestaurantRequest) {
        System.out.println(createRestaurantRequest.toString());
        return Restaurant.builder().build();
//        return restaurantService.addRestaurant(restaurantEntity);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurantById(@PathParam("id") Long id) {
        return Optional.ofNullable(restaurantService.getRestaurantById(id))
                .map(restaurant -> Response.status(Response.Status.OK)
                        .entity(restaurant)
                        .build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

//    @PUT
//    @Path("/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public RestaurantEntity updateRestaurant(@PathParam("id") Long id, Restaurant restaurant) {
//        return restaurantService.updateRestaurant(id, restaurant);
//    }

//    @DELETE
//    @Path("/{id}")
//    public void deleteRestaurant(@PathParam("id") Long id) {
//        restaurantService.deleteRestaurant(id);
//    }

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
