package com.example.myfitnessbuddy.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myfitnessbuddy.daos.DayDao;
import com.example.myfitnessbuddy.daos.DishDao;
import com.example.myfitnessbuddy.daos.FoodDao;
import com.example.myfitnessbuddy.daos.MealDao;
import com.example.myfitnessbuddy.daos.QuantifiedFoodDao;
import com.example.myfitnessbuddy.daos.QuickAdditionDao;
import com.example.myfitnessbuddy.database.models.Day;
import com.example.myfitnessbuddy.database.models.Dish;
import com.example.myfitnessbuddy.database.models.associatios.DishMealCrossRef;
import com.example.myfitnessbuddy.database.models.Food;
import com.example.myfitnessbuddy.database.models.Meal;
import com.example.myfitnessbuddy.database.models.QuantifiedFood;
import com.example.myfitnessbuddy.database.models.QuickAddition;

@Database(entities = {Food.class, Day.class, Meal.class, QuickAddition.class, QuantifiedFood.class, Dish.class, DishMealCrossRef.class}, version = 7, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase databaseInstance = null;

    public abstract FoodDao foodDao();
    public abstract DayDao dayDao();
    public abstract MealDao mealDao();
    public abstract QuickAdditionDao quickAdditionDao();
    public abstract QuantifiedFoodDao quantifiedFoodDao();
    public abstract DishDao dishDao();

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
