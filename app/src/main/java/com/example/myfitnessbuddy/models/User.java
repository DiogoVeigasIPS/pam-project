package com.example.myfitnessbuddy.models;

import java.time.LocalDate;

public class User {
    private LocalDate birthDate;
    private Integer height;
    private Goal goal;
    private Activity activity;

    public User() {
        birthDate = null;
        height = null;
        goal = Goal.NOT_DEFINED;
        activity = Activity.NOT_DEFINED;
    }

    public User(LocalDate birthDate, Integer height, Goal goal, Activity activity) {
        this();
        setBirthDate(birthDate);
        setHeight(height);
        setGoal(goal);
        setActivity(activity);
    }

    // Getter methods
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Integer getHeight() {
        return height;
    }

    public Goal getGoal() {
        return goal;
    }

    public Activity getActivity() {
        return activity;
    }

    // Setter methods
    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        this.birthDate = birthDate;
    }

    public void setHeight(Integer height) {
        if (height == null || height <= 0) {
            throw new IllegalArgumentException("Height must be a positive integer");
        }
        this.height = height;
    }

    public void setGoal(Goal goal) {
        if (goal == null) {
            throw new IllegalArgumentException("Goal cannot be null");
        }
        this.goal = goal;
    }

    public void setActivity(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null");
        }
        this.activity = activity;
    }

    // Methods
    public int getAge(){
        return LocalDate.now().getYear() - birthDate.getYear();
    }
}

