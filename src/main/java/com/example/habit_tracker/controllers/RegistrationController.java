package com.example.habit_tracker.controllers;

import com.example.habit_tracker.models.User;
import com.example.habit_tracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ControllerUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

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
        model.addAttribute("username", "");
        model.addAttribute("email", "");
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("user", user);
        // Проверка наличия ошибок валидации
        if (bindingResult.hasErrors()) {
            // Если есть ошибки, возвращаем пользователя на страницу регистрации
            return "registration";
        }

      

        // Проверка наличия пользователя в базе данных
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
}
