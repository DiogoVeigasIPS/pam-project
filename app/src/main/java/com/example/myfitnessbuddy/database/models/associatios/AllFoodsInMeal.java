package com.example.myfitnessbuddy.database.models.associatios;

import com.example.myfitnessbuddy.database.models.Meal;
import com.example.myfitnessbuddy.database.models.QuantifiedFood;
import com.example.myfitnessbuddy.database.models.QuickAddition;

import java.util.List;

public class AllFoodsInMeal {
    private Meal meal;
    private List<QuantifiedFood> quantifiedFoodList;
    private List<QuickAddition> quickAdditions;
    private List<DishWithQuantifiedFoods> dishWithQuantifiedFoods;

    public AllFoodsInMeal(Meal meal, List<QuantifiedFood> quantifiedFoodList, List<QuickAddition> quickAdditions, List<DishWithQuantifiedFoods> dishWithQuantifiedFoods) {
        this.meal = meal;
        this.quantifiedFoodList = quantifiedFoodList;
        this.quickAdditions = quickAdditions;
        this.dishWithQuantifiedFoods = dishWithQuantifiedFoods;
    }

    public Meal getMeal() {
        return meal;
    }

    public List<QuantifiedFood> getQuantifiedFoodList() {
        return quantifiedFoodList;
    }

    public List<QuickAddition> getQuickAdditions() {
        return quickAdditions;
    }

    public List<DishWithQuantifiedFoods> getDishWithQuantifiedFoods() {
        return dishWithQuantifiedFoods;
    }
}
