package com.example.myfitnessbuddy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.main_fragments.Navigation;
import com.example.myfitnessbuddy.database.models.Day;
import com.example.myfitnessbuddy.database.models.Meal;
import com.example.myfitnessbuddy.database.models.enums.MealType;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fragments
        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);

        Navigation.setFragmentContainer(fragmentContainer);
        Navigation.setFragmentManager(getSupportFragmentManager());

        // Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Navigation.setBottomNavigationView(bottomNavigationView);
        Navigation.setFragmentNavigation();

        //clearSharedPreferences();
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Clear all preferences
        editor.clear();

        // Apply the changes synchronously
        editor.apply();
    }
}
