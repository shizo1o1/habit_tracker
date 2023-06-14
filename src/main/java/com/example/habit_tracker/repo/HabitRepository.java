package com.example.habit_tracker.repo;

import com.example.habit_tracker.models.Habit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HabitRepository extends CrudRepository<Habit, Long> {
    List<Habit> findByUserId(Long userId);
}
