package com.example.habit_tracker.controllers;

import com.example.habit_tracker.models.User;
import com.example.habit_tracker.repo.UserRepository;
import com.example.habit_tracker.services.EmailSender;
import com.example.habit_tracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

@Controller
public class UserProfileController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfile(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping ("/profile/edit")
    public String showEditProfile(Principal principal, Model model){
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String editProfile(Principal principal, @Validated User updatedUser, BindingResult bindingResult) {
        System.out.println("ERROR = " + bindingResult);
        if (bindingResult.hasErrors()) {
            return "profile-edit";
        }
        userService.editProfile( principal.getName(),updatedUser);

        return "redirect:/";
    }
}
