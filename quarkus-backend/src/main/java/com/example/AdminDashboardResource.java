package com.example;

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
        // For now, we'll return a simple message indicating the endpoint works
        // In a real implementation, this would return comprehensive dashboard data
        DashboardOverview overview = new DashboardOverview("Admin Dashboard Overview");
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