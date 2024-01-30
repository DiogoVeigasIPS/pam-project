package com.example.myfitnessbuddy.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessbuddy.DatabaseHelper;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.panel.UpdateDetailsActivity;
import com.example.myfitnessbuddy.models.Food;

public class AddFoodActivity extends AppCompatActivity {
    public static String FOOD_ID = "FOOD_ID";
    private EditText nameInput, descriptionInput, portionInput, unitInput, calorieInput;
    private RadioButton radioBtn1, radioBtn2, radioBtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        ImageButton btBack = findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> finish());

        nameInput = findViewById(R.id.birthdate_input);
        descriptionInput = findViewById(R.id.description_input);
        portionInput = findViewById(R.id.portion_input);
        unitInput = findViewById(R.id.unit_input);
        calorieInput = findViewById(R.id.calorie_input);

        radioBtn1 = findViewById(R.id.radio_button1);
        radioBtn2 = findViewById(R.id.radio_button2);
        radioBtn3 = findViewById(R.id.radio_button3);

        ImageButton btSubmit = findViewById(R.id.bt_submit);

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            btSubmit.setOnClickListener(v -> addFood());
        }else{
            int id = extras.getInt(FOOD_ID);
            updateTitle("Edit Food");
            DatabaseHelper.executeInBackground(() -> {
                Food food = DatabaseHelper.getFoodById(id);
                if(food == null){
                    runOnUiThread(() -> finish());
                    return;
                }

                runOnUiThread(() -> {
                    fillInputs(food);
                    btSubmit.setOnClickListener(v -> editFood(food));
                });
            });
        }
    }

    private void updateTitle(String newTitle) {
        TextView title = findViewById(R.id.title);
        title.setText(newTitle);
    }

    private void editFood(Food food) {
        updateFoodProperties(food);
        DatabaseHelper.updateFood(food);
        finish();
    }

    private void updateFoodProperties(Food existingFood) {
        Food newFood = getFoodFromInputs();

        existingFood.setIcon(newFood.getIcon());
        existingFood.setName(newFood.getName());
        existingFood.setDescription(newFood.getDescription());
        existingFood.setPortionSize(newFood.getPortionSize());
        existingFood.setCaloriesPerPortion(newFood.getCaloriesPerPortion());
        existingFood.setUnits(newFood.getUnits());
    }

    private void fillInputs(Food food) {
        nameInput.setText(food.getName());
        descriptionInput.setText(food.getDescription());
        portionInput.setText(String.valueOf((int)food.getPortionSize()));
        unitInput.setText(food.getUnits());
        calorieInput.setText(String.valueOf((int)food.getCaloriesPerPortion()));

        int icon = food.getIcon();
        if (icon == R.drawable.apple) {
            radioBtn1.setChecked(true);
        } else if (icon == R.drawable.steak) {
            radioBtn2.setChecked(true);
        }else{
            radioBtn3.setChecked(true);
        }
    }


    private void addFood(){
        Food food = getFoodFromInputs();
        if(food == null) return;

        DatabaseHelper.addNewFood(food);
        finish();
    }

    private Food getFoodFromInputs(){
        // Get values from the input fields
        String nameStr = nameInput.getText().toString();
        String descriptionStr = descriptionInput.getText().toString();
        String portionStr = portionInput.getText().toString();
        String unitStr = unitInput.getText().toString();
        String calorieStr = calorieInput.getText().toString();

        int selectedIcon = R.drawable.apple;
        if (radioBtn2.isChecked()) {
            selectedIcon = R.drawable.steak;
        } else if (radioBtn3.isChecked()) {
            selectedIcon = R.drawable.cookie;
        }

        if(nameStr.equals("") || portionStr.equals("") ||  unitStr.equals("") || calorieStr.equals("")){
            Toast.makeText(AddFoodActivity.this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return null;
        }

        double portion;
        try {
            portion = Double.parseDouble(portionStr);
        } catch (NumberFormatException numberFormatException) {
            Toast.makeText(AddFoodActivity.this, R.string.invalid_portion_format, Toast.LENGTH_SHORT).show();
            return null;
        }

        double calorie;
        try {
            calorie = Double.parseDouble(calorieStr);
        } catch (NumberFormatException numberFormatException) {
            Toast.makeText(AddFoodActivity.this, R.string.invalid_calorie_format, Toast.LENGTH_SHORT).show();
            return null;
        }

        Food food;
        try {
            food = new Food(nameStr, descriptionStr, selectedIcon, portion, calorie, unitStr);
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.d(getClass().getSimpleName(), illegalArgumentException.toString());
            Toast.makeText(AddFoodActivity.this, R.string.invalid_details, Toast.LENGTH_SHORT).show();
            return null;
        }
        return food;
    }
}
