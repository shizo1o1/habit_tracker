package com.example.habit_tracker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Calendar;

@Entity
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name, description;

    private int target,  frequency;

    Calendar dateStart, dateFinish;
}
