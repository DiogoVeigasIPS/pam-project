package com.example.myfitnessbuddy.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfitnessbuddy.database.models.Day;
import com.example.myfitnessbuddy.database.models.Meal;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface DayDao {
    @Query("SELECT * FROM day")
    List<Day> getAll();

    @Query("SELECT * from day WHERE dayId = :dayId LIMIT 1")
    Day findById(int dayId);

    @Query("SELECT * FROM day WHERE date = :todayDate LIMIT 1")
    Day getToday(LocalDate todayDate);

    @Query("SELECT * FROM meal WHERE dayId = :dayId")
    List<Meal> getMealsForDay(int dayId);

    /*@Query("SELECT COALESCE(SUM(COALESCE(qa.calories, 0) + COALESCE(qf.calculatedCalories, 0)), 0) AS totalCalories " +
            "FROM meal m " +
            "LEFT JOIN quickAddition qa ON qa.mealId = m.mealId " +
            "LEFT JOIN quantifiedFood qf ON qf.mealId = m.mealId " +
            "WHERE m.dayId = :dayId " +
            "GROUP BY m.mealId")
    List<Integer> getCaloriesList(int dayId);

    @Query("SELECT COALESCE(SUM(COALESCE(qa.calories, 0) + COALESCE(qf.calculatedCalories, 0)), 0) AS totalCalories " +
            "FROM meal m " +
            "LEFT JOIN quickAddition qa ON qa.mealId = m.mealId " +
            "LEFT JOIN quantifiedFood qf ON qf.mealId = m.mealId " +
            "WHERE m.dayId = :dayId")
    int getTotalCalories(int dayId);*/

    @Query("SELECT mealId FROM meal WHERE dayId = :dayId")
    List<Integer> getIdMealsByDay(int dayId);

    @Query("SELECT AVG(CASE WHEN weight <> 0 THEN weight ELSE NULL END) FROM day ORDER BY date DESC LIMIT 30")
    int getLastMonthAverageWeight();

    @Query("SELECT weight FROM day WHERE weight <> 0 ORDER BY date DESC LIMIT 1")
    Integer getLastWeight();

    @Query("SELECT COUNT(*) FROM day")
    int getDayCount();

    @Insert
    long insert(Day days);

    @Update
    void update(Day day);

    @Delete
    void delete(Day day);
}
