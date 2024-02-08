package com.example.myfitnessbuddy.database.models;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.database.models.associatios.ListableFood;

@Entity(tableName = "quickAddition",
        foreignKeys = @ForeignKey(
                entity = Meal.class,
                parentColumns = "mealId",
                childColumns = "mealId",
                onDelete = CASCADE
        ), indices = {@Index("mealId")}
)
public class QuickAddition implements ListableFood {
    @PrimaryKey(autoGenerate = true)
    private int quickAdditionId;
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
    public int getQuickAdditionId() {
        return quickAdditionId;
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
    public void setQuickAdditionId(int quickAdditionId){
        this.quickAdditionId = quickAdditionId;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name.trim();
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

    @Override
    public String getCompoundName() {
        return name;
    }

    @Override
    public String getDetailsLabel() {
        return detailsLabel();
    }

    @Override
    public int getIcon() {
        return R.drawable.flatware;
    }

    @Override
    public int getId() {
        return quickAdditionId;
    }

    @Override
    public String getUnits() {
        return null;
    }
}
