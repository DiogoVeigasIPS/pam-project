package com.example.myfitnessbuddy.main_fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.panel.UpdateDetailsActivity;
import com.example.myfitnessbuddy.activities.panel.UserPreferences;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.Day;
import com.example.myfitnessbuddy.database.models.User;
import com.example.myfitnessbuddy.database.models.enums.Goal;
import com.google.android.material.progressindicator.CircularProgressIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPanel#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPanel extends Fragment {

    TextView mainRemaining, mainGoal, mainFoods,
            lastMonthWeight, todayWeight;
    CircularProgressIndicator calorieProgress;

    public FragmentPanel() {
        // Required empty public constructor
    }

    public static FragmentPanel newInstance() {
        FragmentPanel fragment = new FragmentPanel();
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
        return inflater.inflate(R.layout.fragment_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton calorieButton = getView().findViewById(R.id.mbt_calorie_add);
        ImageButton weightButton = getView().findViewById(R.id.mbt_weight_add);
        AppCompatButton personalDetailsButton = getView().findViewById(R.id.bt_personal_details);

        calorieButton.setOnClickListener(v -> {
            CalorieGoalDialogFragment calorieGoalDialogFragment = new CalorieGoalDialogFragment();
            calorieGoalDialogFragment.show(getChildFragmentManager(), "CalorieGoalDialogFragmentTag");
        });

        weightButton.setOnClickListener(v -> {
            WeightDialogFragment weightDialogFragment = new WeightDialogFragment();
            weightDialogFragment.show(getChildFragmentManager(), "WeightDialogFragmentTag");
        });

        personalDetailsButton.setOnClickListener(v -> {
            Intent caloryIntent = new Intent(getActivity(), UpdateDetailsActivity.class);
            startActivity(caloryIntent);
        });

        setViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void setViews() {
        calorieProgress = getView().findViewById(R.id.calorie_progress);
        mainRemaining = getView().findViewById(R.id.main_remaining);
        mainGoal = getView().findViewById(R.id.main_goal);
        mainFoods = getView().findViewById(R.id.main_foods);
        lastMonthWeight = getView().findViewById(R.id.last_month_weight);
        todayWeight = getView().findViewById(R.id.today_weight);
    }

    private void updateUI(){
        DatabaseHelper.executeInBackground(() -> {
            Day today = DatabaseHelper.DayHelper.getToday();
            int dayId = today.getId();

            int totalCalories = DatabaseHelper.DayHelper.getTotalCalories(dayId);
            int calorieGoal = today.getCalorieGoal();
            int caloriesLeft = calorieGoal - totalCalories;

            int lastMonthAverage = DatabaseHelper.DayHelper.getLastMonthAverageCalories();
            int lastWeight = today.getWeight();

            User user = UserPreferences.readUserPreferences(getActivity());
            Goal goal = user.getGoal();

            requireActivity().runOnUiThread(() -> {
                mainGoal.setText(String.valueOf(calorieGoal));
                mainFoods.setText(String.valueOf(totalCalories));
                mainRemaining.setText(String.valueOf(caloriesLeft));
                double percentage = ((double) totalCalories / calorieGoal) * 100;

                calorieProgress.setProgress((int) percentage, true);

                lastMonthWeight.setText(String.valueOf(lastMonthAverage));
                todayWeight.setText(String.valueOf(lastWeight));

                int colorResourceId;

                if ((goal == Goal.GAIN && lastMonthAverage < lastWeight) || (goal == Goal.LOSE && lastMonthAverage > lastWeight)) {
                    colorResourceId = R.color.success;
                } else {
                    colorResourceId = R.color.danger;
                }

                changeWeightTextColor(colorResourceId);
            });
        });
    }

    private void changeWeightTextColor(int color){
        todayWeight.setTextColor(ContextCompat.getColor(getActivity(), color));
    }

    public static class WeightDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_weight, null);

            builder.setView(dialogView)
                    .setPositiveButton(R.string.update, null) // Set a null click listener initially
                    .setNegativeButton(R.string.cancel, (dialog, id) -> WeightDialogFragment.this.getDialog().cancel());

            AlertDialog dialog = builder.create();

            // Override the onShow method to customize the positive button's behavior
            dialog.setOnShowListener(dialogInterface -> {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(view -> {
                    // Your custom logic here
                    EditText weightInput = dialogView.findViewById(R.id.weight_input);
                    String weightStr = weightInput.getText().toString();

                    if(weightStr.equals("")){
                        Toast.makeText(getActivity(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int weight = Integer.parseInt(weightStr);
                        DatabaseHelper.executeInBackground(() -> {
                            Day today = DatabaseHelper.DayHelper.getToday();
                            today.setWeight(weight);
                            DatabaseHelper.DayHelper.updateDay(today);

                            requireActivity().runOnUiThread(() -> {
                                if(isAdded()){
                                    Toast.makeText(getActivity(), R.string.weight_updated, Toast.LENGTH_SHORT).show();
                                    ((FragmentPanel) getParentFragment()).updateUI();
                                    dialog.dismiss();
                                }
                            });
                        });

                    } catch (NumberFormatException numberFormatException){
                        Log.e("WeightDialogFragment", "Error parsing weight", numberFormatException);
                        Toast.makeText(getActivity(), R.string.invalid_weight_format, Toast.LENGTH_SHORT).show();
                    }
                });
            });

            return dialog;
        }
    }

    public static class CalorieGoalDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_calorie_goal, null);

            builder.setView(dialogView)
                    .setPositiveButton(R.string.update, null) // Set a null click listener initially
                    .setNegativeButton(R.string.cancel, (dialog, id) -> CalorieGoalDialogFragment.this.getDialog().cancel());

            AlertDialog dialog = builder.create();

            // Override the onShow method to customize the positive button's behavior
            dialog.setOnShowListener(dialogInterface -> {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(view -> {
                    // Your custom logic here
                    EditText calorieGoalInput = dialogView.findViewById(R.id.calorie_goal_input);
                    String calorieGoalStr = calorieGoalInput.getText().toString();

                    DatabaseHelper.executeInBackground(() -> {
                        if (!calorieGoalStr.isEmpty()) {
                            try {
                                int calorieGoal = Integer.parseInt(calorieGoalStr);
                                Day today = DatabaseHelper.DayHelper.getToday();

                                today.setCalorieGoal(calorieGoal);
                                DatabaseHelper.DayHelper.updateDay(today);

                                requireActivity().runOnUiThread(() -> {
                                    // Check if the fragment is still attached to the activity
                                    if (isAdded()) {
                                        Toast.makeText(getActivity(), R.string.calorie_goal_updated, Toast.LENGTH_SHORT).show();
                                        ((FragmentPanel) getParentFragment()).updateUI();
                                        dialog.dismiss();
                                    }
                                });
                            } catch (NumberFormatException numberFormatException) {
                                Log.e("CalorieGoalDialogFragment", "Error parsing calorie goal", numberFormatException);
                                requireActivity().runOnUiThread(() -> {
                                    if (isAdded())
                                        Toast.makeText(getActivity(), R.string.invalid_calorie_format, Toast.LENGTH_SHORT).show();
                                });
                            }
                        } else {
                            // If the field is empty, calculate the calorie goal using the user object
                            User user = UserPreferences.readUserPreferences(getActivity());
                            if (user == null) {
                                requireActivity().runOnUiThread(() -> {
                                    if (isAdded())
                                        Toast.makeText(getActivity(), R.string.user_preferences_or_calorie, Toast.LENGTH_LONG).show();
                                });
                                return;
                            }

                            Day today = DatabaseHelper.DayHelper.getToday();
                            today.setCalorieGoal(user.calculateCalorieGoal(today.getWeight()));
                            DatabaseHelper.DayHelper.updateDay(today);

                            requireActivity().runOnUiThread(() -> {
                                // Check if the fragment is still attached to the activity
                                if (isAdded()) {
                                    Toast.makeText(getActivity(), R.string.calorie_goal_updated, Toast.LENGTH_SHORT).show();
                                    ((FragmentPanel) getParentFragment()).updateUI();
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                });
            });

            return dialog;
        }
    }

}