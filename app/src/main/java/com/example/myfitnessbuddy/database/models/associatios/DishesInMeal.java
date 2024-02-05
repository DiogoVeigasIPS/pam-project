package com.example.myfitnessbuddy.database.models.associatios;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.myfitnessbuddy.database.models.Dish;
import com.example.myfitnessbuddy.database.models.Meal;

import java.util.List;

public class DishesInMeal {
    @Embedded
    private Meal meal;

    @Relation(
            parentColumn = "mealId",
            entityColumn = "dishId",
            associateBy = @Junction(DishMealCrossRef.class)
    )
    private List<Dish> dishes;

    public DishesInMeal(Meal meal, List<Dish> dishes) {
        this.meal = meal;
        this.dishes = dishes;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}
