package com.ilimitech.delivery.spring.userwallets;

import com.ilimitech.delivery.spring.userwallets.dto.CreateUserWalletDto;
import com.ilimitech.delivery.spring.userwallets.dto.UpdateUserWalletDto;
import com.ilimitech.delivery.spring.userwallets.dto.UserWalletDto;

import java.util.List;

public interface UserWalletService {
    List<UserWalletDto> findAll();
    UserWalletDto findById(Long id);
    UserWalletDto create(CreateUserWalletDto dto);
    UserWalletDto update(Long id, UpdateUserWalletDto dto);
    void delete(Long id);
}

