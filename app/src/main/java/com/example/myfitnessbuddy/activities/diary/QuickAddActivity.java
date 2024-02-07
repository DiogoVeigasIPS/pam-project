package com.example.myfitnessbuddy.activities.diary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.Food;
import com.example.myfitnessbuddy.database.models.QuickAddition;

public class QuickAddActivity extends AppCompatActivity {

    public static final String MEAL_ID = "MEAL_ID";
    public static final String TITLE = "TITLE";
    public static final String QUICK_ADDITION_ID = "QUICK_ADDITION_ID";
    private EditText nameInput, caloriesInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add);

        findViewById(R.id.bt_back).setOnClickListener(v -> finish());

        Bundle extras = getIntent().getExtras();
        if(extras == null) return;

        int stringResource = extras.getInt(TITLE);
        int mealId = extras.getInt(MEAL_ID);
        int quickAdditionId = extras.getInt(QUICK_ADDITION_ID, -1);

        setActivityTitle(stringResource);

        setEditTexts();
        if(quickAdditionId == -1) {
            findViewById(R.id.bt_submit).setOnClickListener(v -> addQuickAddition(mealId));
            return;
        }

        DatabaseHelper.executeInBackground(() -> {
            QuickAddition quickAddition = DatabaseHelper.QuickAdditionHelper.getQuickAdditionById(quickAdditionId);
            if(quickAddition == null){
                runOnUiThread(() -> finish());
                return;
            }

            runOnUiThread(() -> {
                AppCompatButton deleteButton = findViewById(R.id.bt_delete);
                setDeleteButton(deleteButton, quickAddition);
                fillInputs(quickAddition);
                findViewById(R.id.bt_submit).setOnClickListener(v -> editQuickAddition(quickAddition));
            });
        });
    }

    private void setDeleteButton(AppCompatButton deleteButton, QuickAddition quickAddition) {
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(quickAddition));
    }

    private void showDeleteConfirmationDialog(QuickAddition quickAddition) {
        String message = getString(R.string.delete_confirmation) + " <b>" + quickAddition.getCompoundName() + "</b> ?";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml(message))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    DatabaseHelper.QuickAdditionHelper.deleteQuickAddition(quickAddition);
                    finish();
                })
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void editQuickAddition(QuickAddition quickAddition) {
        updateQuickAdditionProperties(quickAddition);
        DatabaseHelper.QuickAdditionHelper.updateQuickAddition(quickAddition);
        finish();
    }

    private void updateQuickAdditionProperties(QuickAddition existingQuickAddition) {
        QuickAddition newQuickAddition = getQuickAdditionFromInputs(existingQuickAddition.getId());

        existingQuickAddition.setCalories(newQuickAddition.getCalories());
        existingQuickAddition.setName(newQuickAddition.getName());
    }

    private void fillInputs(QuickAddition quickAddition) {
        nameInput.setText(quickAddition.getName());
        caloriesInput.setText(String.valueOf(quickAddition.getCalories()));
    }

    private void setEditTexts() {
        nameInput = findViewById(R.id.name_input);
        caloriesInput = findViewById(R.id.calories_input);
    }

    private QuickAddition getQuickAdditionFromInputs(int mealId){
        String nameStr = nameInput.getText().toString();
        String caloriesStr = caloriesInput.getText().toString();

        if(nameStr.trim().equals("") || caloriesStr.trim().equals("")){
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return null;
        }

        int calories;
        try{
            calories = Integer.parseInt(caloriesStr);
        }catch (NumberFormatException numberFormatException){
            Toast.makeText(this, R.string.invalid_calorie_format, Toast.LENGTH_SHORT).show();
            return null;
        }

        QuickAddition quickAddition;
        try {
            quickAddition = new QuickAddition(nameStr, calories, mealId);
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.d(getClass().getSimpleName(), illegalArgumentException.toString());
            Toast.makeText(QuickAddActivity.this, R.string.invalid_details, Toast.LENGTH_SHORT).show();
            return null;
        }

        return quickAddition;
    }

    private void addQuickAddition(int mealId) {
        QuickAddition quickAddition = getQuickAdditionFromInputs(mealId);

        if(quickAddition == null) return;

        DatabaseHelper.QuickAdditionHelper.addNewQuickAddition(quickAddition);
        finish();
    }

    private void setActivityTitle(int stringResource) {
        TextView title = findViewById(R.id.title);
        String newTitle = getString(R.string.quick) + " " + getString(stringResource);
        title.setText(newTitle);
    }
}