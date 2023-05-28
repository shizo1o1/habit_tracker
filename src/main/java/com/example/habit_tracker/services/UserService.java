package com.example.habit_tracker.services;

import com.example.habit_tracker.models.Role;
import com.example.habit_tracker.models.User;
import com.example.habit_tracker.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.util.StringUtils;

import java.util.Collections;
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

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

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
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }
}
