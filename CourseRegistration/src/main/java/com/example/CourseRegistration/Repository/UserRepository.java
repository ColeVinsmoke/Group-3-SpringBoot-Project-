package com.example.CourseRegistration.Repository;

import com.example.CourseRegistration.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
