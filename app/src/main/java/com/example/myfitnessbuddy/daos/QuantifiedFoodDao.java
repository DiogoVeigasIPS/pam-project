package com.example.myfitnessbuddy.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfitnessbuddy.database.models.Food;
import com.example.myfitnessbuddy.database.models.QuantifiedFood;

import java.util.List;

@Dao
public interface QuantifiedFoodDao {
    @Query("SELECT * FROM quantifiedFood")
    List<QuantifiedFood> getAll();

    @Query("SELECT * FROM quantifiedFood WHERE quantifiedFoodId = :id")
    QuantifiedFood findById(int id);

    @Insert
    void insert(QuantifiedFood quantifiedFood);

    @Insert
    void insertAll(QuantifiedFood[] quantifiedFoods);

    @Update
    void update(QuantifiedFood quantifiedFood);

    @Delete
    void delete(QuantifiedFood quantifiedFood);
}
