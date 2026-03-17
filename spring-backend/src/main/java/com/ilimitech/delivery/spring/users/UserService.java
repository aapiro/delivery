package com.ilimitech.delivery.spring.users;

import com.ilimitech.delivery.spring.users.dto.CreateUserDto;
import com.ilimitech.delivery.spring.users.dto.UpdateUserDto;
import com.ilimitech.delivery.spring.users.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();
    UserDto findById(Long id);
    UserDto create(CreateUserDto dto);
    UserDto update(Long id, UpdateUserDto dto);
    void delete(Long id);
}

