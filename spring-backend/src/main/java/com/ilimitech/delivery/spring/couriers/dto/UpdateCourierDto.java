package com.ilimitech.delivery.spring.couriers.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateCourierDto {

    @NotBlank(message = "name must not be blank")
    private String name;

    private String vehicleType;
    private Long userId;
    private String licenseNumber;
    private LocalDate licenseExpiry;
    private String vehiclePlate;
    private BigDecimal currentLatitude;
    private BigDecimal currentLongitude;
    private Boolean isOnline;
    private Integer currentOrdersCount;
    private Boolean isActive;

    public UpdateCourierDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public LocalDate getLicenseExpiry() { return licenseExpiry; }
    public void setLicenseExpiry(LocalDate licenseExpiry) { this.licenseExpiry = licenseExpiry; }

    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }

    public BigDecimal getCurrentLatitude() { return currentLatitude; }
    public void setCurrentLatitude(BigDecimal currentLatitude) { this.currentLatitude = currentLatitude; }

    public BigDecimal getCurrentLongitude() { return currentLongitude; }
    public void setCurrentLongitude(BigDecimal currentLongitude) { this.currentLongitude = currentLongitude; }

    public Boolean getIsOnline() { return isOnline; }
    public void setIsOnline(Boolean isOnline) { this.isOnline = isOnline; }

    public Integer getCurrentOrdersCount() { return currentOrdersCount; }
    public void setCurrentOrdersCount(Integer currentOrdersCount) { this.currentOrdersCount = currentOrdersCount; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}

