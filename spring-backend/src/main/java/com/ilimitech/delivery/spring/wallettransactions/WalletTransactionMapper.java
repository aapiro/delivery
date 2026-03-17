package com.ilimitech.delivery.spring.wallettransactions;

import com.ilimitech.delivery.spring.wallettransactions.dto.CreateWalletTransactionDto;
import com.ilimitech.delivery.spring.wallettransactions.dto.UpdateWalletTransactionDto;
import com.ilimitech.delivery.spring.wallettransactions.dto.WalletTransactionDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class WalletTransactionMapper {

    public WalletTransactionDto toDto(WalletTransaction w){
        if (w == null) return null;
        WalletTransactionDto d = new WalletTransactionDto();
        d.setId(w.getId());
        d.setWalletId(w.getWalletId());
        d.setType(w.getType());
        d.setAmount(w.getAmount());
        d.setOrderId(w.getOrderId());
        d.setDescription(w.getDescription());
        d.setCreatedAt(w.getCreatedAt());
        return d;
    }

    public WalletTransaction toEntity(CreateWalletTransactionDto d){
        if (d == null) return null;
        WalletTransaction w = new WalletTransaction();
        w.setWalletId(d.getWalletId());
        w.setType(d.getType());
        w.setAmount(d.getAmount());
        w.setOrderId(d.getOrderId());
        w.setDescription(d.getDescription());
        w.setCreatedAt(LocalDateTime.now());
        return w;
    }

    public WalletTransaction applyUpdate(WalletTransaction existing, UpdateWalletTransactionDto d){
        Optional.ofNullable(d.getType()).ifPresent(existing::setType);
        Optional.ofNullable(d.getAmount()).ifPresent(existing::setAmount);
        Optional.ofNullable(d.getOrderId()).ifPresent(existing::setOrderId);
        Optional.ofNullable(d.getDescription()).ifPresent(existing::setDescription);
        return existing;
    }
}

