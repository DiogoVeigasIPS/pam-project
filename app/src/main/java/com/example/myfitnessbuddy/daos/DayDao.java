package com.example.myfitnessbuddy.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfitnessbuddy.models.Day;
import com.example.myfitnessbuddy.models.Meal;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface DayDao {
    @Query("SELECT * FROM day")
    List<Day> getAll();

    @Query("SELECT * from day WHERE id = :id LIMIT 1")
    Day findById(int id);

    @Query("SELECT * FROM day WHERE date = :todayDate LIMIT 1")
    Day getToday(LocalDate todayDate);

    @Query("SELECT * FROM meal WHERE dayId = :dayId")
    List<Meal> getMealsForDay(int dayId);

    @Query("SELECT COALESCE(SUM(qa.calories), 0) AS totalCalories " +
            "FROM meal m " +
            "LEFT JOIN quickAddition qa ON qa.mealId = m.id " +
            "WHERE m.dayId = :dayId " +
            "GROUP BY m.id")
    List<Integer> getCaloriesList(int dayId);

    @Query("SELECT weight FROM day WHERE weight <> 0 ORDER BY date DESC LIMIT 1")
    Integer getLastWeight();

    @Insert
    long insert(Day days);

    @Update
    void update(Day day);

    @Delete
    void delete(Day day);
}
