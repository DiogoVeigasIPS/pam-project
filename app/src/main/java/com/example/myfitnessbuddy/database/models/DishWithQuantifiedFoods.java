package com.example.myfitnessbuddy.database.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DishWithQuantifiedFoods implements FoodPreset {
    @Embedded
    public Dish dish;
    @Relation(
            parentColumn = "dishId",
            entityColumn = "quantifiedFoodId"
    )
    public List<QuantifiedFood> quantifiedFoods;

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
        return String.format("%s %s", dish.getName(), dish.getDescription());
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
