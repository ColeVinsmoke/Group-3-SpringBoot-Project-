package com.example.CourseRegistration.Controller;

import com.example.CourseRegistration.Entity.Course;
import com.example.CourseRegistration.Entity.Role;
import com.example.CourseRegistration.Entity.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.CourseRegistration.Service.CourseService;
import com.example.CourseRegistration.Service.RegistrationService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CourseRegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private CourseService courseService;


    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String keyword, HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");

        // CHECK IF USER LOGGED IN
        if (user == null) {
            return "redirect:/auth/login";
        }

        // ONLY STUDENTS CAN ACCESS
        if (user.getRole() != Role.STUDENT) {
            return "redirect:/auth/login";
        }

        List<Course> courses;

        if (keyword != null && !keyword.isEmpty()) {

            courses = courseService.searchCourses(keyword);

        } else {

            courses = courseService.getAllCourses();
        }

        // GET REGISTERED COURSES
        List<Course> myCourses = registrationService.getUserCourses(user.getId());

        // GET TOTAL CREDITS
        int totalCredits =
                registrationService.getTotalCredits(user.getId());

        model.addAttribute("username", user.getFullName());

        // ALL AVAILABLE COURSES
        model.addAttribute("courses",
                courseService.getAllCourses());

        // REGISTERED COURSES
        model.addAttribute("myCourses", myCourses);

        model.addAttribute("totalCredits", totalCredits);

        model.addAttribute("courseCount", myCourses.size());

        model.addAttribute("creditStatus",
                (totalCredits < 15) ? "BELOW LIMIT" : (totalCredits > 24) ? "ABOVE LIMIT" : "OK");

        return "dashboard";
    }



    @GetMapping("/register/{courseId}")
    public String registerCourse(@PathVariable Long courseId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            return "redirect:/auth/login";
        }

        try {

            registrationService.registerCourse(user.getId(), courseId);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Course enrolled successfully!"
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }


        return "redirect:/dashboard#courses";
    }

    @GetMapping("/drop/{courseId}")
    public String dropCourse(@PathVariable Long courseId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            return "redirect:/auth/login";
        }

        try {

            registrationService.dropCourse(
                    user.getId(),
                    courseId
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Course dropped successfully!"
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/dashboard#mycourses";
    }


    // ADMIN DASHBOARD
    @GetMapping("/adminDashboard")
    public String adminDashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");

        // CHECK IF USER LOGGED IN
        if (user == null) {
            return "redirect:/auth/login";
        }

        // ONLY ADMINS CAN ACCESS
        if (user.getRole() != Role.ADMIN) {
            return "redirect:/auth/login";
        }

        // GET COURSE DATA
        long totalCourses =
                courseService.getAllCourses().size();

        // TEMPORARY
        long activeCourses = totalCourses;

        model.addAttribute("adminName",
                user.getFullName());

        model.addAttribute("totalCourses",
                totalCourses);

        model.addAttribute("activeCourses",
                activeCourses);

        return "adminDashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/auth/login";
    }
}