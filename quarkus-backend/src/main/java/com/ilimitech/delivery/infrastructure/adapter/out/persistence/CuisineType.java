package com.ilimitech.delivery.infrastructure.adapter.out.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cuisine_types")
public class CuisineType extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true, nullable = false)
    public String name;

    @ManyToMany(mappedBy = "cuisines")
    @JsonIgnore
    public Set<RestaurantEntity> restaurantEntities = new HashSet<>();
}