package com.ilimitech.delivery.spring.wallettransactions;

import com.ilimitech.delivery.spring.wallettransactions.dto.CreateWalletTransactionDto;
import com.ilimitech.delivery.spring.wallettransactions.dto.UpdateWalletTransactionDto;
import com.ilimitech.delivery.spring.wallettransactions.dto.WalletTransactionDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository repository;
    private final WalletTransactionMapper mapper;

    public WalletTransactionServiceImpl(WalletTransactionRepository repository, WalletTransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<WalletTransactionDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public WalletTransactionDto findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    @Override
    public WalletTransactionDto create(CreateWalletTransactionDto dto) {
        WalletTransaction saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    @Override
    public WalletTransactionDto update(Long id, UpdateWalletTransactionDto dto) {
        return repository.findById(id).map(existing -> mapper.applyUpdate(existing, dto)).map(repository::save).map(mapper::toDto).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}

