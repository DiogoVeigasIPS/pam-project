package com.example.myfitnessbuddy.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dish")
public class Dish {
    @PrimaryKey(autoGenerate = true)
    private int dishId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "icon")
    private int icon;
    @ColumnInfo(name = "calculatedCalories")
    private int calculatedCalories;

    public Dish(String name, String description, int icon) {
        setName(name);
        setDescription(description);
        setIcon(icon);
    }

    // Getter methods
    public int getDishId() {
        return dishId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIcon() {
        return icon;
    }

    public int getCalculatedCalories(){
        return this.calculatedCalories;
    }

    // Setter methods
    public void setDishId(int id) {
        this.dishId = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setCalculatedCalories(int calculatedCalories) {
        this.calculatedCalories = calculatedCalories;
    }
}