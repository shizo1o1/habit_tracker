package com.example.habit_tracker.services;

import com.example.habit_tracker.models.Role;
import com.example.habit_tracker.models.User;
import com.example.habit_tracker.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private EmailSender emailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registration")
    public boolean registerUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null){
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getUsername() + " "+ user.getEmail());

        userRepository.save(user);
        if (!StringUtils.isEmpty(user.getEmail())){
            String message = String.format(
                    "Hello, %s!\n"+
                            "Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            emailSender.sendMail(user.getEmail(), "Activation code", message) ;
        }

        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null){
            return false;
        }
        user.setActive(true);
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    @PostMapping("/profile/edit")
    public boolean editProfile(String authorizedUser,  User updatedUser) {
        User user = userRepository.findByUsername(authorizedUser);

        if (!Objects.equals(user.getEmail(), updatedUser.getEmail())){
            user.setActive(false);
            user.setActivationCode(UUID.randomUUID().toString());
            user.setEmail(updatedUser.getEmail());
            String message = String.format(
                    "Hello, %s!\n"+
                            "Please, confirm email change: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            emailSender.sendMail(user.getEmail(), "Activation code", message) ;
            SecurityContextHolder.clearContext();
        }

        if (!Objects.equals(user.getUsername(), updatedUser.getUsername())){
            user.setUsername(updatedUser.getUsername());
            SecurityContextHolder.clearContext();
        }

        userRepository.save(user);
        return true;
    }
    @GetMapping("/login/google/callback")
    public String handleGoogleCallback(@AuthenticationPrincipal OAuth2User oauth2User) {
        // get user data from OAuth2User
        String username = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");
        System.out.println(username + " "+ email);

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);

        registerUser(user);


        return "redirect:/";
    }
}
