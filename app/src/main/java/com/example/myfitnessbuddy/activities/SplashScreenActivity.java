package com.example.myfitnessbuddy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.myfitnessbuddy.MainActivity;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.Day;
import com.example.myfitnessbuddy.database.models.Meal;
import com.example.myfitnessbuddy.database.models.data.FoodsData;
import com.example.myfitnessbuddy.database.models.enums.MealType;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Start, populate and check for a new day
        startDatabase();
    }

    private void startDatabase() {
        DatabaseHelper.init(getApplicationContext());

        DatabaseHelper.executeInBackground(() -> {
            // Check for need for population
            if (DatabaseHelper.DayHelper.getDayCount() == 0) {
                DatabaseHelper.FoodHelper.addNewFoods(FoodsData.populateFoodsData());
            }

            checkNewDay();

            runOnUiThread(() -> {
                new Handler().postDelayed(() -> {
                    // Create an Intent to start the main activity
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);

                    // Close the splash screen activity
                    finish();
                }, SPLASH_DELAY);
            });
        });
    }

    private void checkNewDay() {
        Day day = DatabaseHelper.DayHelper.getToday();
        if (day != null)
            return;

        // Get last day's calorieGoal
        Day lastDay = DatabaseHelper.DayHelper.getYesterday();

        int calories = 0;
        if (lastDay != null) calories = lastDay.getCalorieGoal();

        Day newDay = new Day(calories);

        // Add new day
        int insertedDayId = (int) DatabaseHelper.DayHelper.addNewDay(newDay);

        // Initialize meals
        List<Meal> meals = new ArrayList<>();
        meals.add(new Meal(MealType.BREAKFAST, insertedDayId));
        meals.add(new Meal(MealType.LUNCH, insertedDayId));
        meals.add(new Meal(MealType.DINNER, insertedDayId));
        meals.add(new Meal(MealType.SNACKS, insertedDayId));
        DatabaseHelper.MealHelper.addNewMeals(meals);
    }
}
