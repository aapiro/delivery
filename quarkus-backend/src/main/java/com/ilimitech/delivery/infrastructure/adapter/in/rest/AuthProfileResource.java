package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.application.usecase.AuthService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.HashMap;
import java.util.Map;

@Path("/auth/profile")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class AuthProfileResource {

    @Inject
    AuthService authService;

    @Inject
    JsonWebToken jwt;

    @GET
    public Response getProfile() {
        Long id = Long.valueOf(jwt.getSubject());
        AuthService.UserProfileDto profile = authService.profile(id);
        Map<String, Object> user = new HashMap<>();
        user.put("id", profile.id());
        user.put("name", profile.name());
        user.put("email", profile.email());
        user.put("userType", profile.userType());
        return Response.ok(Map.of(
                "message", "User profile retrieved successfully",
                "user", user
        )).build();
    }
}
