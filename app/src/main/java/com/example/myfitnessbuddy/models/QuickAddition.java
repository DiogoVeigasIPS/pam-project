package com.example.myfitnessbuddy.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class QuickAddition implements Consumable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "calories")
    private int calories;
    @ColumnInfo(name = "type")
    private String type;

    public QuickAddition(String name, int calories) {
        setName(name);
        setCalories(calories);
        setType(getClass().getSimpleName());
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
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

    @Override
    public void setType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }
}
