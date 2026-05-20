package com.example.CourseRegistration.Service;

import com.example.CourseRegistration.Entity.Role;
import com.example.CourseRegistration.Entity.User;
import com.example.CourseRegistration.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // REGISTER STUDENT
    public void registerStudent(User user) {

        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new RuntimeException("Student with this email already exists!");
        }

        // SET ROLE
        user.setRole(Role.STUDENT);

        userRepository.save(user);
    }

    // REGISTER ADMIN
    public User registerAdmin(User user) {

        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new RuntimeException("Admin with this email already exists!");
        }

        // SET ROLE
        user.setRole(Role.ADMIN);

        return userRepository.save(user);
    }

    // LOGIN
    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password!");
        }

        return user;
    }
}