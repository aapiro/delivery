package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.application.usecase.MenuCategoryAdminService;
import com.ilimitech.delivery.infrastructure.adapter.in.rest.dto.AdminMenuCategoryResponse;
import com.ilimitech.delivery.infrastructure.adapter.in.rest.dto.AdminMenuCategoryWriteDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/admin/restaurants/{restaurantId}/menu-categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
public class MenuCategoryAdminResource {

    @Inject
    MenuCategoryAdminService menuCategoryAdminService;

    @GET
    public List<AdminMenuCategoryResponse> list(@PathParam("restaurantId") Long restaurantId) {
        return menuCategoryAdminService.listByRestaurant(restaurantId);
    }

    @POST
    public AdminMenuCategoryResponse create(
            @PathParam("restaurantId") Long restaurantId,
            AdminMenuCategoryWriteDto dto) {
        return menuCategoryAdminService.create(restaurantId, dto);
    }

    @PUT
    @Path("/{categoryId}")
    public AdminMenuCategoryResponse update(
            @PathParam("restaurantId") Long restaurantId,
            @PathParam("categoryId") Long categoryId,
            AdminMenuCategoryWriteDto dto) {
        return menuCategoryAdminService.update(restaurantId, categoryId, dto);
    }

    @DELETE
    @Path("/{categoryId}")
    public Response delete(
            @PathParam("restaurantId") Long restaurantId,
            @PathParam("categoryId") Long categoryId) {
        menuCategoryAdminService.delete(restaurantId, categoryId);
        return Response.noContent().build();
    }
}
