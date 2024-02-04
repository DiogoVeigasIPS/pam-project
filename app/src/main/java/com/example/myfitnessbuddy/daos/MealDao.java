package com.example.myfitnessbuddy.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfitnessbuddy.database.models.Meal;

import java.util.List;

@Dao
public interface MealDao {
    @Query("SELECT * FROM meal")
    List<Meal> getAll();

    @Query("SELECT * from meal WHERE id = :id LIMIT 1")
    Meal findById(int id);

    @Query("SELECT COALESCE(SUM(qa.calories), 0) + COALESCE(SUM(qf.calculatedCalories), 0) " +
            "FROM quickAddition qa " +
            "LEFT JOIN quantifiedFood qf ON qa.mealId = qf.mealId " +
            "WHERE qa.mealId = :mealId")
    int getCalories(int mealId);

    @Insert
    void insert(Meal... meals);

    @Update
    void update(Meal meal);

    @Delete
    void delete(Meal meal);
}
