package com.example.CourseRegistration.Controller;

import com.example.CourseRegistration.Entity.Role;
import com.example.CourseRegistration.Entity.User;
import com.example.CourseRegistration.Service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // SHOW LOGIN PAGE
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // SHOW REGISTER PAGE
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // PROCESS REGISTRATION
    @PostMapping("/register")
    public String processRegister(@RequestParam String fullName,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  Model model) {

        try {

            User user = new User();

            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(password);

            // REGISTER AS STUDENT
            userService.registerStudent(user);

            model.addAttribute("message",
                    "Registration successful! Please login.");

            return "redirect:/auth/login";

        } catch (RuntimeException e) {

            model.addAttribute("error", e.getMessage());

            return "register";
        }
    }

    // PROCESS LOGIN
    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        try {
            User user =
                    userService.loginUser(email, password);

            // SAVE USER IN SESSION
            session.setAttribute("currentUser", user);

            // CHECK ROLE
            if (user.getRole() == Role.ADMIN) {
                return "redirect:/adminDashboard";

            } else if (user.getRole() == Role.STUDENT) {
                return "redirect:/dashboard";
            }

            return "redirect:/";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
}