package com.example.myfitnessbuddy.database.models;

import com.example.myfitnessbuddy.database.models.enums.Activity;
import com.example.myfitnessbuddy.database.models.enums.Goal;

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

    public int calculateCalorieGoal(int weight) {
        if (birthDate == null || height == 0 || weight == 0 || goal == Goal.NOT_DEFINED || activity == Activity.NOT_DEFINED) {
            throw new IllegalStateException("User attributes are not fully defined");
        }

        // Calculate age
        int age = getAge();

        // Calculate BMR (Basal Metabolic Rate) using Mifflin-St Jeor Equation
        double bmr;
        if (goal == Goal.LOSE) {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        } else if (goal == Goal.GAIN) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 161;
        } else { // Goal.MAINTAIN
            bmr = 10 * weight + 6.25 * height - 5 * age;
        }

        // Adjust BMR based on activity level
        double calorieGoal;
        switch (activity) {
            case NOT_ACTIVE:
                calorieGoal = bmr * 1.2;
                break;
            case SLIGHTLY_ACTIVE:
                calorieGoal = bmr * 1.375;
                break;
            case ACTIVE:
                calorieGoal = bmr * 1.55;
                break;
            case VERY_ACTIVE:
                calorieGoal = bmr * 1.725;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + activity);
        }

        return (int) calorieGoal;
    }
}

