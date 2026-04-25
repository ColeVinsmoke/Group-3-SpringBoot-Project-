package com.example.Group_3;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Group3Controller {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("totalCredits", 0); // You can make this dynamic later

        return "dashboard";
    }

    // NEW: Logout endpoint
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate the session (clears user data)
        session.invalidate();
        // Redirect to login page
        return "redirect:/auth/login";
    }
}