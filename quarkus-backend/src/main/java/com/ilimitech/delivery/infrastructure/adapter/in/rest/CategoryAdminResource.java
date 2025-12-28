package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.application.usecase.CategoryService;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.Category;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/admin/categories")
public class CategoryAdminResource {

    @Inject
    CategoryService categoryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> getAllCategories(
            @QueryParam("name") String name,
            @QueryParam("isActive") Boolean isActive) {
        return categoryService.getAllCategoriesFiltered(name, isActive);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Category getCategoryById(@PathParam("id") Long id) {
        return categoryService.getCategoryById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Category addCategory(Category category) {
        return categoryService.addCategory(category);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Category updateCategory(@PathParam("id") Long id, Category updatedCategory) {
        return categoryService.updateCategory(id, updatedCategory);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        categoryService.deleteCategory(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/toggle-status")
    @Produces(MediaType.APPLICATION_JSON)
    public Category toggleCategoryStatus(@PathParam("id") Long id) {
        return categoryService.toggleCategoryStatus(id);
    }
    
    /**
     * Search categories by name (admin endpoint)
     * GET /admin/categories/search?name={name}
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> searchCategories(@QueryParam("name") String name) {
        return categoryService.searchCategories(name);
    }
}