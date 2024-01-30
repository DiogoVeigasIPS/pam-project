package com.example.myfitnessbuddy.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfitnessbuddy.models.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM food")
    List<Food> getAll();

    @Query("SELECT * from food WHERE id = :id LIMIT 1")
    Food findById(int id);

    @Insert
    void insert(Food... foods);

    @Update
    void update(Food food);

    @Delete
    void delete(Food food);

    @Query("SELECT * FROM food WHERE name LIKE :searchQuery")
    List<Food> searchByName(String searchQuery);
}
