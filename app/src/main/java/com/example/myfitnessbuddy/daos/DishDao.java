package com.example.myfitnessbuddy.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.myfitnessbuddy.database.models.Dish;
import com.example.myfitnessbuddy.database.models.associatios.DishWithQuantifiedFoods;

import java.util.List;

@Dao
public interface DishDao {

    @Transaction
    @Query("SELECT * FROM dish")
    List<DishWithQuantifiedFoods> getAll();

    @Transaction
    @Query("SELECT * FROM dish WHERE dishId = :dishId")
    DishWithQuantifiedFoods findById(int dishId);

    @Insert
    void insert(Dish dish);

    @Update
    void update(Dish dish);

    @Delete
    void delete(Dish dish);

    @Transaction
    @Query("SELECT * FROM dish WHERE name LIKE :searchQuery")
    List<DishWithQuantifiedFoods> searchByName(String searchQuery);

}
