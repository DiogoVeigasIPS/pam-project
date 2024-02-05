package com.example.myfitnessbuddy.database.models.associatios;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "dishMealCrossRef", primaryKeys = {"dishId", "mealId"}, indices = {@Index("dishId"), @Index("mealId")})
public class DishMealCrossRef {
    private int dishId;
    private int mealId;

    public DishMealCrossRef(int dishId, int mealId) {
        this.dishId = dishId;
        this.mealId = mealId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }
}
