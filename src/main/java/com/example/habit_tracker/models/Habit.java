package com.example.habit_tracker.models;

import javax.persistence.*;

@Entity
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String name, description, dateStart, dateFinish;
    private int target,  frequency;
    
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Habit() {
    }

    public Habit(String name, String description, String dateStart, String dateFinish, int target, int frequency) {
        this.name = name;
        this.description = description;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.target = target;
        this.frequency = frequency;
    }
}
