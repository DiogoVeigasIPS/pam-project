package com.example.myfitnessbuddy.main_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.diary.AddToMealActivity;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.models.Day;
import com.example.myfitnessbuddy.models.Meal;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDiary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDiary extends Fragment {

    private TextView goalOutput, foodOutput, leftoverGoalOutput,
            breakfastCalories, lunchCalories, dinnerCalories, snackCalories;

    public FragmentDiary() {
        // Required empty public constructor
    }

    public static FragmentDiary newInstance() {
        FragmentDiary fragment = new FragmentDiary();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Navigation
        setNavigationalButtons();

        // Dynamic values
        setTextViews();
        updateCalories();
    }

    private void updateCalories() {
        // TODO Read values from db and update textviews
    }

    private void setTextViews() {
        goalOutput = getView().findViewById(R.id.goal_output);
        foodOutput = getView().findViewById(R.id.food_output);
        leftoverGoalOutput = getView().findViewById(R.id.leftover_goal_output);

        breakfastCalories = getView().findViewById(R.id.breakfast_calories);
        lunchCalories = getView().findViewById(R.id.lunch_calories);
        dinnerCalories = getView().findViewById(R.id.dinner_calories);
        snackCalories = getView().findViewById(R.id.snack_calories);
    }

    private void setNavigationalButtons() {
        ImageButton btBack = getView().findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> Navigation.updateFragment(FragmentPanel.newInstance()));

        Button addBreakfast = getView().findViewById(R.id.add_breakfast);
        Button addLunch = getView().findViewById(R.id.add_lunch);
        Button addDinner = getView().findViewById(R.id.add_dinner);
        Button addSnack = getView().findViewById(R.id.add_snack);

        addBreakfast.setOnClickListener(v -> addFoodToMeal(R.string.breakfast));
        addLunch.setOnClickListener(v -> addFoodToMeal(R.string.lunch));
        addDinner.setOnClickListener(v -> addFoodToMeal(R.string.dinner));
        addSnack.setOnClickListener(v -> addFoodToMeal(R.string.snack));
    }

    // Send meal or something (like an id)
    private void addFoodToMeal(int title){
        DatabaseHelper.executeInBackground(() -> {
            Day today = DatabaseHelper.DayHelper.getToday();
            List<Meal> meals = DatabaseHelper.DayHelper.getMealsForDay(today.getId());
            Meal selectedMeal;

            if(title == R.string.breakfast){
                selectedMeal = meals.get(0);
            }else if(title == R.string.lunch){
                selectedMeal = meals.get(1);
            }else if(title == R.string.dinner){
                selectedMeal = meals.get(2);
            }else{
                selectedMeal = meals.get(3);
            }

            requireActivity().runOnUiThread(() -> {
                Intent intent = new Intent(getContext(), AddToMealActivity.class);
                intent.putExtra(AddToMealActivity.TITLE, title);
                intent.putExtra(AddToMealActivity.MEAL_ID, selectedMeal.getId());
                startActivity(intent);
            });
        });

    }
}