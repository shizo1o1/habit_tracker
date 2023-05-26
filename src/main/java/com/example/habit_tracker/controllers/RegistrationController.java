package com.example.habit_tracker.controllers;

import com.example.habit_tracker.models.User;
import com.example.habit_tracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

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
        if (!userService.registerUser(user)){
            return "redirect:/registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activateUser(Model model, @PathVariable String code){
        boolean isActivated = userService.activateUser(code);

        if (isActivated){
            model.addAttribute("message", "User successfully activated");
        }
        else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}
