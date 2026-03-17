package com.ilimitech.delivery.spring.addresses;

import com.ilimitech.delivery.spring.addresses.dto.AddressDto;
import com.ilimitech.delivery.spring.addresses.dto.CreateAddressDto;
import com.ilimitech.delivery.spring.addresses.dto.UpdateAddressDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;
    private final AddressMapper mapper;

    public AddressServiceImpl(AddressRepository repository, AddressMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<AddressDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public AddressDto create(CreateAddressDto dto) {
        Address saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    @Override
    public AddressDto update(Long id, UpdateAddressDto dto) {
        return repository.findById(id)
                .map(existing -> mapper.applyUpdate(existing, dto))
                .map(repository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}

