package com.example.Group_3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public boolean registerUser(String username, String password, String email) {
        // Check if user exists
        if (userRepository.existsByUsername(username)) {
            return false; // Username taken
        }
        if (userRepository.existsByEmail(email)) {
            return false; // Email taken
        }

        // Create new user
        User user = new User(username, password, email);
        userRepository.save(user);
        return true;
    }

    public User loginUser(String username, String password) {
        // Find user by username
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password)) // Simple password check
                .orElse(null); // Return null if password doesn't match
    }
}