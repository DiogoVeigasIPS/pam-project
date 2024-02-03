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
import androidx.room.RoomWarnings;

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
    private int quantifiedFoodId;
    @ColumnInfo(name = "quantity")
    private double quantity;
    @Nullable
    @ColumnInfo(name = "mealId")
    private Integer mealId;
    @Nullable
    @ColumnInfo(name = "dishId")
    private Integer dishId;
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded(prefix = "food_")
    private Food food;
    @ColumnInfo(name = "calculatedCalories")
    private int calculatedCalories;

    public QuantifiedFood(double quantity, Food food) {
        setFood(food);
        setQuantity(quantity);
    }

    // Getter methods
    public int getQuantifiedFoodId(){
        return quantifiedFoodId;
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

    public int getCalculatedCalories() {
        return calculatedCalories;
    }

    @Nullable
    public Integer getDishId() {
        return dishId;
    }

    // Setter methods with data validation
    public void setQuantifiedFoodId(int id){
        this.quantifiedFoodId = id;
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
        calculateCalories();
    }

    public void setMealId(@Nullable Integer mealId) {
        this.mealId = mealId;
    }

    public void setDishId(@Nullable Integer dishId) {
        this.dishId = dishId;
    }

    public void setCalculatedCalories(int calculatedCalories) {
        this.calculatedCalories = calculatedCalories;
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

    public void calculateCalories() {
        if (food == null) return;

        double caloriesPerUnit = food.getCaloriesPerPortionUnit();
        calculatedCalories = (int) (caloriesPerUnit * quantity);
    }
}
