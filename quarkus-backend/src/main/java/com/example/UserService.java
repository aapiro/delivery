package com.example;

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
        // This would typically get the current authenticated user from JWT or session
        return new User("John Doe", "john@example.com");
    }
}