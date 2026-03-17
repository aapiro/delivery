package com.ilimitech.delivery.spring.users;

import com.ilimitech.delivery.spring.users.dto.CreateUserDto;
import com.ilimitech.delivery.spring.users.dto.UpdateUserDto;
import com.ilimitech.delivery.spring.users.dto.UserDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class UserMapper {

    public UserDto toDto(User u){
        if (u == null) return null;
        UserDto d = new UserDto();
        d.setId(u.getId());
        d.setEmail(u.getEmail());
        d.setPhone(u.getPhone());
        d.setFullName(u.getFullName());
        d.setUserType(u.getUserType());
        d.setIsVerified(u.getIsVerified());
        d.setIsActive(u.getIsActive());
        d.setCreatedAt(u.getCreatedAt());
        return d;
    }

    public User toEntity(CreateUserDto d){
        if (d == null) return null;
        User u = new User();
        u.setEmail(d.getEmail());
        u.setPhone(d.getPhone());
        u.setFullName(d.getFullName());
        u.setUserType(d.getUserType());
        u.setIsVerified(false);
        u.setIsActive(true);
        u.setCreatedAt(LocalDateTime.now());
        return u;
    }

    public User applyUpdate(User existing, UpdateUserDto d){
        Optional.ofNullable(d.getPhone()).ifPresent(existing::setPhone);
        Optional.ofNullable(d.getFullName()).ifPresent(existing::setFullName);
        Optional.ofNullable(d.getUserType()).ifPresent(existing::setUserType);
        Optional.ofNullable(d.getIsVerified()).ifPresent(existing::setIsVerified);
        Optional.ofNullable(d.getIsActive()).ifPresent(existing::setIsActive);
        return existing;
    }
}

