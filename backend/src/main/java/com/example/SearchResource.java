package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/search")
public class SearchResource {

    @Inject
    SearchService searchService;

    @GET
    @Path("/restaurants")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Restaurant> searchRestaurants(
            @QueryParam("q") String query,
            @QueryParam("category") String category,
            @QueryParam("minRating") Double minRating,
            @QueryParam("isOpen") Boolean isOpen) {
        return searchService.searchRestaurants(query, category, minRating, isOpen);
    }

    @GET
    @Path("/dishes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dish> searchDishes(@QueryParam("q") String query) {
        return searchService.searchDishes(query);
    }
}