package com.example.myfitnessbuddy.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quickAddition")
public class QuickAddition{

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "calories")
    private int calories;

    public QuickAddition(String name, int calories) {
        setName(name);
        setCalories(calories);
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    // Setter methods
    public void setId(int id){
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setCalories(int calories) {
        if (calories < 0) {
            throw new IllegalArgumentException("Calories cannot be negative");
        }
        this.calories = calories;
    }

    public String getCaloriesLabel(){
        return calories + " kcal";
    }

    public String detailsLabel(){
        return getCaloriesLabel();
    }
}
