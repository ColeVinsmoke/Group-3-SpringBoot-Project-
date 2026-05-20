package com.example.CourseRegistration.Repository;

import com.example.CourseRegistration.Entity.Course;
import com.example.CourseRegistration.Entity.Registration;
import com.example.CourseRegistration.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    Optional<Registration> findByUserAndCourse(User user, Course course);

    List<Registration> findByUser(User user);

}