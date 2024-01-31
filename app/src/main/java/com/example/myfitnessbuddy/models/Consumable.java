package com.example.myfitnessbuddy.models;

public interface Consumable {
    void setType(String type);

    String getType();

    int getCalories();

    String getName();

    String detailsLabel();

}
