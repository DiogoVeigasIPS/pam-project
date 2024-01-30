package com.example.myfitnessbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;


import com.example.myfitnessbuddy.fragments.Navigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DB
        DatabaseHelper.init(getApplicationContext());

        // Fragments
        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);

        Navigation.setFragmentContainer(fragmentContainer);
        Navigation.setFragmentManager(getSupportFragmentManager());

        // Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Navigation.setFragmentNavigation(bottomNavigationView);

        //clearSharedPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        User user = UserPreferences.readUserPreferences(this);
//        if(user != null){
//            Log.d("SHARED_PREFERENCES", user.getActivity().name());
//            Log.d("SHARED_PREFERENCES", user.getGoal().name());
//            Log.d("SHARED_PREFERENCES", String.valueOf(user.getAge()));
//            Log.d("SHARED_PREFERENCES", String.valueOf(user.getHeight()));
//        }
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
