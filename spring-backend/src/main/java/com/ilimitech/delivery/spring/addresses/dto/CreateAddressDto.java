package com.ilimitech.delivery.spring.addresses.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAddressDto {
    private Long userId;
    private Long restaurantId;
    private String street;
    private String state;
    private String zipCode;
    private String country;
    private String addressLine;
    private String city;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean isVerified;
    private String placeId;
    private String formattedAddress;
    private String deliveryInstructions;
    private String addressType;
    private String floor;
    private String apartment;
    private Boolean isDefault;
}

