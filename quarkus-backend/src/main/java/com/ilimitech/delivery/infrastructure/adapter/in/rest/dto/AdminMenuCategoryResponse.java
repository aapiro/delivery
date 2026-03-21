package com.ilimitech.delivery.infrastructure.adapter.in.rest.dto;

/**
 * Respuesta JSON con {@code restaurantId} explícito (evita {@code @JsonBackReference} en la entidad).
 */
public class AdminMenuCategoryResponse {
    public Long id;
    public Long restaurantId;
    public String name;
    public String slug;
    public int displayOrder;
}
