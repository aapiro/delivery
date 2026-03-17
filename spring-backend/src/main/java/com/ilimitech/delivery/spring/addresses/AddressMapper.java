package com.ilimitech.delivery.spring.addresses;

import com.ilimitech.delivery.spring.addresses.dto.AddressDto;
import com.ilimitech.delivery.spring.addresses.dto.CreateAddressDto;
import com.ilimitech.delivery.spring.addresses.dto.UpdateAddressDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddressMapper {

    public AddressDto toDto(Address a) {
        if (a == null) return null;
        return AddressDto.builder()
                .id(a.getId())
                .userId(a.getUserId())
                .restaurantId(a.getRestaurantId())
                .street(a.getStreet())
                .state(a.getState())
                .zipCode(a.getZipCode())
                .country(a.getCountry())
                .addressLine(a.getAddressLine())
                .city(a.getCity())
                .latitude(a.getLatitude())
                .longitude(a.getLongitude())
                .isVerified(a.getIsVerified())
                .placeId(a.getPlaceId())
                .formattedAddress(a.getFormattedAddress())
                .deliveryInstructions(a.getDeliveryInstructions())
                .addressType(a.getAddressType())
                .floor(a.getFloor())
                .apartment(a.getApartment())
                .isDefault(a.getIsDefault())
                .build();
    }

    public Address toEntity(CreateAddressDto d) {
        if (d == null) return null;
        return Address.builder()
                .userId(d.getUserId())
                .restaurantId(d.getRestaurantId())
                .street(d.getStreet())
                .state(d.getState())
                .zipCode(d.getZipCode())
                .country(d.getCountry())
                .addressLine(d.getAddressLine())
                .city(d.getCity())
                .latitude(d.getLatitude())
                .longitude(d.getLongitude())
                .isVerified(d.getIsVerified())
                .placeId(d.getPlaceId())
                .formattedAddress(d.getFormattedAddress())
                .deliveryInstructions(d.getDeliveryInstructions())
                .addressType(d.getAddressType())
                .floor(d.getFloor())
                .apartment(d.getApartment())
                .isDefault(d.getIsDefault())
                .build();
    }

    public Address applyUpdate(Address existing, UpdateAddressDto d) {
        Optional.ofNullable(d.getUserId()).ifPresent(existing::setUserId);
        Optional.ofNullable(d.getRestaurantId()).ifPresent(existing::setRestaurantId);
        Optional.ofNullable(d.getStreet()).ifPresent(existing::setStreet);
        Optional.ofNullable(d.getState()).ifPresent(existing::setState);
        Optional.ofNullable(d.getZipCode()).ifPresent(existing::setZipCode);
        Optional.ofNullable(d.getCountry()).ifPresent(existing::setCountry);
        Optional.ofNullable(d.getAddressLine()).ifPresent(existing::setAddressLine);
        Optional.ofNullable(d.getCity()).ifPresent(existing::setCity);
        Optional.ofNullable(d.getLatitude()).ifPresent(existing::setLatitude);
        Optional.ofNullable(d.getLongitude()).ifPresent(existing::setLongitude);
        Optional.ofNullable(d.getIsVerified()).ifPresent(existing::setIsVerified);
        Optional.ofNullable(d.getPlaceId()).ifPresent(existing::setPlaceId);
        Optional.ofNullable(d.getFormattedAddress()).ifPresent(existing::setFormattedAddress);
        Optional.ofNullable(d.getDeliveryInstructions()).ifPresent(existing::setDeliveryInstructions);
        Optional.ofNullable(d.getAddressType()).ifPresent(existing::setAddressType);
        Optional.ofNullable(d.getFloor()).ifPresent(existing::setFloor);
        Optional.ofNullable(d.getApartment()).ifPresent(existing::setApartment);
        Optional.ofNullable(d.getIsDefault()).ifPresent(existing::setIsDefault);
        return existing;
    }
}

