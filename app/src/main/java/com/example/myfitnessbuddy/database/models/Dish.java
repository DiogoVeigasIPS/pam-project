/*package com.example.myfitnessbuddy.database.models;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "dish",
        foreignKeys = @ForeignKey(
                entity = Meal.class,
                parentColumns = "id",
                childColumns = "mealId",
                onDelete = CASCADE),
        indices = @Index("mealId")
)
public class Dish {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "icon")
    private int icon;
    @ColumnInfo(name = "mealId")
    private int mealId;
    @Ignore
    private List<QuantifiedFood> foods;

    @Ignore
    public Dish(String name, String description, int icon) {
        setName(name);
        setDescription(description);
        setIcon(icon);
    }

    public Dish(String name, String description, int icon, List<QuantifiedFood> foods) {
        this(name, description, icon);
        this.foods = foods;
    }

    // Getter methods
    public int getId() {
        return id;
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

    public List<QuantifiedFood> getFoods() {
        return foods;
    }

    // Setter methods
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

    // Methods
    public int getCalories() {
        int calorieSum = 0;

        for (QuantifiedFood food : foods) {
            calorieSum += food.getCalories();
        }

        return calorieSum;
    }

    public String getCaloriesLabel() {
        return getCalories() + " kcal";
    }

    public String detailsLabel() {
        return getCaloriesLabel();
    }
}
*/