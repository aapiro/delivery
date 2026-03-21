package com.ilimitech.delivery.infrastructure.adapter.in.rest;

import com.ilimitech.delivery.application.usecase.AdminAuthService;
import com.ilimitech.delivery.application.usecase.AdminAuthService.AdminAuthTokens;
import com.ilimitech.delivery.application.usecase.AdminAuthService.AdminProfileDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

import io.quarkus.security.identity.SecurityIdentity;

@Path("/admin/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminAuthResource {

    @Inject
    AdminAuthService adminAuthService;

    @Inject
    SecurityIdentity securityIdentity;

    @POST
    @Path("/login")
    @PermitAll
    public Response login(AdminLoginBody body) {
        if (body == null || isBlank(body.email) || body.password == null || body.password.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "email and password are required"))
                    .build();
        }
        AdminAuthTokens tokens = adminAuthService.login(body.email, body.password);
        return Response.ok(toLoginBody(tokens)).build();
    }

    @POST
    @Path("/refresh")
    @PermitAll
    public Response refresh(AdminRefreshBody body) {
        if (body == null || isBlank(body.refreshToken)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "refreshToken is required"))
                    .build();
        }
        AdminAuthTokens tokens = adminAuthService.refresh(body.refreshToken);
        return Response.ok(toLoginBody(tokens)).build();
    }

    @POST
    @Path("/logout")
    @RolesAllowed("admin")
    public Response logout() {
        if (securityIdentity != null && !securityIdentity.isAnonymous()) {
            try {
                long userId = Long.parseLong(securityIdentity.getPrincipal().getName());
                adminAuthService.revokeAllRefreshTokens(userId);
            } catch (NumberFormatException ignored) {
                // subject no numérico
            }
        }
        return Response.ok(Map.of("message", "Admin logged out successfully")).build();
    }

    @GET
    @RolesAllowed("admin")
    @Path("/profile")
    public Response getProfile() {
        long userId = Long.parseLong(securityIdentity.getPrincipal().getName());
        AdminProfileDto profile = adminAuthService.profile(userId);
        return Response.ok(toAdminMap(profile)).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("/profile")
    public Response updateProfile(AdminProfileUpdateBody body) {
        long userId = Long.parseLong(securityIdentity.getPrincipal().getName());
        String name = body != null ? body.name : null;
        String email = body != null ? body.email : null;
        AdminProfileDto updated = adminAuthService.updateProfile(userId, name, email);
        return Response.ok(toAdminMap(updated)).build();
    }

    private static Map<String, Object> toLoginBody(AdminAuthTokens tokens) {
        return Map.of(
                "admin", toAdminMap(tokens.admin()),
                "token", tokens.token(),
                "refreshToken", tokens.refreshToken(),
                "expiresIn", tokens.expiresIn()
        );
    }

    private static Map<String, Object> toAdminMap(AdminProfileDto a) {
        return Map.of(
                "id", a.id(),
                "email", a.email(),
                "name", a.name(),
                "role", a.role(),
                "permissions", a.permissions(),
                "isActive", a.isActive(),
                "createdAt", a.createdAt(),
                "updatedAt", a.updatedAt()
        );
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdminLoginBody {
        public String email;
        public String password;
        public Boolean rememberMe;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdminRefreshBody {
        public String refreshToken;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdminProfileUpdateBody {
        public String name;
        public String email;
    }
}
