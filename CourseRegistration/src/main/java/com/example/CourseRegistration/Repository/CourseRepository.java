package com.example.CourseRegistration.Repository;

import com.example.CourseRegistration.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // FIND BY COURSE CODE (existing)
    Optional<Course> findByCode(String code);

    //JPQL SEARCH (code OR title)
    @Query("SELECT c FROM Course c " +
            "WHERE LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchCourses(@Param("keyword") String keyword);
}