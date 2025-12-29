package com.ilimitech.delivery.application.usecase;

import com.ilimitech.delivery.common.NotFoundException;
import com.ilimitech.delivery.application.port.out.RestaurantRepository;
import com.ilimitech.delivery.infrastructure.adapter.in.rest.generated.model.PagedRestaurantResponse;
import com.ilimitech.delivery.infrastructure.adapter.in.rest.generated.model.Restaurant;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.CuisineType;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.MenuCategory;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.RestaurantEntity;
import com.ilimitech.delivery.infrastructure.mapper.RestaurantMapper;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class RestaurantService {

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    RestaurantMapper restaurantMapper;
    
    public PagedRestaurantResponse getRestaurants(int page, int size) {
        // 1. Supongamos que usas Panache para la consulta
        PanacheQuery<RestaurantEntity> query = restaurantRepository.findAll().page(page, size);

        List<RestaurantEntity> entities = query.list();
        long totalElements = query.count();
        int totalPages = query.pageCount();

        // 2. Creamos la respuesta de tu esquema OpenAPI
        PagedRestaurantResponse response = new PagedRestaurantResponse();

        // 3. Usamos MapStruct para convertir la lista de entidades a DTOs
        response.setContent(restaurantMapper.toSummaryList(entities));

        // 4. Llenamos los metadatos de paginación
        response.setTotalElements(totalElements);
        response.setTotalPages(totalPages);
        response.setSize(size);
        response.setNumber(page);

        return response;
    }

    public PagedRestaurantResponse getAllRestaurantsFiltered(String name, String cuisine, Boolean isOpen, int page, int size) {
        // Get all restaurants first
        PanacheQuery<RestaurantEntity> query = restaurantRepository.findAll().page(page, size);

        long totalElements = query.count();
        int totalPages = query.pageCount();

        List<RestaurantEntity> restaurantEntities = query.list();
        // Apply filters if provided
        if (name != null && !name.isBlank()) {
            restaurantEntities = restaurantEntities.stream()
                .filter(r -> r.getName() != null && r.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }

        if (cuisine != null && !cuisine.isBlank()) {
            restaurantEntities = restaurantEntities.stream()
                    .filter(r -> r.getCuisines() != null && r.getCuisines().stream()
                            .anyMatch(c -> c.name.equalsIgnoreCase(cuisine)))
                    .collect(Collectors.toList());
        }
        
        if (isOpen != null) {
            restaurantEntities = restaurantEntities.stream()
                .filter(r -> r.getIsOpen() == isOpen)
                .collect(Collectors.toList());
        }

        PagedRestaurantResponse response = new PagedRestaurantResponse();

        // 3. Usamos MapStruct para convertir la lista de entidades a DTOs
        response.setContent(restaurantMapper.toSummaryList(restaurantEntities));

        // 4. Llenamos los metadatos de paginación
        response.setTotalElements(totalElements);
        response.setTotalPages(totalPages);
        response.setSize(size);
        response.setNumber(page);

        return response;
    }

    @Transactional
    public RestaurantEntity addRestaurant(RestaurantEntity restaurantEntity) {
        validateRestaurantData(restaurantEntity);
        restaurantRepository.persist(restaurantEntity);
        return restaurantEntity;
    }

    public Restaurant getRestaurantById(Long id) {
        RestaurantEntity restaurantEntityById = restaurantRepository.findById(id);
        return restaurantMapper.toRestaurant(restaurantEntityById);
    }

    @Transactional
    public RestaurantEntity updateRestaurant(Long id, Restaurant restaurant) {
        RestaurantEntity existing = restaurantRepository.findById(id);
        if (existing == null) {
            return null;
        }

        existing.setName(restaurant.getName());
//        CuisineType cuisineType = new CuisineType();
//        existing.setCuisines(Set.of(cuisineType));

        BigDecimal updatedRating = restaurant.getRating();
        if (updatedRating != null) {
            if (updatedRating.compareTo(BigDecimal.ZERO) < 0 || updatedRating.compareTo(new BigDecimal("5.0")) > 0) {
                throw new IllegalArgumentException("Rating must be between 0 and 5");
            }
            existing.setRating(updatedRating.setScale(1, RoundingMode.HALF_UP));
        }

        String description = restaurant.getDescription();
        if (description != null) {
            existing.setDescription(description);
        }

        Integer deliveryTimeMin = restaurant.getDeliveryTimeMin();
        if (deliveryTimeMin != null) {
            existing.setDeliveryTimeMin(deliveryTimeMin);
        }

        Integer deliveryTimeMax = restaurant.getDeliveryTimeMax();
        if (deliveryTimeMax != null) {
            existing.setDeliveryTimeMax(deliveryTimeMax);
        }

        BigDecimal deliveryFee = restaurant.getDeliveryFee();
        if (deliveryFee != null) {
            existing.setDeliveryFee(deliveryFee);
        }

        BigDecimal minimumOrder = restaurant.getMinimumOrder();
        if (minimumOrder != null) {
            existing.setMinimumOrder(minimumOrder);
        }

        boolean isOpen = restaurant.getIsOpen();
        existing.setIsOpen(isOpen);

        String imageUrl = restaurant.getImageUrl().toString();
        if (imageUrl != null) {
            existing.setImageUrl(imageUrl);
        }
        return existing;
    }

    @Transactional
    public Restaurant toggleRestaurantStatus(Long id) {
        Restaurant restaurant = getRestaurantById(id);
        restaurant.setIsOpen(!restaurant.getIsOpen());
        return restaurant;
    }

    @Transactional
    public void deleteRestaurant(Long id) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id);
        if (restaurantEntity == null) {
            throw new NotFoundException("Restaurant with ID " + id + " not found");
        }
        restaurantRepository.delete(restaurantEntity);
    }
    
    /**
     * Get all categories for restaurants
     */
    public List<MenuCategory> getAllCategories() {
        return restaurantRepository.listAll().stream()
                .flatMap(r -> r.categories.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Search restaurants by name or cuisine
     */
    public List<RestaurantEntity> searchRestaurants(String name, String cuisine) {
        List<RestaurantEntity> restaurantEntities = restaurantRepository.listAll();
        
        // Apply filters if provided
        if (name != null && !name.isEmpty()) {
            restaurantEntities = restaurantEntities.stream()
                .filter(r -> r.getName() != null && r.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (cuisine != null && !cuisine.isEmpty()) {
            restaurantEntities = restaurantEntities.stream()
                .filter(r -> r.getCuisines() != null && r.getCuisines().contains(cuisine.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return restaurantEntities;
    }

    private void validateRestaurantData(RestaurantEntity restaurant) {
        if (restaurant.getName() == null || restaurant.getName().isBlank()) {
            throw new IllegalArgumentException("Restaurant name is required");
        }

        BigDecimal rating = restaurant.getRating();
        if (rating != null && (rating.compareTo(BigDecimal.ZERO) < 0 || rating.compareTo(new BigDecimal("5.0")) > 0)) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        if (restaurant.getDeliveryTimeMin() != null && restaurant.getDeliveryTimeMax() != null
                && restaurant.getDeliveryTimeMin() > restaurant.getDeliveryTimeMax()) {
            throw new IllegalArgumentException("Minimum delivery time cannot be greater than maximum");
        }

        if (restaurant.getDeliveryFee() != null && restaurant.getDeliveryFee().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Delivery fee cannot be negative");
        }
    }
}