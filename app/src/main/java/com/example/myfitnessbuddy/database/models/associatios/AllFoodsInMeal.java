package com.example.myfitnessbuddy.database.models.associatios;

import com.example.myfitnessbuddy.database.models.Meal;
import com.example.myfitnessbuddy.database.models.QuantifiedFood;
import com.example.myfitnessbuddy.database.models.QuickAddition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AllFoodsInMeal{
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

    public int getCalorieSum(){
        int calorieSum = 0;

        for(DishWithQuantifiedFoods dishWithQuantifiedFoods : getDishWithQuantifiedFoods()){
            calorieSum += dishWithQuantifiedFoods.getCalories();
        }

        for(QuantifiedFood quantifiedFood : getQuantifiedFoodList()){
            calorieSum += quantifiedFood.getCalories();
        }

        for(QuickAddition quickAddition : getQuickAdditions()){
            calorieSum += quickAddition.getCalories();
        }

        return calorieSum;
    }
    
    public List<ListableFood> getListableFoods(){
        List<ListableFood> listableFoods = new ArrayList<>();
        listableFoods.addAll(getQuantifiedFoodList());
        listableFoods.addAll(getQuickAdditions());
        listableFoods.addAll(getDishWithQuantifiedFoods());

        return listableFoods;
    }

    public List<ListableFood> getListableFoods(String search) {
        List<ListableFood> listableFoods = new ArrayList<>();
        listableFoods.addAll(getQuantifiedFoodList());
        listableFoods.addAll(getQuickAdditions());
        listableFoods.addAll(getDishWithQuantifiedFoods());

        Iterator<ListableFood> iterator = listableFoods.iterator();
        while (iterator.hasNext()) {
            ListableFood listableFood = iterator.next();
            if (!listableFood.getCompoundName().contains(search)) {
                iterator.remove();
            }
        }

        return listableFoods;
    }
}
