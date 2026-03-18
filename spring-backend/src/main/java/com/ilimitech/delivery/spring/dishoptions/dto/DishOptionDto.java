package com.ilimitech.delivery.spring.dishoptions.dto;

public class DishOptionDto {
    private Long id;
    private Long dishId;
    private String name;
    private Boolean required;

    public DishOptionDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getRequired() { return required; }
    public void setRequired(Boolean required) { this.required = required; }
}

