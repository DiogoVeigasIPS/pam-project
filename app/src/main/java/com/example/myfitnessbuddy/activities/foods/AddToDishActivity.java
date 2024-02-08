package com.example.myfitnessbuddy.activities.foods;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.adapters.ActionType;
import com.example.myfitnessbuddy.adapters.FoodAdapter;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.associatios.ListableFood;

import java.util.ArrayList;
import java.util.List;

public class AddToDishActivity extends AppCompatActivity {
    public static final String TITLE = "TITLE";
    public static final String DISH_ID = "DISH_ID";
    private String dishName;
    private int dishId;

    private RecyclerView foodsList;
    private TextView emptyList;
    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_dish);

        setNavigationalButtons();

        Bundle extras = getIntent().getExtras();

        if(extras == null) return;

        dishName = extras.getString(TITLE);
        dishId = extras.getInt(DISH_ID);

        foodsList = findViewById(R.id.foods_list);
        emptyList = findViewById(R.id.empty_list);

        setListAdapter();
        updateDishData();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(foodAdapter == null) return;

        updateDishData();
        updateFoodList();
    }

    private void setListAdapter() {
        DatabaseHelper.executeInBackground(() -> {
            List<ListableFood> foods = new ArrayList<>(DatabaseHelper.FoodHelper.getAllFoods());

            runOnUiThread(() -> {
                foodAdapter = new FoodAdapter(foods, ActionType.ADD_TO_DISH, dishId);
                foodsList.setAdapter(foodAdapter);
                foodsList.setLayoutManager(new LinearLayoutManager(this));

                if (foods.isEmpty()) {
                    showEmptyListMessage(R.string.this_list_seems_to_be_empty, foodsList);
                    return;
                }
                hideEmptyListMessage();
            });
        });
    }

    private void updateFoodList() {
        DatabaseHelper.executeInBackground(() -> {
            List<ListableFood> foods = new ArrayList<>(DatabaseHelper.FoodHelper.getAllFoods());

            runOnUiThread(() -> {
                if(foods.isEmpty()){
                    showEmptyListMessage(R.string.this_list_seems_to_be_empty, foodsList);
                    return;
                }

                hideEmptyListMessage();
                foodAdapter.setFoods(foods);
            });
        });
    }

    private void updateFoodList(String search) {
        DatabaseHelper.executeInBackground(() -> {
            List<ListableFood> foods = new ArrayList<>(DatabaseHelper.FoodHelper.getFoodsByName(search));

            runOnUiThread(() -> {
                if(foods.isEmpty()){
                    showEmptyListMessage(R.string.not_found_in_list, foodsList);
                    return;
                }else{
                    hideEmptyListMessage();
                }

                foodAdapter.setFoods(foods);
                hideKeyboard();
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

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void updateDishData() {
        DatabaseHelper.executeInBackground(() -> {
            int calories = DatabaseHelper.DishHelper.getDishById(dishId).getCalories();

            runOnUiThread(() -> setActivityTitle(calories));
        });
    }

    private void setActivityTitle(int calories) {
        String formattedName = dishName.length() > 10 ? dishName.substring(0, 10) + "..." : dishName;

        String newTitle = formattedName + ": " + calories + " Kcal";

        TextView title = findViewById(R.id.title);
        title.setText(newTitle);
    }

    private void setNavigationalButtons(){
        ImageButton btBack = findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> finish());

        EditText foodsSearch = findViewById(R.id.foods_search);
        foodsSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String search = foodsSearch.getText().toString();
                updateFoodList(search);
                return true;
            }
            return false;
        });
    }
}