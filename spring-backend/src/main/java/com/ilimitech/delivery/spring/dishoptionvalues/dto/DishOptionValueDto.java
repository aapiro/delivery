package com.ilimitech.delivery.spring.dishoptionvalues.dto;

import java.math.BigDecimal;

public class DishOptionValueDto {
    private Long id;
    private Long optionId;
    private String name;
    private BigDecimal extraPrice;

    public DishOptionValueDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOptionId() { return optionId; }
    public void setOptionId(Long optionId) { this.optionId = optionId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getExtraPrice() { return extraPrice; }
    public void setExtraPrice(BigDecimal extraPrice) { this.extraPrice = extraPrice; }
}

