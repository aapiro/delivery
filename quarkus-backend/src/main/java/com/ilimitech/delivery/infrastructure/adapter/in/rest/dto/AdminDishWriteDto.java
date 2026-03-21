package com.ilimitech.delivery.infrastructure.adapter.in.rest.dto;

import java.math.BigDecimal;

/**
 * Cuerpo JSON para alta/edición de platos desde el panel admin (sin problemas de referencias JPA/Jackson).
 */
public class AdminDishWriteDto {
    public String name;
    public String description;
    public BigDecimal price;
    public String imageUrl;
    public Boolean isAvailable;
    public Long restaurantId;
    public Long categoryId;
}
