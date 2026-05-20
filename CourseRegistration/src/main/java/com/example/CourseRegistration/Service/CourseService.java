package com.example.CourseRegistration.Service;

import com.example.CourseRegistration.Entity.Course;
import com.example.CourseRegistration.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
        @Autowired
        private CourseRepository courseRepository;

        // add a new course
        public void addCourse(Course course) {
            // Ensure course code is unique
            if (courseRepository.findByCode(course.getCode()).isPresent()) {
                throw new RuntimeException("Course with this code already exists!");
            }
            courseRepository.save(course);
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


    public List<Course> searchCourses(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return getAllCourses();
        }

        return courseRepository.searchCourses(keyword);
    }
}

