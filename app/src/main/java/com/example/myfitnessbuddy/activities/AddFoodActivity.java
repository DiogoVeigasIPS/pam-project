package com.example.myfitnessbuddy.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.myfitnessbuddy.DatabaseHelper;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.panel.UpdateDetailsActivity;
import com.example.myfitnessbuddy.models.Food;

public class AddFoodActivity extends AppCompatActivity {

    // Declare your views as class members
    private EditText nameInput, descriptionInput, portionInput, unitInput, calorieInput;
    private RadioButton radioBtn1, radioBtn2, radioBtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Initialize your views
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
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    return;
                }

                double portion;
                try {
                    portion = Double.parseDouble(portionStr);
                } catch (NumberFormatException numberFormatException) {
                    Toast.makeText(AddFoodActivity.this, R.string.invalid_portion_format, Toast.LENGTH_SHORT).show();
                    return;
                }

                double calorie;
                try {
                    calorie = Double.parseDouble(calorieStr);
                } catch (NumberFormatException numberFormatException) {
                    Toast.makeText(AddFoodActivity.this, R.string.invalid_calorie_format, Toast.LENGTH_SHORT).show();
                    return;
                }

                Food food;
                try {
                    food = new Food(nameStr, descriptionStr, selectedIcon, portion, calorie, unitStr);
                } catch (IllegalArgumentException illegalArgumentException) {
                    Log.d(getClass().getSimpleName(), illegalArgumentException.toString());
                    Toast.makeText(AddFoodActivity.this, R.string.invalid_details, Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseHelper.addNewFood(food);
                finish();

            }
        });
    }
}
