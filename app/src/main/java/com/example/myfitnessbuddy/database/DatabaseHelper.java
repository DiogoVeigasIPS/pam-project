package com.example.myfitnessbuddy.database;

import android.content.Context;

import com.example.myfitnessbuddy.models.Food;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseHelper {

    private static AppDatabase appDatabase;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void init(Context context) {
        appDatabase = AppDatabase.getInstance(context);
    }

    public static void executeInBackground(Runnable runnable) {
        executorService.execute(runnable);
    }

    public static List<Food> getAllFoods() {
        return appDatabase.foodDao().getAll();
    }

    public static void addNewFood(Food food) {
        executorService.execute(() -> appDatabase.foodDao().insert(food));
    }

    public static Food getFoodById(int foodId) {
        return appDatabase.foodDao().findById(foodId);
    }

    public static void updateFood(Food food) {
        executorService.execute(() -> appDatabase.foodDao().update(food));
    }

    public static List<Food> getFoodsByName(String searchQuery) {
        return appDatabase.foodDao().searchByName("%" + searchQuery + "%");
    }

    public static void deleteFood(Food food) {
        executorService.execute(() -> appDatabase.foodDao().delete(food));
    }
}
