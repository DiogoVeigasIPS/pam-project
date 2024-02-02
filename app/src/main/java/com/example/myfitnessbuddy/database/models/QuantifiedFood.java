package com.example.myfitnessbuddy.database.models;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.text.DecimalFormat;

@Entity(tableName = "quantifiedFood",
        foreignKeys = {
            @ForeignKey(
                    entity = Meal.class,
                    parentColumns = "id",
                    childColumns = "mealId",
                    onDelete = CASCADE
            )
        },
        indices = {@Index("mealId"), @Index("dishId")}
)
public class QuantifiedFood {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "quantity")
    private double quantity;
    @Nullable
    @ColumnInfo(name = "mealId")
    private Integer mealId;
    @Nullable
    @ColumnInfo(name = "dishId")
    private Integer dishId;
    @Embedded(prefix = "food_")
    private Food food;

    @Ignore
    public QuantifiedFood(double quantity) {
        setQuantity(quantity);
    }

    public QuantifiedFood(double quantity, Food food) {
        this(quantity);
        setFood(food);
    }

    // Getter methods
    public int getId(){
        return id;
    }

    public Food getFood(){
        return this.food;
    }

    public double getQuantity() {
        return quantity;
    }

    @Nullable
    public Integer getMealId() {
        return mealId;
    }

    @Nullable
    public Integer getDishId() {
        return dishId;
    }

    // Setter methods with data validation
    public void setId(int id){
        this.id = id;
    }
    public void setFood(Food food) {
        if (food == null) {
            throw new IllegalArgumentException("Food cannot be null");
        }
        this.food = food;
    }

    public void setQuantity(double quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.quantity = quantity;
    }

    public void setMealId(@Nullable Integer mealId) {
        this.mealId = mealId;
    }

    public void setDishId(@Nullable Integer dishId) {
        this.dishId = dishId;
    }

    // Methods
    public int getCalories() {
        if(food == null) throw new NullPointerException("Food is null");

        double caloriesPerUnit = food.getCaloriesPerPortionUnit();
        return (int) (caloriesPerUnit * quantity);
    }

    public String getCaloriesLabel(){
        return getCalories() + " kcal";
    }

    public String getPortionLabel(){
        return new DecimalFormat("#").format(quantity) + " " + food.getUnits();
    }

    public String detailsLabel(){
        return getCaloriesLabel() + " " + getPortionLabel();
    }

    public String getName(){
        return food.getName();
    }
}
