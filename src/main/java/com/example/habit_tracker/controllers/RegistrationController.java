package com.example.habit_tracker.controllers;

import com.example.habit_tracker.models.User;
import com.example.habit_tracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
    @GetMapping("/registration")
    public String showRegistration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("user", user);
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        // Check user in DB
        if (!userService.registerUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Clean user authentication
        SecurityContextHolder.clearContext();

        // Clean seance
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Logout from Google
        String logoutUrl = "https://www.google.com/accounts/Logout?continue=https://appengine.google.com/_ah/logout?continue=http://localhost:8080/";

        // Redirect user logout url Google
        return "redirect:" + logoutUrl;
    }
}
