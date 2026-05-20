package com.example.CourseRegistration.Controller;

import com.example.CourseRegistration.Entity.Course;
import com.example.CourseRegistration.Entity.Role;
import com.example.CourseRegistration.Entity.User;

import com.example.CourseRegistration.Service.CourseService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // VIEW COURSES
    @GetMapping
    public String showCourses(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");

        if (user == null || user.getRole() != Role.ADMIN) {
            return "redirect:/auth/login";
        }

        model.addAttribute("adminName", user.getFullName());
        model.addAttribute("courses", courseService.getAllCourses());

        return "courses";
    }

    // ADD COURSE
    @PostMapping("/add")
    public String addCourse(@RequestParam String code,
                            @RequestParam String title,
                            @RequestParam int creditUnits) {

        Course course = new Course();
        course.setCode(code);
        course.setTitle(title);
        course.setCreditUnits(creditUnits);

        courseService.addCourse(course);

        return "redirect:/courses";
    }

    // DELETE COURSE
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);

        return "redirect:/courses";
    }
}