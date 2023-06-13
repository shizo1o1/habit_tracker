package com.example.habit_tracker.controllers;

import com.example.habit_tracker.models.Habit;
import com.example.habit_tracker.models.User;
import com.example.habit_tracker.repo.HabitRepository;
import com.example.habit_tracker.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;


@Controller
public class HabitController {
    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private  UserRepository userRepository;

    @GetMapping("/")
    public String home(Principal principal, Model model) {
        Iterable<Habit> habits = habitRepository.findAll();
        model.addAttribute("habit", habits);
        if ( principal != null){
            String username = principal.getName();
            model.addAttribute("username", username);
        }
        return "index";
    }

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
    public String editProfile(Principal principal, BindingResult bindingResult, @RequestParam String username, @RequestParam String email){
        if (bindingResult.hasErrors()) {
            // Если есть ошибки, возвращаем пользователя на страницу регистрации
            return "/profile/edit";
        }

        String authorizedUser = principal.getName();
        User user = userRepository.findByUsername(authorizedUser);

        user.setUsername(username);
        user.setEmail(email);

        userRepository.save(user);
        return ("redirect:/");
    }

    @GetMapping("/add-habit")
    public String showAddHabit (Model model){
        return "add-habit";
    }

    @PostMapping("/add-habit")
    public String postAddHabit(@RequestParam String name,@RequestParam String description, @RequestParam String dateStart, @RequestParam String dateFinish, @RequestParam int target, @RequestParam int frequency){
        Habit habit = new Habit(name, description, dateStart, dateFinish, target, frequency);
        habitRepository.save(habit);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String detailsHabit(@PathVariable(value = "id") long id, Model model){
        if (!habitRepository.existsById(id)){
            return ("redirect:/");
        }
        Optional<Habit> habit =habitRepository.findById(id);
        ArrayList<Habit> result =new ArrayList<>();
        habit.ifPresent(result::add);
        model.addAttribute("habit", result);
        return "details-habit";
    }

    @GetMapping("/{id}/edit")
    public String editHabit(@PathVariable(value = "id") long id, Model model){
        if (!habitRepository.existsById(id)){
            return ("redirect:/");
        }
        Optional <Habit> product = habitRepository.findById(id);
        ArrayList<Habit> result = new ArrayList<>();
        product.ifPresent(result::add);
        model.addAttribute("habit", result);
        return "edit-habit";
    }

    @PostMapping("/{id}/edit")
    public String postEditHabit(@RequestParam String name, @RequestParam String description, @RequestParam int target,@RequestParam int frequency, @RequestParam String dateStart, @RequestParam String dateFinish, @PathVariable(value = "id") long id,  Model model){
        if (!habitRepository.existsById(id)){
            return ("redirect:/");
        }
        Habit habit = habitRepository.findById(id).orElseThrow();
        habit.setName(name);
        habit.setDescription(description);
        habit.setTarget(target);
        habit.setFrequency(frequency);
        habit.setDateStart(dateStart);
        habit.setDateFinish(dateFinish);
        habitRepository.save(habit);
        return "redirect:/";
    }
    @PostMapping("/{id}/remove")
    public String removeProduct(@PathVariable(value = "id") long id){
        Habit habit = habitRepository.findById(id).orElseThrow();
        habitRepository.delete(habit);
        return "redirect:/";
    }

}
