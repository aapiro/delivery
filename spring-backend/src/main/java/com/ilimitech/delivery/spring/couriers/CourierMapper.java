package com.ilimitech.delivery.spring.couriers;

import com.ilimitech.delivery.spring.couriers.dto.CourierDto;
import com.ilimitech.delivery.spring.couriers.dto.CreateCourierDto;
import com.ilimitech.delivery.spring.couriers.dto.UpdateCourierDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CourierMapper {

    public CourierDto toDto(Courier e) {
        if (e == null) return null;
        CourierDto dto = new CourierDto();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setVehicleType(e.getVehicleType());
        dto.setUserId(e.getUserId());
        dto.setLicenseNumber(e.getLicenseNumber());
        dto.setLicenseExpiry(e.getLicenseExpiry());
        dto.setVehiclePlate(e.getVehiclePlate());
        dto.setCurrentLatitude(e.getCurrentLatitude());
        dto.setCurrentLongitude(e.getCurrentLongitude());
        dto.setIsOnline(e.getIsOnline());
        dto.setCurrentOrdersCount(e.getCurrentOrdersCount());
        dto.setIsActive(e.getIsActive());
        return dto;
    }

    public Courier toEntity(CreateCourierDto dto) {
        if (dto == null) return null;
        Courier e = new Courier();
        e.setName(dto.getName());
        e.setVehicleType(dto.getVehicleType());
        e.setUserId(dto.getUserId());
        e.setLicenseNumber(dto.getLicenseNumber());
        e.setLicenseExpiry(dto.getLicenseExpiry());
        e.setVehiclePlate(dto.getVehiclePlate());
        e.setCurrentLatitude(dto.getCurrentLatitude());
        e.setCurrentLongitude(dto.getCurrentLongitude());
        e.setIsOnline(dto.getIsOnline() != null ? dto.getIsOnline() : false);
        e.setCurrentOrdersCount(dto.getCurrentOrdersCount() != null ? dto.getCurrentOrdersCount() : 0);
        e.setIsActive(dto.getIsActive());
        return e;
    }

    public Courier applyUpdate(Courier existing, UpdateCourierDto dto) {
        Optional.ofNullable(dto.getName()).ifPresent(existing::setName);
        Optional.ofNullable(dto.getVehicleType()).ifPresent(existing::setVehicleType);
        Optional.ofNullable(dto.getUserId()).ifPresent(existing::setUserId);
        Optional.ofNullable(dto.getLicenseNumber()).ifPresent(existing::setLicenseNumber);
        Optional.ofNullable(dto.getLicenseExpiry()).ifPresent(existing::setLicenseExpiry);
        Optional.ofNullable(dto.getVehiclePlate()).ifPresent(existing::setVehiclePlate);
        Optional.ofNullable(dto.getCurrentLatitude()).ifPresent(existing::setCurrentLatitude);
        Optional.ofNullable(dto.getCurrentLongitude()).ifPresent(existing::setCurrentLongitude);
        Optional.ofNullable(dto.getIsOnline()).ifPresent(existing::setIsOnline);
        Optional.ofNullable(dto.getCurrentOrdersCount()).ifPresent(existing::setCurrentOrdersCount);
        Optional.ofNullable(dto.getIsActive()).ifPresent(existing::setIsActive);
        return existing;
    }
}

