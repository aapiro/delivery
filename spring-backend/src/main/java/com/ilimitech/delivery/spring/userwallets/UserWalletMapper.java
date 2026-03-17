package com.ilimitech.delivery.spring.userwallets;

import com.ilimitech.delivery.spring.userwallets.dto.CreateUserWalletDto;
import com.ilimitech.delivery.spring.userwallets.dto.UpdateUserWalletDto;
import com.ilimitech.delivery.spring.userwallets.dto.UserWalletDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserWalletMapper {

    public UserWalletDto toDto(UserWallet w){
        if (w == null) return null;
        UserWalletDto d = new UserWalletDto();
        d.setId(w.getId());
        d.setUserId(w.getUserId());
        d.setBalance(w.getBalance());
        d.setCurrency(w.getCurrency());
        return d;
    }

    public UserWallet toEntity(CreateUserWalletDto d){
        if (d == null) return null;
        UserWallet w = new UserWallet();
        w.setUserId(d.getUserId());
        w.setBalance(d.getBalance());
        w.setCurrency(d.getCurrency());
        return w;
    }

    public UserWallet applyUpdate(UserWallet existing, UpdateUserWalletDto d){
        Optional.ofNullable(d.getBalance()).ifPresent(existing::setBalance);
        Optional.ofNullable(d.getCurrency()).ifPresent(existing::setCurrency);
        return existing;
    }
}

