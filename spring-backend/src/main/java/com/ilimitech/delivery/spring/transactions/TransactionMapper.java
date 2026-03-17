package com.ilimitech.delivery.spring.transactions;

import com.ilimitech.delivery.spring.transactions.dto.CreateTransactionDto;
import com.ilimitech.delivery.spring.transactions.dto.TransactionDto;
import com.ilimitech.delivery.spring.transactions.dto.UpdateTransactionDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class TransactionMapper {

    public TransactionDto toDto(Transaction t){
        if (t == null) return null;
        TransactionDto d = new TransactionDto();
        d.setId(t.getId());
        d.setOrderId(t.getOrderId());
        d.setPaymentMethodId(t.getPaymentMethodId());
        d.setAmount(t.getAmount());
        d.setStatus(t.getStatus());
        d.setProviderTransactionId(t.getProviderTransactionId());
        d.setCreatedAt(t.getCreatedAt());
        return d;
    }

    public Transaction toEntity(CreateTransactionDto d){
        if (d == null) return null;
        Transaction t = new Transaction();
        t.setOrderId(d.getOrderId());
        t.setPaymentMethodId(d.getPaymentMethodId());
        t.setAmount(d.getAmount());
        t.setStatus(d.getStatus());
        t.setProviderTransactionId(d.getProviderTransactionId());
        t.setCreatedAt(LocalDateTime.now());
        return t;
    }

    public Transaction applyUpdate(Transaction existing, UpdateTransactionDto d){
        Optional.ofNullable(d.getOrderId()).ifPresent(existing::setOrderId);
        Optional.ofNullable(d.getPaymentMethodId()).ifPresent(existing::setPaymentMethodId);
        Optional.ofNullable(d.getAmount()).ifPresent(existing::setAmount);
        Optional.ofNullable(d.getStatus()).ifPresent(existing::setStatus);
        Optional.ofNullable(d.getProviderTransactionId()).ifPresent(existing::setProviderTransactionId);
        return existing;
    }
}

