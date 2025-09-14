package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class SearchService {

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    DishRepository dishRepository;

    @Inject
    CategoryRepository categoryRepository;

    public List<Restaurant> searchRestaurants(String query, String category, Double minRating, Boolean isOpen) {
        // Implementación básica de búsqueda por nombre y categoría
        StringBuilder jpql = new StringBuilder("SELECT r FROM Restaurant r WHERE 1=1");
        if (query != null && !query.trim().isEmpty()) {
            jpql.append(" AND LOWER(r.name) LIKE LOWER(:query)");
        }
        if (category != null && !category.trim().isEmpty()) {
            // Esta implementación es simplificada, en una aplicación real se necesitaría
            // un join con la tabla de categorías o relación many-to-many
            jpql.append(" AND r.cuisine LIKE :category");
        }
        if (minRating != null) {
            jpql.append(" AND r.rating >= :minRating");
        }
        if (isOpen != null) {
            jpql.append(" AND r.isOpen = :isOpen");
        }

        jpql.append(" ORDER BY r.name");

        var queryBuilder = restaurantRepository.getEntityManager().createQuery(jpql.toString(), Restaurant.class);

        if (query != null && !query.trim().isEmpty()) {
            queryBuilder.setParameter("query", "%" + query + "%");
        }
        if (category != null && !category.trim().isEmpty()) {
            queryBuilder.setParameter("category", "%" + category + "%");
        }
        if (minRating != null) {
            queryBuilder.setParameter("minRating", minRating);
        }
        if (isOpen != null) {
            queryBuilder.setParameter("isOpen", isOpen);
        }

        return queryBuilder.getResultList();
    }

    public List<Dish> searchDishes(String query) {
        // Implementación básica de búsqueda por nombre
        StringBuilder jpql = new StringBuilder("SELECT d FROM Dish d WHERE LOWER(d.name) LIKE :query");
        var queryBuilder = dishRepository.getEntityManager().createQuery(jpql.toString(), Dish.class);
        queryBuilder.setParameter("query", "%" + query + "%");
        return queryBuilder.getResultList();
    }

    public List<Category> getRestaurantCategories() {
        // Devuelve todas las categorías disponibles
        return categoryRepository.listAll();
    }
}