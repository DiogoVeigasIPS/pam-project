package com.example.myfitnessbuddy.activities.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.database.DatabaseHelper;

public class AddToMealActivity extends AppCompatActivity {

    public static final String TITLE = "TITLE";
    public static final String MEAL_ID = "MEAL_ID";
    private int stringResource;
    private int mealId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_meal);

        ImageButton btBack = findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> finish());

        Bundle extras = getIntent().getExtras();

        if(extras == null) return;
        stringResource = extras.getInt(TITLE);
        mealId = extras.getInt(MEAL_ID);

        updateMealData();
        setQuickAddNavigation(stringResource);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMealData();
    }

    private void updateMealData() {
        DatabaseHelper.executeInBackground(() -> {
            int calories = DatabaseHelper.MealHelper.getCalories(mealId);

            runOnUiThread(() -> setActivityTitle(calories));
        });
    }

    private void setQuickAddNavigation(int stringResource) {
        ((Button)findViewById(R.id.bt_quick_addition)).setOnClickListener(v -> {
            Intent intent = new Intent(AddToMealActivity.this, QuickAddActivity.class);
            intent.putExtra(TITLE, stringResource);
            intent.putExtra(QuickAddActivity.MEAL_ID, mealId);
            startActivity(intent);
        });
    }

    private void setActivityTitle(int calories) {
        String newTitle = getString(stringResource) + ": " + calories + " Kcal";

        TextView title = findViewById(R.id.title);
        title.setText(newTitle);
    }
}