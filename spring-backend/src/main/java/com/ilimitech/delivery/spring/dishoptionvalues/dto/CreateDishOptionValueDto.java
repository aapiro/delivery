package com.ilimitech.delivery.spring.dishoptionvalues.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateDishOptionValueDto {

    @NotNull
    private Long optionId;

    @NotBlank
    private String name;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal extraPrice;

    public CreateDishOptionValueDto() {}

    public Long getOptionId() { return optionId; }
    public void setOptionId(Long optionId) { this.optionId = optionId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getExtraPrice() { return extraPrice; }
    public void setExtraPrice(BigDecimal extraPrice) { this.extraPrice = extraPrice; }
}

