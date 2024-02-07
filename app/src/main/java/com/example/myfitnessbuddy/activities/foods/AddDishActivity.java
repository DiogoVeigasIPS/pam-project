package com.example.myfitnessbuddy.activities.foods;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.adapters.ActionType;
import com.example.myfitnessbuddy.adapters.FoodAdapter;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.Dish;
import com.example.myfitnessbuddy.database.models.associatios.DishWithQuantifiedFoods;
import com.example.myfitnessbuddy.database.models.associatios.ListableFood;
import com.example.myfitnessbuddy.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

public class AddDishActivity extends AppCompatActivity {

    public static String DISH_ID = "DISH_ID";
    private EditText nameInput, descriptionInput;
    private RadioButton radioBtn1, radioBtn2, radioBtn3;
    private int dishId = -1;

    private RecyclerView foodsList;
    private TextView emptyList;
    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);

        findViewById(R.id.bt_back).setOnClickListener(v -> finish());

        initializeInputs();

        foodsList = findViewById(R.id.foods_list);
        emptyList = findViewById(R.id.empty_list);

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            findViewById(R.id.bt_submit).setOnClickListener(v -> addDish());
            return;
        }

        dishId = extras.getInt(DISH_ID);
        setListAdapter();
        updateTitle();
        setEditDishActions();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(foodAdapter == null) return;

        updateFoodList();
    }

    private void setListAdapter() {
        DatabaseHelper.executeInBackground(() -> {
            DishWithQuantifiedFoods dishWithQuantifiedFoods = DatabaseHelper.DishHelper.getDishById(dishId);
            List<ListableFood> foods = new ArrayList<>(dishWithQuantifiedFoods.getQuantifiedFoods());

            runOnUiThread(() -> {
                foodAdapter = new FoodAdapter(foods, ActionType.EDIT_IN_DISH, dishId);
                foodsList.setAdapter(foodAdapter);
                foodsList.setLayoutManager(new LinearLayoutManager(this));

                updateDishCalories(dishWithQuantifiedFoods);
                if (foods.isEmpty()) {
                    showEmptyListMessage(R.string.this_list_seems_to_be_empty, foodsList);
                    return;
                }
                hideEmptyListMessage();
            });
        });
    }

    public void updateFoodList() {
        DatabaseHelper.executeInBackground(() -> {
            DishWithQuantifiedFoods dishWithQuantifiedFoods = DatabaseHelper.DishHelper.getDishById(dishId);
            List<ListableFood> foods = new ArrayList<>(dishWithQuantifiedFoods.getQuantifiedFoods());

            runOnUiThread(() -> {
                updateDishCalories(dishWithQuantifiedFoods);
                foodAdapter.setFoods(foods);

                if(foods.isEmpty()){
                    showEmptyListMessage(R.string.this_list_seems_to_be_empty, foodsList);
                    return;
                }

                hideEmptyListMessage();
            });
        });
    }

    private void hideEmptyListMessage() {
        emptyList.setVisibility(View.GONE);
        foodsList.setVisibility(View.VISIBLE);
    }

    private void showEmptyListMessage(int message, RecyclerView foodsList){
        emptyList.setText(message);
        emptyList.setVisibility(View.VISIBLE);
        foodsList.setVisibility(View.GONE);
    }

    public void updateDishCalories(DishWithQuantifiedFoods dishWithQuantifiedFoods) {
        int calories = dishWithQuantifiedFoods.getCalories();
        ((TextView)findViewById(R.id.dishes_kcals)).setText(String.valueOf(calories));
    }

    private void setEditDishActions() {
        DatabaseHelper.executeInBackground(() -> {
            DishWithQuantifiedFoods dishWithQuantifiedFoods = DatabaseHelper.DishHelper.getDishById(dishId);
            if(dishWithQuantifiedFoods == null){
                runOnUiThread(() -> finish());
                return;
            }

            Dish dish = dishWithQuantifiedFoods.getDish();

            runOnUiThread(() -> {
                // Show card with list and stuff
                findViewById(R.id.foods_card_view).setVisibility(View.VISIBLE);

                // Add button adds
                findViewById(R.id.bt_add_food).setOnClickListener(v -> {
                    Intent intent = new Intent(AddDishActivity.this, AddToDishActivity.class);
                    intent.putExtra(AddToDishActivity.TITLE, dish.getName());
                    intent.putExtra(AddToDishActivity.DISH_ID, dish.getDishId());
                    startActivity(intent);
                });

                // Delete
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
        String message = getString(R.string.delete_confirmation)+ " <b>" + dish.getCompoundName() + "</b> ?";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml(message))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    DatabaseHelper.DishHelper.deleteDish(dish);
                    finish();
                })
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void editDish(Dish dish) {
        updateFoodProperties(dish);
        DatabaseHelper.DishHelper.updateDish(dish);
        CustomToast.showSuccessToast(this, R.string.dish_updated, Toast.LENGTH_SHORT);
        //finish();
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

        DatabaseHelper.executeInBackground(() -> {
            dishId = (int) DatabaseHelper.DishHelper.addNewDish(dish);
            DishWithQuantifiedFoods addedDish = DatabaseHelper.DishHelper.getDishById(dishId);

            runOnUiThread(() -> {
                setListAdapter();
                updateTitle();
                setEditDishActions();
                updateDishCalories(addedDish);
                CustomToast.showSuccessToast(this, R.string.dish_created, Toast.LENGTH_SHORT);
            });
        });

        //finish();
    }

    private void updateTitle() {
        TextView title = findViewById(R.id.title);
        title.setText(R.string.edit_dish);
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
            CustomToast.showErrorToast(AddDishActivity.this, R.string.fill_all_fields, Toast.LENGTH_SHORT);
            return null;
        }

        Dish dish;
        try {
            dish = new Dish(nameStr, descriptionStr, selectedIcon);
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.d(getClass().getSimpleName(), illegalArgumentException.toString());
            CustomToast.showErrorToast(AddDishActivity.this, R.string.invalid_details, Toast.LENGTH_SHORT);
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