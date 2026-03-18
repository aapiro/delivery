package com.ilimitech.delivery.spring.dishoptionvalues;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "dish_option_values")
public class DishOptionValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long optionId;
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal extraPrice;

    public DishOptionValue() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOptionId() { return optionId; }
    public void setOptionId(Long optionId) { this.optionId = optionId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getExtraPrice() { return extraPrice; }
    public void setExtraPrice(BigDecimal extraPrice) { this.extraPrice = extraPrice; }
}

