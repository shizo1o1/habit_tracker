package com.example.habit_tracker.controllers;

import com.example.habit_tracker.models.Role;
import com.example.habit_tracker.models.User;
import com.example.habit_tracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
import java.util.Collections;

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

    @GetMapping("/login/google/callback")
    public String handleGoogleCallback(@AuthenticationPrincipal OAuth2User oauth2User) {
        // Получение информации о пользователе из объекта OAuth2User
        String username = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");

        // Создание экземпляра пользователя
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);

        // Сохранение пользователя в базу данных
        userService.registerUser(user);

        // Дополнительные действия после сохранения пользователя

        return "redirect:/";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Очищаем аутентификацию пользователя
        SecurityContextHolder.clearContext();

        // Очищаем сеанс пользователя
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Выполняем выход пользователя из Google
        String logoutUrl = "https://www.google.com/accounts/Logout?continue=https://appengine.google.com/_ah/logout?continue=http://localhost:8080/";

        // Перенаправляем пользователя на URL-адрес выхода из Google
        return "redirect:" + logoutUrl;
    }
}
