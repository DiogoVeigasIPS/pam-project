package com.example.myfitnessbuddy.main_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.diary.AddToMealActivity;
import com.example.myfitnessbuddy.activities.panel.UserPreferences;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.Day;
import com.example.myfitnessbuddy.database.models.Meal;

import java.time.LocalDate;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDiary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDiary extends Fragment {

    private TextView goalOutput, foodOutput, leftoverGoalOutput,
            breakfastCalories, lunchCalories, dinnerCalories, snackCalories;
    private int dayId;

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

        initDayId();

        setNavigationalButtons();
        setTextViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCalories();
    }

    private void initDayId(){
        DatabaseHelper.executeInBackground(() -> {
            Day today = DatabaseHelper.DayHelper.getToday();
            dayId = today.getDayId();
        });
    }

    private void updateCalories() {
        DatabaseHelper.executeInBackground(() -> {
            Day today = DatabaseHelper.DayHelper.getDayById(dayId);
            List<Integer> calories = DatabaseHelper.DayHelper.getCaloriesList(dayId);

            requireActivity().runOnUiThread(() -> {
                if(calories.size() < 4) return;

                breakfastCalories.setText(String.valueOf(calories.get(0)));
                lunchCalories.setText(String.valueOf(calories.get(1)));
                dinnerCalories.setText(String.valueOf(calories.get(2)));
                snackCalories.setText(String.valueOf(calories.get(3)));

                int sum = 0;
                for(Integer calorie : calories){
                    sum += calorie;
                }

                goalOutput.setText(String.valueOf(today.getCalorieGoal()));
                foodOutput.setText(String.valueOf(sum));
                leftoverGoalOutput.setText(String.valueOf(today.getCalorieGoal() - sum));
            });
        });
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
        btBack.setOnClickListener(v -> Navigation.navigateToPanel());

        Button addBreakfast = getView().findViewById(R.id.add_breakfast);
        Button addLunch = getView().findViewById(R.id.add_lunch);
        Button addDinner = getView().findViewById(R.id.add_dinner);
        Button addSnack = getView().findViewById(R.id.add_snack);

        ImageButton editBreakfast = getView().findViewById(R.id.bt_edit_breakfast);
        ImageButton editLunch = getView().findViewById(R.id.bt_edit_lunch);
        ImageButton editDinner = getView().findViewById(R.id.bt_edit_dinner);
        ImageButton editSnack = getView().findViewById(R.id.bt_edit_snack);

        ImageButton btDateBack = getView().findViewById(R.id.bt_date_back);
        ImageButton btDateForward = getView().findViewById(R.id.bt_date_forward);

        addBreakfast.setOnClickListener(v -> addFoodToMeal(R.string.breakfast));
        addLunch.setOnClickListener(v -> addFoodToMeal(R.string.lunch));
        addDinner.setOnClickListener(v -> addFoodToMeal(R.string.dinner));
        addSnack.setOnClickListener(v -> addFoodToMeal(R.string.snack));

        editBreakfast.setOnClickListener(v -> editMeal(R.string.breakfast));
        editLunch.setOnClickListener(v -> editMeal(R.string.lunch));
        editDinner.setOnClickListener(v -> editMeal(R.string.dinner));
        editSnack.setOnClickListener(v -> editMeal(R.string.snack));

        btDateBack.setOnClickListener(v -> {
            updateDay(true);
        });

        btDateForward.setOnClickListener(v -> {
            updateDay(false);
        });
    }

    private void updateDay(boolean goBack) {
        DatabaseHelper.executeInBackground(() -> {
            int visitingDayId = goBack ? dayId - 1 : dayId + 1;

            Day day = DatabaseHelper.DayHelper.getDayById(visitingDayId);

            requireActivity().runOnUiThread(() -> {
                if(day == null){
                    Toast.makeText(getActivity(), R.string.no_more_day, Toast.LENGTH_SHORT).show();
                    return;
                }

                dayId = visitingDayId;
                TextView dateText = getView().findViewById(R.id.date_text);

                if(!day.getDate().equals(LocalDate.now())){
                    if(day.getDate().equals(LocalDate.now().minusDays(1))){
                        dateText.setText(R.string.yesterday);
                    }else{
                        dateText.setText(UserPreferences.convertLocalDateToString(day.getDate()));
                    }
                }else{
                    dateText.setText(R.string.today);
                }

                updateCalories();
            });

        });
    }

    private void editMeal(int title) {
        DatabaseHelper.executeInBackground(() -> {
            List<Meal> meals = DatabaseHelper.DayHelper.getMealsForDay(dayId);
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
                intent.putExtra(AddToMealActivity.IS_EDIT, true);
                intent.putExtra(AddToMealActivity.MEAL_ID, selectedMeal.getMealId());
                startActivity(intent);
            });
        });
    }

    private void addFoodToMeal(int title){
        DatabaseHelper.executeInBackground(() -> {
            List<Meal> meals = DatabaseHelper.DayHelper.getMealsForDay(dayId);
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
                intent.putExtra(AddToMealActivity.MEAL_ID, selectedMeal.getMealId());
                startActivity(intent);
            });
        });
    }
}