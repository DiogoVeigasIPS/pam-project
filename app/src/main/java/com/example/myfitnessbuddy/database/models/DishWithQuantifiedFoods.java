package com.example.myfitnessbuddy.database.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DishWithQuantifiedFoods implements FoodPreset {
    @Embedded
    private Dish dish;
    @Relation(
            parentColumn = "dishId",
            entityColumn = "quantifiedFoodId"
    )
    private List<QuantifiedFood> quantifiedFoods;

    public DishWithQuantifiedFoods(Dish dish, List<QuantifiedFood> quantifiedFoods) {
        this.dish = dish;
        this.quantifiedFoods = quantifiedFoods;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public List<QuantifiedFood> getQuantifiedFoods() {
        return quantifiedFoods;
    }

    public void setQuantifiedFoods(List<QuantifiedFood> quantifiedFoods) {
        this.quantifiedFoods = quantifiedFoods;
    }

    public int getCalories() {
        int calorieSum = 0;

        for(QuantifiedFood food : quantifiedFoods){
            calorieSum += food.getCalories();
        }

        return calorieSum;
    }

    public String getCaloriesLabel(){
        return getCalories() + " kcal";
    }

    @Override
    public String getCompoundName() {
        return dish.getCompoundName();
    }

    @Override
    public String getDetailsLabel(){
        return getCaloriesLabel();
    }

    @Override
    public int getIcon() {
        return dish.getIcon();
    }
}
