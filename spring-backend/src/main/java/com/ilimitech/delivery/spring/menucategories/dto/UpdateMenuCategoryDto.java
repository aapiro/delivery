package com.ilimitech.delivery.spring.menucategories.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateMenuCategoryDto {

    @NotBlank(message = "name must not be blank")
    private String name;

    private String slug;
    private String icon;
    private Integer displayOrder;
    private Boolean isActive;

    public UpdateMenuCategoryDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}

