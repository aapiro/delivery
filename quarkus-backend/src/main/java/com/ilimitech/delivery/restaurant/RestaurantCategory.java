package com.ilimitech.delivery.restaurant;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurant_categories")
public class RestaurantCategory {

    @EmbeddedId
    private RestaurantCategoryId id;

    public RestaurantCategory() {
        this.id = new RestaurantCategoryId();
    }

    public RestaurantCategory(Long restaurantId, Long categoryId) {
        this.id = new RestaurantCategoryId(restaurantId, categoryId);
    }

    public RestaurantCategoryId getId() {
        return id;
    }

    public void setId(RestaurantCategoryId id) {
        this.id = id;
    }

    @Embeddable
    public static class RestaurantCategoryId implements java.io.Serializable {

        @Column(name = "restaurant_id")
        private Long restaurantId;

        @Column(name = "category_id")
        private Long categoryId;

        public RestaurantCategoryId() {}

        public RestaurantCategoryId(Long restaurantId, Long categoryId) {
            this.restaurantId = restaurantId;
            this.categoryId = categoryId;
        }

        // Getters and setters
        public Long getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(Long restaurantId) {
            this.restaurantId = restaurantId;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RestaurantCategoryId)) return false;

            RestaurantCategoryId that = (RestaurantCategoryId) o;

            if (!restaurantId.equals(that.restaurantId)) return false;
            return categoryId.equals(that.categoryId);
        }

        @Override
        public int hashCode() {
            int result = restaurantId.hashCode();
            result = 31 * result + categoryId.hashCode();
            return result;
        }
    }
}