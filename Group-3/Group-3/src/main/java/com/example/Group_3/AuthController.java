package com.example.Group_3;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String email,
                                  Model model) {

        boolean success = authService.registerUser(username, password, email);

        if (success) {
            model.addAttribute("message", "Registration successful! Please login.");
            return "redirect:/auth/login";
        } else {
            model.addAttribute("error", "Username or email already exists.");
            return "register";
        }
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        User user = authService.loginUser(username, password);

        if (user != null) {
            // Store user info in session, so we can access it on the dashboard
            session.setAttribute("currentUser", user);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }
    }
}