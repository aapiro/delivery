package com.ilimitech.delivery.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "addresses")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    @Column(name = "is_default")
    private Boolean isDefault;

    /**
     * Libre para UI (la tabla ya existe como `delivery_instructions`).
     * Se mapea con el campo `instructions` del frontend.
     */
    @Column(name = "delivery_instructions")
    private String instructions;

    /**
     * Etiqueta para UI (tabla: `address_type`).
     * Ej: HOME / WORK / OTHER
     */
    @Column(name = "address_type")
    private String addressType;
    
    // Constructors
    public Address() {}
    
    public Address(String street, String city, String state, String zipCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }
}