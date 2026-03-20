package com.ilimitech.delivery.application.usecase;

import com.ilimitech.delivery.application.port.out.UserRepository;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class UserService {
    
    @Inject
    UserRepository userRepository;
    
    // Placeholder for user service implementation
    public List<User> getAllUsers() {
        return userRepository.listAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public User getCurrentUser() {
        return userRepository.listAll().stream().findFirst().orElse(null);
    }
}