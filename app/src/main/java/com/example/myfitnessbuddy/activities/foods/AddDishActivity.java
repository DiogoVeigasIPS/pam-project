package com.example.myfitnessbuddy.activities.foods;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.Dish;
import com.example.myfitnessbuddy.database.models.DishWithQuantifiedFoods;

public class AddDishActivity extends AppCompatActivity {

    public static String DISH_ID = "DISH_ID";
    private EditText nameInput, descriptionInput;
    private RadioButton radioBtn1, radioBtn2, radioBtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);

        findViewById(R.id.bt_back).setOnClickListener(v -> finish());

        initializeInputs();

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            findViewById(R.id.bt_submit).setOnClickListener(v -> addDish());
            return;
        }

        int id = extras.getInt(DISH_ID);
        updateTitle();
        DatabaseHelper.executeInBackground(() -> {
            DishWithQuantifiedFoods dishWithQuantifiedFoods = DatabaseHelper.DishHelper.getDishById(id);
            if(dishWithQuantifiedFoods == null){
                runOnUiThread(() -> finish());
                return;
            }

            Dish dish = dishWithQuantifiedFoods.getDish();

            runOnUiThread(() -> {
                AppCompatButton deleteButton = findViewById(R.id.bt_delete);
                setDeleteButton(deleteButton, dish);
                fillInputs(dish);
                findViewById(R.id.bt_submit).setOnClickListener(v -> editDish(dish));
            });
        });
    }

    private void setDeleteButton(AppCompatButton deleteButton, Dish dish) {
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(dish));
    }

    private void showDeleteConfirmationDialog(Dish dish) {
        String message = "Are you sure you want to delete the <b>" + dish.getCompoundName() + "</b> ?";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml(message))
                .setPositiveButton("Yes", (dialog, which) -> {
                    DatabaseHelper.DishHelper.deleteDish(dish);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void editDish(Dish dish) {
        updateFoodProperties(dish);
        DatabaseHelper.DishHelper.updateDish(dish);
        finish();
    }

    private void updateFoodProperties(Dish existingDish) {
        Dish newDish = getDishFromInputs();

        existingDish.setIcon(newDish.getIcon());
        existingDish.setName(newDish.getName());
        existingDish.setDescription(newDish.getDescription());
    }

    private void fillInputs(Dish dish) {
        nameInput.setText(dish.getName());
        descriptionInput.setText(dish.getDescription());

        int icon = dish.getIcon();
        if (icon == R.drawable.salad) {
            radioBtn1.setChecked(true);
        } else if (icon == R.drawable.grilled_steak) {
            radioBtn2.setChecked(true);
        }else{
            radioBtn3.setChecked(true);
        }
    }

    private void addDish(){
        Dish dish = getDishFromInputs();
        if(dish == null) return;

        DatabaseHelper.DishHelper.addNewDish(dish);
        finish();
    }

    private void updateTitle() {
        TextView title = findViewById(R.id.title);
        title.setText(R.string.edit_food);
    }

    private Dish getDishFromInputs(){
        // Get values from the input fields
        String nameStr = nameInput.getText().toString();
        String descriptionStr = descriptionInput.getText().toString();

        int selectedIcon = R.drawable.salad;
        if (radioBtn2.isChecked()) {
            selectedIcon = R.drawable.grilled_steak;
        } else if (radioBtn3.isChecked()) {
            selectedIcon = R.drawable.cheese_cake;
        }

        if(nameStr.trim().equals("")){
            Toast.makeText(AddDishActivity.this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return null;
        }

        Dish dish;
        try {
            dish = new Dish(nameStr, descriptionStr, selectedIcon);
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.d(getClass().getSimpleName(), illegalArgumentException.toString());
            Toast.makeText(AddDishActivity.this, R.string.invalid_details, Toast.LENGTH_SHORT).show();
            return null;
        }

        return dish;
    }

    private void initializeInputs() {
        nameInput = findViewById(R.id.birthdate_input);
        descriptionInput = findViewById(R.id.description_input);

        radioBtn1 = findViewById(R.id.radio_button1);
        radioBtn2 = findViewById(R.id.radio_button2);
        radioBtn3 = findViewById(R.id.radio_button3);
    }
}