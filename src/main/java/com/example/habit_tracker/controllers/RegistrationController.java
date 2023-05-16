package com.example.habit_tracker.controllers;
import com.example.habit_tracker.models.User;
import com.example.habit_tracker.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
     @GetMapping("/registration")
    public String showRegistration() {
        return "registration";
    }


    @PostMapping("/registration")
    public String registerUser(User user) {
        user.setActive(true);
        user.setRoles("USER");
        userRepository.save(user);
        return "redirect:/login";
    }
}
