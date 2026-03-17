package com.ilimitech.delivery.spring.addresses;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long restaurantId;
    private String street;
    private String state;
    private String zipCode;
    private String country;

    @Column(name = "address_line", columnDefinition = "text")
    private String addressLine;

    private String city;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    private Boolean isVerified;
    private String placeId;

    @Column(columnDefinition = "text")
    private String formattedAddress;

    @Column(columnDefinition = "text")
    private String deliveryInstructions;

    private String addressType;
    private String floor;
    private String apartment;
    private Boolean isDefault;
}

