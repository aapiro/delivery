package com.ilimitech.delivery.spring.addresses.dto;

import java.math.BigDecimal;

public class UpdateAddressDto {
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

    public UpdateAddressDto() {}

    public UpdateAddressDto(Long userId, Long restaurantId, String street, String state, String zipCode, String country, String addressLine, String city, BigDecimal latitude, BigDecimal longitude, Boolean isVerified, String placeId, String formattedAddress, String deliveryInstructions, String addressType, String floor, String apartment, Boolean isDefault) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.street = street;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.addressLine = addressLine;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isVerified = isVerified;
        this.placeId = placeId;
        this.formattedAddress = formattedAddress;
        this.deliveryInstructions = deliveryInstructions;
        this.addressType = addressType;
        this.floor = floor;
        this.apartment = apartment;
        this.isDefault = isDefault;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long userId;
        private Long restaurantId;
        private String street;
        private String state;
        private String zipCode;
        private String country;
        private String addressLine;
        private String city;
        private java.math.BigDecimal latitude;
        private java.math.BigDecimal longitude;
        private Boolean isVerified;
        private String placeId;
        private String formattedAddress;
        private String deliveryInstructions;
        private String addressType;
        private String floor;
        private String apartment;
        private Boolean isDefault;

        public Builder userId(Long userId){ this.userId = userId; return this; }
        public Builder restaurantId(Long restaurantId){ this.restaurantId = restaurantId; return this; }
        public Builder street(String street){ this.street = street; return this; }
        public Builder state(String state){ this.state = state; return this; }
        public Builder zipCode(String zipCode){ this.zipCode = zipCode; return this; }
        public Builder country(String country){ this.country = country; return this; }
        public Builder addressLine(String addressLine){ this.addressLine = addressLine; return this; }
        public Builder city(String city){ this.city = city; return this; }
        public Builder latitude(java.math.BigDecimal latitude){ this.latitude = latitude; return this; }
        public Builder longitude(java.math.BigDecimal longitude){ this.longitude = longitude; return this; }
        public Builder isVerified(Boolean isVerified){ this.isVerified = isVerified; return this; }
        public Builder placeId(String placeId){ this.placeId = placeId; return this; }
        public Builder formattedAddress(String formattedAddress){ this.formattedAddress = formattedAddress; return this; }
        public Builder deliveryInstructions(String deliveryInstructions){ this.deliveryInstructions = deliveryInstructions; return this; }
        public Builder addressType(String addressType){ this.addressType = addressType; return this; }
        public Builder floor(String floor){ this.floor = floor; return this; }
        public Builder apartment(String apartment){ this.apartment = apartment; return this; }
        public Builder isDefault(Boolean isDefault){ this.isDefault = isDefault; return this; }

        public UpdateAddressDto build(){ return new UpdateAddressDto(userId,restaurantId,street,state,zipCode,country,addressLine,city,latitude,longitude,isVerified,placeId,formattedAddress,deliveryInstructions,addressType,floor,apartment,isDefault); }
    }

    // Getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public java.math.BigDecimal getLatitude() { return latitude; }
    public void setLatitude(java.math.BigDecimal latitude) { this.latitude = latitude; }
    public java.math.BigDecimal getLongitude() { return longitude; }
    public void setLongitude(java.math.BigDecimal longitude) { this.longitude = longitude; }
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    public String getPlaceId() { return placeId; }
    public void setPlaceId(String placeId) { this.placeId = placeId; }
    public String getFormattedAddress() { return formattedAddress; }
    public void setFormattedAddress(String formattedAddress) { this.formattedAddress = formattedAddress; }
    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }
    public String getAddressType() { return addressType; }
    public void setAddressType(String addressType) { this.addressType = addressType; }
    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }
    public String getApartment() { return apartment; }
    public void setApartment(String apartment) { this.apartment = apartment; }
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
}

