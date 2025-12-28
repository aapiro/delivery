package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.domain.model.DashboardOverview;
import com.ilimitech.delivery.application.usecase.DashboardService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/dashboard")
public class AdminDashboardResource {

    @Inject
    DashboardService dashboardService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDashboardOverview() {
        // Return comprehensive dashboard overview with key metrics
        DashboardService.DashboardStats stats = dashboardService.getDashboardStatistics();
        
        // Create a more detailed overview response
        DashboardOverview overview = new DashboardOverview(
            "Admin Dashboard Overview",
            stats.getTotalRestaurants(),
            stats.getActiveRestaurants(), 
            stats.getTotalDishes(),
            stats.getAvailableDishes(),
            stats.getTotalOrders(),
            stats.getRecentOrders(),
            stats.getTotalRevenue()
        );
        
        return Response.ok(overview).build();
    }

    @GET
    @Path("/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDashboardStats() {
        DashboardService.DashboardStats stats = dashboardService.getDashboardStatistics();
        return Response.ok(stats).build();
    }
}