package com.example.myfitnessbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.widget.FrameLayout;


import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.main_fragments.Navigation;
import com.example.myfitnessbuddy.models.Day;
import com.example.myfitnessbuddy.models.Meal;
import com.example.myfitnessbuddy.models.enums.MealType;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DB
        DatabaseHelper.init(getApplicationContext());
        checkNewDay();

        // Fragments
        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);

        Navigation.setFragmentContainer(fragmentContainer);
        Navigation.setFragmentManager(getSupportFragmentManager());

        // Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Navigation.setFragmentNavigation(bottomNavigationView);

        //clearSharedPreferences();
    }

    private void checkNewDay() {
        DatabaseHelper.executeInBackground(() -> {
            Day day = DatabaseHelper.DayHelper.getToday();
            if(day != null)
                return;

            // Get last day's calorieGoal
            Day lastDay = DatabaseHelper.DayHelper.getYesterday();

            int calories = 0;
            if(lastDay != null) calories = lastDay.getCalorieGoal();

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

        });
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Clear all preferences
        editor.clear();

        // Apply the changes synchronously
        editor.commit();
    }
}
