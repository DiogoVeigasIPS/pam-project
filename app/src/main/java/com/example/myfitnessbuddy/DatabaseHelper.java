package com.example.myfitnessbuddy;

import android.content.Context;
import android.os.AsyncTask;

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
}
