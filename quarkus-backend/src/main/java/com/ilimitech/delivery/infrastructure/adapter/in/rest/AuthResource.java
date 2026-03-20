package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.application.usecase.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class AuthResource {

    @Inject
    AuthService authService;

    @Inject
    io.quarkus.security.identity.SecurityIdentity securityIdentity;

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        if (request == null || isBlank(request.getName()) || isBlank(request.getEmail()) || isBlank(request.getPassword())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "name, email and password are required"))
                    .build();
        }
        AuthService.AuthTokens tokens = authService.register(request.getName(), request.getEmail(), request.getPassword());
        return Response.status(Response.Status.CREATED).entity(toPayload(tokens)).build();
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        if (request == null || isBlank(request.getEmail()) || isBlank(request.getPassword())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "email and password are required"))
                    .build();
        }
        AuthService.AuthTokens tokens = authService.login(request.getEmail(), request.getPassword());
        return Response.ok(toPayload(tokens)).build();
    }

    @POST
    @Path("/refresh")
    public Response refresh(RefreshRequest request) {
        if (request == null || isBlank(request.getRefreshToken())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "refreshToken is required"))
                    .build();
        }
        AuthService.AuthTokens tokens = authService.refresh(request.getRefreshToken());
        return Response.ok(toPayload(tokens)).build();
    }

    @POST
    @Path("/logout")
    public Response logout() {
        if (securityIdentity != null && !securityIdentity.isAnonymous()) {
            try {
                long userId = Long.parseLong(securityIdentity.getPrincipal().getName());
                authService.revokeAllRefreshTokens(userId);
            } catch (NumberFormatException ignored) {
                // JWT principal might use sub elsewhere; ignore revoke
            }
        }
        return Response.ok(Map.of("message", "User logged out successfully")).build();
    }

    private static Map<String, Object> toPayload(AuthService.AuthTokens tokens) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", tokens.user().id());
        user.put("name", tokens.user().name());
        user.put("email", tokens.user().email());
        return Map.of(
                "user", user,
                "token", tokens.token(),
                "refreshToken", tokens.refreshToken()
        );
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

class RegisterRequest {
    private String name;
    private String email;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class LoginRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class RefreshRequest {
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
