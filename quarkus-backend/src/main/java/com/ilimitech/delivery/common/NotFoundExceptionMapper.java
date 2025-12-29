package com.ilimitech.delivery.common;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Context
    UriInfo uriInfo; // Esto inyecta la información de la URL actual

    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "error", "Recurso no encontrado",
                        "path", uriInfo.getPath(), // Muestra la ruta (ej: /api/admin/error)
                        "full_url", uriInfo.getRequestUri().toString(), // URL completa
                        "method", exception.getResponse().toString() // Método HTTP (opcional)
                ))
                .build();
    }
}