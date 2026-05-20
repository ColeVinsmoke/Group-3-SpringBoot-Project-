package com.example.CourseRegistration.Service;

import com.example.CourseRegistration.Entity.Course;
import com.example.CourseRegistration.Entity.Registration;
import com.example.CourseRegistration.Entity.User;
import com.example.CourseRegistration.Repository.CourseRepository;
import com.example.CourseRegistration.Repository.RegistrationRepository;
import com.example.CourseRegistration.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Registration registerCourse(Long userId, Long courseId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        // prevent duplicate registration
        if (registrationRepository.findByUserAndCourse(user, course).isPresent()) {
            throw new RuntimeException("Already registered for this course!");
        }

        // credit limit check (example: 18 units max)
        int currentCredits = getTotalCredits(userId);

        if (currentCredits + course.getCreditUnits() > 18) {
            throw new RuntimeException("Credit limit exceeded!");
        }

        Registration reg = new Registration();
        reg.setUser(user);
        reg.setCourse(course);

        return registrationRepository.save(reg);
    }

        public void dropCourse(Long userId, Long courseId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        Registration reg = registrationRepository.findByUserAndCourse(user, course)
                .orElseThrow(() -> new RuntimeException("Registration not found!"));

        registrationRepository.delete(reg);
    }


    public List<Course> getUserCourses(Long userId) {

        return registrationRepository.findByUser(
                        userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found!"))
                )
                .stream()
                .map(Registration::getCourse)
                .toList();
    }


    public int getTotalCredits(Long userId) {

        return getUserCourses(userId)
                .stream()
                .mapToInt(Course::getCreditUnits)
                .sum();
    }
}