package com.ashok.it.dockerprojectintegeration.Controller;

import com.ashok.it.dockerprojectintegeration.Model.User;
import com.ashok.it.dockerprojectintegeration.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, 
                               @RequestParam String confirmPassword,
                               Model model) {
        
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            model.addAttribute("user", user);
            return "register";
        }

        if (user.getPassword().length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters!");
            model.addAttribute("user", user);
            return "register";
        }

        try {
            user.setRole(User.Role.USER); // Default role for new registrations
            userService.createUser(user);
            model.addAttribute("success", "Registration successful! Please login with your credentials.");
            return "register";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "register";
        }
    }
}
