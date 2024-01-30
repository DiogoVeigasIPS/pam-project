package com.example.myfitnessbuddy.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfitnessbuddy.models.Consumable;

import java.util.List;

@Dao
public interface ConsumableDao {
    @Query("SELECT * FROM consumable")
    List<Consumable> getAll();

    @Query("SELECT * FROM consumable WHERE type = :type")
    List<Consumable> getAllByType(String type);

    @Query("SELECT * from consumable WHERE id = :id LIMIT 1")
    Consumable findById(int id);

    @Insert
    void insert(Consumable... consumables);

    @Update
    void update(Consumable consumable);

    @Delete
    void delete(Consumable consumable);

    @Query("SELECT * FROM consumable WHERE name LIKE :searchQuery")
    List<Consumable> searchByName(String searchQuery);

    @Query("SELECT * FROM consumable WHERE name LIKE :searchQuery AND type = :type")
    List<Consumable> searchByNameAndType(String searchQuery, String type);
}
