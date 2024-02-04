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

    @Query("SELECT COALESCE(SUM(COALESCE(qa.calories, 0) + COALESCE(qf.calculatedCalories, 0)), 0) AS totalCalories " +
            "FROM meal m " +
            "LEFT JOIN quickAddition qa ON qa.mealId = m.id " +
            "LEFT JOIN quantifiedFood qf ON qf.mealId = m.id " +
            "WHERE m.id = :mealId " +
            "GROUP BY m.id")
    int getCalories(int mealId);

    @Insert
    void insert(Meal... meals);

    @Update
    void update(Meal meal);

    @Delete
    void delete(Meal meal);
}
