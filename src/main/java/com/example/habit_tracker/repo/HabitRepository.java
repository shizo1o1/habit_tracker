package com.example.habit_tracker.repo;

import com.example.habit_tracker.models.Habit;
import org.springframework.data.repository.CrudRepository;

public interface HabitRepository extends CrudRepository<Habit, Long> {
}
