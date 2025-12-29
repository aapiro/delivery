package com.ilimitech.delivery.infrastructure.adapter.out.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "restaurants")
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_open")
    private Boolean isOpen;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    public List<MenuCategory> categories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "restaurant_cuisines",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "cuisine_id"))
    public Set<CuisineType> cuisines = new HashSet<>();

    @Column(precision = 3, scale = 1)
    private BigDecimal rating;

    @Column(name = "review_count")
    private int reviewCount;

    @Column(name = "delivery_time_min")
    private Integer deliveryTimeMin;

    @Column(name = "delivery_time_max")
    private Integer deliveryTimeMax;

    @Column(name = "delivery_fee", precision = 5, scale = 2)
    private BigDecimal deliveryFee;

    @Column(name = "minimum_order", precision = 6, scale = 2)
    private BigDecimal minimumOrder;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DishEntity> dishes;

    public void setRating(BigDecimal rating) {
        if (rating != null) {
            // Validate rating is between 0 and 5
            if (rating.compareTo(BigDecimal.ZERO) < 0 || rating.compareTo(new BigDecimal("5.0")) > 0) {
                throw new IllegalArgumentException("Rating must be between 0 and 5");
            }
            // Round to 1 decimal place
            this.rating = rating.setScale(1, RoundingMode.HALF_UP);
        } else {
            this.rating = null;
        }
    }

}