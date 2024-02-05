package com.example.myfitnessbuddy.activities.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.adapters.ActionType;
import com.example.myfitnessbuddy.adapters.FoodAdapter;
import com.example.myfitnessbuddy.adapters.SearchType;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.associatios.ListableFood;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AddToMealActivity extends AppCompatActivity {

    public static final String TITLE = "TITLE";
    public static final String MEAL_ID = "MEAL_ID";
    public static final String IS_EDIT = "IS_EDIT";
    private int stringResource;
    private int mealId;
    private boolean isEdit;

    private RecyclerView foodsList;
    private TextView emptyList;
    private FoodAdapter foodAdapter;

    private EditText foodsSearch;
    private SearchType searchType = SearchType.FOODS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_meal);

        setNavigationalButtons();
        setTabNavigation();

        Bundle extras = getIntent().getExtras();

        if(extras == null) return;
        stringResource = extras.getInt(TITLE);
        mealId = extras.getInt(MEAL_ID);
        isEdit = extras.getBoolean(IS_EDIT, false);

        foodsList = findViewById(R.id.foods_list);
        emptyList = findViewById(R.id.empty_list);

        updateLayoutAccordingToMode(isEdit);
        setListAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateMealData();
        if(foodAdapter == null) return;

        String search = foodsSearch.getText().toString();
        if (searchType == SearchType.FOODS) {
            if (!search.trim().equals("")) {
                updateFoodList();
            } else {
                updateFoodList(search);
            }
        } else if (searchType == SearchType.DISHES) {
            if (!search.trim().equals("")) {
                updateDishList();
            } else {
                updateDishList(search);
            }
        }
    }

    private void updateLayoutAccordingToMode(boolean isEdit) {
        if(!isEdit){
            setQuickAddNavigation(stringResource);
            return;
        }

        findViewById(R.id.tab_selector).setVisibility(View.GONE);
        findViewById(R.id.bt_quick_addition).setVisibility(View.GONE);
    }

    private void setListAdapter() {
        DatabaseHelper.executeInBackground(() -> {
            List<ListableFood> foods;

            if(!isEdit)
                foods = new ArrayList<>(DatabaseHelper.FoodHelper.getAllFoods());
            else
                foods = new ArrayList<>(DatabaseHelper.MealHelper.getAllFoodsInMeal(mealId).getListableFoods());

            runOnUiThread(() -> {
                if(!isEdit)
                    foodAdapter = new FoodAdapter(foods, ActionType.ADD_TO_MEAL, mealId);
                else
                    foodAdapter = new FoodAdapter(foods, ActionType.EDIT_IN_MEAL, mealId, stringResource);

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

    public void updateFoodList() {
        DatabaseHelper.executeInBackground(() -> {
            List<ListableFood> foods;

            if(!isEdit)
                foods = new ArrayList<>(DatabaseHelper.FoodHelper.getAllFoods());
            else
                foods = new ArrayList<>(DatabaseHelper.MealHelper.getAllFoodsInMeal(mealId).getListableFoods());

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
            List<ListableFood> foods;

            if(!isEdit)
                foods = new ArrayList<>(DatabaseHelper.FoodHelper.getFoodsByName(search));
            else
                foods = new ArrayList<>(DatabaseHelper.MealHelper.getAllFoodsInMeal(mealId).getListableFoods(search));

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

    private void updateDishList() {
        DatabaseHelper.executeInBackground(() -> {
            List<ListableFood> foods = new ArrayList<>(DatabaseHelper.DishHelper.getAllDishes());

            runOnUiThread(() -> {
                if (foods.isEmpty()) {
                    showEmptyListMessage(R.string.this_list_seems_to_be_empty, foodsList);
                    return;
                }

                hideEmptyListMessage();
                foodAdapter.setFoods(foods, ActionType.ADD_TO_MEAL);
            });
        });
    }

    private void updateDishList(String search) {
        DatabaseHelper.executeInBackground(() -> {
            List<ListableFood> foods = new ArrayList<>(DatabaseHelper.DishHelper.searchDishesByName(search));

            runOnUiThread(() -> {
                if (foods.isEmpty()) {
                    showEmptyListMessage(R.string.not_found_in_list, foodsList);
                    return;
                } else {
                    hideEmptyListMessage();
                }

                foodAdapter.setFoods(foods, ActionType.ADD_TO_MEAL);
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

    public void updateMealData() {
        DatabaseHelper.executeInBackground(() -> {
            int calories = DatabaseHelper.MealHelper.getCalories(mealId);

            runOnUiThread(() -> setActivityTitle(calories));
        });
    }

    private void setQuickAddNavigation(int stringResource) {
        findViewById(R.id.bt_quick_addition).setOnClickListener(v -> {
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

    private void setNavigationalButtons(){
        ImageButton btBack = findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> finish());

        foodsSearch = findViewById(R.id.foods_search);
        foodsSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String search = foodsSearch.getText().toString();
                if (searchType == SearchType.FOODS) {
                    updateFoodList(search);
                } else if (searchType == SearchType.DISHES) {
                    updateDishList(search);
                }
                return true;
            }
            return false;
        });
    }

    private void setTabNavigation() {
        TabLayout tabSelector = findViewById(R.id.tab_selector);

        tabSelector.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                foodsSearch.setText("");
                int selectedTabPosition = tab.getPosition();
                switch (selectedTabPosition) {
                    case 0:
                        searchType = SearchType.FOODS;
                        updateFoodList();
                        break;
                    case 1:
                        searchType = SearchType.DISHES;
                        updateDishList();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselected
            }
        });
    }
}