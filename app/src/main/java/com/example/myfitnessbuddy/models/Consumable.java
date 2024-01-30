package com.example.myfitnessbuddy.models;

public interface Consumable {
    public void setType(String type);

    public String getType();

    public int getCalories();

    public String getName();

    public String detailsLabel();

}
