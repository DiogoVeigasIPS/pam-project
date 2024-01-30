package com.example.myfitnessbuddy;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myfitnessbuddy.daos.FoodDao;
import com.example.myfitnessbuddy.models.Food;

@Database(entities = {Food.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase databaseInstance = null;
    public abstract FoodDao foodDao();

    public static AppDatabase getInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(
                            context.getApplicationContext(), AppDatabase.class, "fitness_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return databaseInstance;
    }
}
