package com.example.myfitnessbuddy.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfitnessbuddy.models.Day;
import com.example.myfitnessbuddy.models.Meal;

import java.util.List;

@Dao
public interface DayDao {
    @Query("SELECT * FROM day")
    List<Day> getAll();

    @Query("SELECT * from day WHERE id = :id LIMIT 1")
    Day findById(int id);

    @Query("SELECT * FROM meal WHERE dayId = :dayId")
    List<Meal> getMealsForDay(int dayId);

    @Insert
    void insert(Day... days);

    @Update
    void update(Day day);

    @Delete
    void delete(Day day);
}
