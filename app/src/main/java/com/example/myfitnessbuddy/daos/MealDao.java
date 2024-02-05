package com.example.myfitnessbuddy.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.myfitnessbuddy.database.models.associatios.DishMealCrossRef;
import com.example.myfitnessbuddy.database.models.associatios.DishesInMeal;
import com.example.myfitnessbuddy.database.models.Meal;

import java.util.List;

@Dao
public interface MealDao {
    @Query("SELECT * FROM meal")
    List<Meal> getAll();

    @Query("SELECT * from meal WHERE mealId = :mealId LIMIT 1")
    Meal findById(int mealId);

    @Query("SELECT COALESCE(SUM(COALESCE(qa.calories, 0) + COALESCE(qf.calculatedCalories, 0)), 0) AS totalCalories " +
            "FROM meal m " +
            "LEFT JOIN quickAddition qa ON qa.mealId = m.mealId " +
            "LEFT JOIN quantifiedFood qf ON qf.mealId = m.mealId " +
            "WHERE m.mealId = :mealId " +
            "GROUP BY m.mealId")
    int getCalories(int mealId);

    @Transaction
    @Query("SELECT * from meal")
    List<DishesInMeal> getDishesInMeals();

    @Transaction
    @Query("SELECT * from meal WHERE mealId = :mealId")
    DishesInMeal getDishesInMeal(int mealId);

    @Query("SELECT COUNT(*) FROM dishMealCrossRef WHERE mealId = :mealId AND dishId = :dishId")
    int dishIsDuplicateInMeal(int mealId, int dishId);

    @Insert
    void insertDishInMeal(DishMealCrossRef dishMealCrossRef);

    @Delete
    void removeDishFromMeal(DishMealCrossRef dishMealCrossRef);

    @Insert
    void insert(Meal... meals);

    @Update
    void update(Meal meal);

    @Delete
    void delete(Meal meal);
}
