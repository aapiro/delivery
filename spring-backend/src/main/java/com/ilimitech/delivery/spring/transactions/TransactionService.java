package com.ilimitech.delivery.spring.transactions;

import com.ilimitech.delivery.spring.transactions.dto.CreateTransactionDto;
import com.ilimitech.delivery.spring.transactions.dto.TransactionDto;
import com.ilimitech.delivery.spring.transactions.dto.UpdateTransactionDto;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> findAll();
    TransactionDto findById(Long id);
    TransactionDto create(CreateTransactionDto dto);
    TransactionDto update(Long id, UpdateTransactionDto dto);
    void delete(Long id);
}

