package com.ilimitech.delivery.spring.userwallets;

import com.ilimitech.delivery.spring.userwallets.dto.CreateUserWalletDto;
import com.ilimitech.delivery.spring.userwallets.dto.UpdateUserWalletDto;
import com.ilimitech.delivery.spring.userwallets.dto.UserWalletDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserWalletServiceImpl implements UserWalletService {

    private final UserWalletRepository repository;
    private final UserWalletMapper mapper;

    public UserWalletServiceImpl(UserWalletRepository repository, UserWalletMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<UserWalletDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserWalletDto findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    @Override
    public UserWalletDto create(CreateUserWalletDto dto) {
        UserWallet saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    @Override
    public UserWalletDto update(Long id, UpdateUserWalletDto dto) {
        return repository.findById(id).map(existing -> mapper.applyUpdate(existing, dto)).map(repository::save).map(mapper::toDto).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}

