package com.example.myfitnessbuddy.activities.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.foods.AddFoodActivity;
import com.example.myfitnessbuddy.adapters.ActionType;
import com.example.myfitnessbuddy.adapters.FoodAdapter;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.Food;
import com.example.myfitnessbuddy.database.models.FoodPreset;
import com.example.myfitnessbuddy.main_fragments.FragmentPanel;
import com.example.myfitnessbuddy.main_fragments.Navigation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AddToMealActivity extends AppCompatActivity {

    public static final String TITLE = "TITLE";
    public static final String MEAL_ID = "MEAL_ID";
    private int stringResource;
    private int mealId;

    private RecyclerView foodsList;
    private TextView emptyList;
    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_meal);

        setNavigationalButtons();

        Bundle extras = getIntent().getExtras();

        if(extras == null) return;
        stringResource = extras.getInt(TITLE);
        mealId = extras.getInt(MEAL_ID);

        updateMealData();
        setQuickAddNavigation(stringResource);

        foodsList = findViewById(R.id.foods_list);
        emptyList = findViewById(R.id.empty_list);
        setListAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateMealData();
        if(foodAdapter != null) updateFoodList();
    }

    private void setListAdapter() {
        DatabaseHelper.executeInBackground(() -> {
            List<FoodPreset> foods = new ArrayList<>(DatabaseHelper.FoodHelper.getAllFoods());

            runOnUiThread(() -> {
                foodAdapter = new FoodAdapter(foods, ActionType.ADD, mealId);
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
            List<FoodPreset> foods = new ArrayList<>(DatabaseHelper.FoodHelper.getAllFoods());

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
            List<FoodPreset> foods = new ArrayList<>(DatabaseHelper.FoodHelper.getFoodsByName(search));

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

    public void updateMealData() {
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