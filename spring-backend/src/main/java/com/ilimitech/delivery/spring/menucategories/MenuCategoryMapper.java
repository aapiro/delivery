package com.ilimitech.delivery.spring.menucategories;

import com.ilimitech.delivery.spring.menucategories.dto.CreateMenuCategoryDto;
import com.ilimitech.delivery.spring.menucategories.dto.MenuCategoryDto;
import com.ilimitech.delivery.spring.menucategories.dto.UpdateMenuCategoryDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class MenuCategoryMapper {

    public MenuCategoryDto toDto(MenuCategory e) {
        if (e == null) return null;
        MenuCategoryDto dto = new MenuCategoryDto();
        dto.setId(e.getId());
        dto.setRestaurantId(e.getRestaurantId());
        dto.setName(e.getName());
        dto.setSlug(e.getSlug());
        dto.setIcon(e.getIcon());
        dto.setDisplayOrder(e.getDisplayOrder());
        dto.setIsActive(e.getIsActive());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }

    public MenuCategory toEntity(CreateMenuCategoryDto dto) {
        if (dto == null) return null;
        MenuCategory e = new MenuCategory();
        e.setRestaurantId(dto.getRestaurantId());
        e.setName(dto.getName());
        e.setSlug(dto.getSlug());
        e.setIcon(dto.getIcon());
        e.setDisplayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0);
        e.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        return e;
    }

    public MenuCategory applyUpdate(MenuCategory existing, UpdateMenuCategoryDto dto) {
        Optional.ofNullable(dto.getName()).ifPresent(existing::setName);
        Optional.ofNullable(dto.getSlug()).ifPresent(existing::setSlug);
        Optional.ofNullable(dto.getIcon()).ifPresent(existing::setIcon);
        Optional.ofNullable(dto.getDisplayOrder()).ifPresent(existing::setDisplayOrder);
        Optional.ofNullable(dto.getIsActive()).ifPresent(existing::setIsActive);
        existing.setUpdatedAt(LocalDateTime.now());
        return existing;
    }
}

