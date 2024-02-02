package com.example.myfitnessbuddy.activities.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.QuickAddition;

public class QuickAddActivity extends AppCompatActivity {

    public static final String MEAL_ID = "MEAL_ID";
    private static final String TITLE = "TITLE";
    private TextView nameInput, caloriesInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add);

        ((ImageButton)findViewById(R.id.bt_back)).setOnClickListener(v -> finish());

        Bundle extras = getIntent().getExtras();

        if(extras == null) return;
        int stringResource = extras.getInt(TITLE);
        int mealId = extras.getInt(MEAL_ID);

        setActivityTitle(stringResource);

        setEditTexts();
        ((ImageButton)findViewById(R.id.bt_submit)).setOnClickListener(v -> addQuickAddition(mealId));

    }

    private void setEditTexts() {
        nameInput = findViewById(R.id.name_input);
        caloriesInput = findViewById(R.id.calories_input);
    }

    private void addQuickAddition(int mealId) {
        String nameStr = nameInput.getText().toString();
        String caloriesStr = caloriesInput.getText().toString();

        if(nameStr.equals("") || caloriesStr.equals("")){
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        int calories;
        try{
            calories = Integer.parseInt(caloriesStr);
        }catch (NumberFormatException numberFormatException){
            Toast.makeText(this, R.string.invalid_calorie_format, Toast.LENGTH_SHORT).show();
            return;
        }

        QuickAddition quickAddition;
        try {
            quickAddition = new QuickAddition(nameStr, calories, mealId);
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.d(getClass().getSimpleName(), illegalArgumentException.toString());
            Toast.makeText(QuickAddActivity.this, R.string.invalid_details, Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper.QuickAdditionHelper.addNewQuickAddition(quickAddition);
        finish();
    }

    private void setActivityTitle(int stringResource) {
        TextView title = findViewById(R.id.title);
        String newTitle = getString(R.string.quick) + " " + getString(stringResource);
        title.setText(newTitle);
    }
}