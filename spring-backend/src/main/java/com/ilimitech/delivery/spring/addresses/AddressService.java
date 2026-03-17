package com.ilimitech.delivery.spring.addresses;

import com.ilimitech.delivery.spring.addresses.dto.AddressDto;
import com.ilimitech.delivery.spring.addresses.dto.CreateAddressDto;
import com.ilimitech.delivery.spring.addresses.dto.UpdateAddressDto;

import java.util.List;

public interface AddressService {
    List<AddressDto> findAll();
    AddressDto findById(Long id);
    AddressDto create(CreateAddressDto dto);
    AddressDto update(Long id, UpdateAddressDto dto);
    void delete(Long id);
}

