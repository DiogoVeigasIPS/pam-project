package com.example.myfitnessbuddy.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfitnessbuddy.database.models.QuickAddition;

import java.util.List;

@Dao
public interface QuickAdditionDao {
    @Query("SELECT * FROM quickAddition")
    List<QuickAddition> getAll();

    @Query("SELECT * from quickAddition WHERE quickAdditionId = :quickAdditionId LIMIT 1")
    QuickAddition findById(int quickAdditionId);

    @Insert
    void insert(QuickAddition... quickAdditions);

    @Update
    void update(QuickAddition quickAddition);

    @Delete
    void delete(QuickAddition quickAddition);
}
