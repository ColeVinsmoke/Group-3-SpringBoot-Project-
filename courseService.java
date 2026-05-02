package com.example.registration.service;

import com.example.registration.entity.Course;
import com.example.registration.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // add a new course
    public Course addCourse(Course course) {
        // Ensure course code is unique
        if (courseRepository.findByCode(course.getCode()).isPresent()) {
            throw new RuntimeException("Course with this code already exists!");
        }
        return courseRepository.save(course);
    }

    // update an existing course
    public Course updateCourse(Long id, Course updatedCourse) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        course.setName(updatedCourse.getName());
        course.setCode(updatedCourse.getCode());
        course.setCreditUnits(updatedCourse.getCreditUnits());
        course.setCapacity(updatedCourse.getCapacity());

        return courseRepository.save(course);
    }

    // delete a course
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found!"));
        courseRepository.delete(course);
    }

    // view all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // view a single course by ID
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found!"));
    }

    // view a single course by code
    public Course getCourseByCode(String code) {
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Course not found!"));
    }
}
