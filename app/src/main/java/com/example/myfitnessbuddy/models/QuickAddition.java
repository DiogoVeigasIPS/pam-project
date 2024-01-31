package com.example.myfitnessbuddy.models;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "quickAddition",
        foreignKeys = @ForeignKey(
                entity = Meal.class,
                parentColumns = "id",
                childColumns = "mealId",
                onDelete = CASCADE
        ), indices = {@Index("mealId")}
)
public class QuickAddition{
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "calories")
    private int calories;
    @ColumnInfo(name = "mealId")
    private int mealId;

    @Ignore
    public QuickAddition(String name, int calories) {
        setName(name);
        setCalories(calories);
    }

    public QuickAddition(String name, int calories, int mealId) {
        this(name, calories);
        setMealId(mealId);
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

    public int getMealId() {
        return mealId;
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

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }
}
