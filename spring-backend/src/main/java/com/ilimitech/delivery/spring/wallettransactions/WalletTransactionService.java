package com.ilimitech.delivery.spring.wallettransactions;

import com.ilimitech.delivery.spring.wallettransactions.dto.CreateWalletTransactionDto;
import com.ilimitech.delivery.spring.wallettransactions.dto.UpdateWalletTransactionDto;
import com.ilimitech.delivery.spring.wallettransactions.dto.WalletTransactionDto;

import java.util.List;

public interface WalletTransactionService {
    List<WalletTransactionDto> findAll();
    WalletTransactionDto findById(Long id);
    WalletTransactionDto create(CreateWalletTransactionDto dto);
    WalletTransactionDto update(Long id, UpdateWalletTransactionDto dto);
    void delete(Long id);
}

