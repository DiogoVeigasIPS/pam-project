package com.example.myfitnessbuddy.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfitnessbuddy.models.Meal;

import java.util.List;

@Dao
public interface MealDao {
    @Query("SELECT * FROM meal")
    List<Meal> getAll();

    @Query("SELECT * from meal WHERE id = :id LIMIT 1")
    Meal findById(int id);

    @Insert
    void insert(Meal... meals);

    @Update
    void update(Meal meal);

    @Delete
    void delete(Meal meal);
}
