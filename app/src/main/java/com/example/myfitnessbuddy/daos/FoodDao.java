package com.example.myfitnessbuddy.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfitnessbuddy.database.models.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM food")
    List<Food> getAll();

    @Query("SELECT * from food WHERE foodId = :foodId LIMIT 1")
    Food findById(int foodId);

    @Insert
    void insert(Food food);

    @Insert
    void insertAll(Food[] foods);

    @Update
    void update(Food food);

    @Delete
    void delete(Food food);

    @Query("SELECT * FROM food WHERE name LIKE :searchQuery")
    List<Food> searchByName(String searchQuery);
}
