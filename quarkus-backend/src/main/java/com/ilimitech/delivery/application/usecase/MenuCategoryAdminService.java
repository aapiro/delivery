package com.ilimitech.delivery.application.usecase;

import com.ilimitech.delivery.common.NotFoundException;
import com.ilimitech.delivery.application.port.out.DishRepository;
import com.ilimitech.delivery.application.port.out.RestaurantRepository;
import com.ilimitech.delivery.infrastructure.adapter.in.rest.dto.AdminMenuCategoryResponse;
import com.ilimitech.delivery.infrastructure.adapter.in.rest.dto.AdminMenuCategoryWriteDto;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.MenuCategory;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.RestaurantEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ApplicationScoped
public class MenuCategoryAdminService {

    private static final Pattern NON_SLUG = Pattern.compile("[^a-z0-9]+");

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    DishRepository dishRepository;

    public List<AdminMenuCategoryResponse> listByRestaurant(Long restaurantId) {
        if (restaurantId == null) {
            return List.of();
        }
        List<MenuCategory> list = MenuCategory.list("restaurant.id = ?1", restaurantId);
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public AdminMenuCategoryResponse create(Long restaurantId, AdminMenuCategoryWriteDto dto) {
        if (dto == null || dto.name == null || dto.name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new NotFoundException("Restaurant not found");
        }
        MenuCategory mc = new MenuCategory();
        mc.restaurant = restaurant;
        mc.name = dto.name.trim();
        mc.slug = slugOrDefault(dto.slug, dto.name);
        mc.displayOrder = dto.displayOrder != null ? dto.displayOrder : 0;
        mc.persist();
        return toResponse(mc);
    }

    @Transactional
    public AdminMenuCategoryResponse update(Long restaurantId, Long categoryId, AdminMenuCategoryWriteDto dto) {
        MenuCategory mc = MenuCategory.findById(categoryId);
        if (mc == null || mc.getRestaurant() == null || !Objects.equals(mc.getRestaurant().getId(), restaurantId)) {
            throw new NotFoundException("Menu category not found");
        }
        if (dto.name != null && !dto.name.isBlank()) {
            mc.name = dto.name.trim();
        }
        if (dto.slug != null && !dto.slug.isBlank()) {
            mc.slug = dto.slug.trim().toLowerCase(Locale.ROOT);
        }
        if (dto.displayOrder != null) {
            mc.displayOrder = dto.displayOrder;
        }
        return toResponse(mc);
    }

    @Transactional
    public void delete(Long restaurantId, Long categoryId) {
        MenuCategory mc = MenuCategory.findById(categoryId);
        if (mc == null || mc.getRestaurant() == null || !Objects.equals(mc.getRestaurant().getId(), restaurantId)) {
            throw new NotFoundException("Menu category not found");
        }
        long dishes = dishRepository.find("category.id = ?1", categoryId).count();
        if (dishes > 0) {
            throw new IllegalStateException("Cannot delete a menu category that still has dishes assigned");
        }
        mc.delete();
    }

    private AdminMenuCategoryResponse toResponse(MenuCategory c) {
        AdminMenuCategoryResponse r = new AdminMenuCategoryResponse();
        r.id = c.id;
        r.restaurantId = c.getRestaurant() != null ? c.getRestaurant().getId() : null;
        r.name = c.name;
        r.slug = c.slug;
        r.displayOrder = c.displayOrder;
        return r;
    }

    private static String slugOrDefault(String slug, String name) {
        if (slug != null && !slug.isBlank()) {
            return slug.trim().toLowerCase(Locale.ROOT);
        }
        return slugify(name);
    }

    private static String slugify(String input) {
        if (input == null) {
            return "";
        }
        String n = Normalizer.normalize(input.trim(), Normalizer.Form.NFD);
        n = n.replaceAll("\\p{M}+", "");
        n = n.toLowerCase(Locale.ROOT);
        n = NON_SLUG.matcher(n).replaceAll("-");
        n = n.replaceAll("^-|-$", "");
        return n.isEmpty() ? "categoria" : n;
    }
}
