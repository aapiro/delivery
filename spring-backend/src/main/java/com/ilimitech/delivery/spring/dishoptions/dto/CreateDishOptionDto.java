package com.ilimitech.delivery.spring.dishoptions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateDishOptionDto {

    @NotNull
    private Long dishId;

    @NotBlank
    private String name;

    private Boolean required;

    public CreateDishOptionDto() {}

    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getRequired() { return required; }
    public void setRequired(Boolean required) { this.required = required; }
}

