package com.example.myfitnessbuddy.activities.panel;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.foods.AddFoodActivity;
import com.example.myfitnessbuddy.database.DatabaseHelper;
import com.example.myfitnessbuddy.database.models.Day;
import com.example.myfitnessbuddy.database.models.enums.Activity;
import com.example.myfitnessbuddy.database.models.enums.Goal;
import com.example.myfitnessbuddy.database.models.User;
import com.example.myfitnessbuddy.utils.CustomToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class UpdateDetailsActivity extends AppCompatActivity {
    private EditText birthDateInput, heightInput, weightInput;
    private Spinner objectiveSpinner, activitySpinner;
    private final SimpleDateFormat dateFormat = UserPreferences.getDateFormat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        ImageButton btBack = findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> finish());

        assignViews();

        fillSpinners();

        birthDateInput.setOnClickListener(v -> showDatePickerDialog());

        getUserDetails();

        findViewById(R.id.bt_submit).setOnClickListener(v -> updateUserPreferences());
    }

    private void getUserDetails() {
        User user;
        try{
            user = UserPreferences.readUserPreferences(this);
        }catch(IllegalArgumentException illegalArgumentException){
            return;
        }

        if (user == null) return;

        birthDateInput.setText(UserPreferences.convertLocalDateToString(user.getBirthDate()));
        heightInput.setText(String.valueOf(user.getHeight()));

        @SuppressWarnings("unchecked")
        ArrayAdapter<Goal> goalAdapter = (ArrayAdapter<Goal>) objectiveSpinner.getAdapter();
        @SuppressWarnings("unchecked")
        ArrayAdapter<Activity> activityAdapter = (ArrayAdapter<Activity>) activitySpinner.getAdapter();

        objectiveSpinner.setSelection((goalAdapter).getPosition(user.getGoal()));
        activitySpinner.setSelection((activityAdapter).getPosition(user.getActivity()));

        DatabaseHelper.executeInBackground(() -> {
            int weight = DatabaseHelper.DayHelper.getLastWeight();

            runOnUiThread(() -> weightInput.setText(String.valueOf(weight)));
        });
    }

    private boolean dateIsValid(String date) {
        if (date.trim().equals("")) return false;

        try {
            Date parsedDate = dateFormat.parse(date);

            // If parsing succeeds, the date format is valid
            return parsedDate != null;
        } catch (ParseException e) {
            return false;
        }
    }

    private void updateUserPreferences() {
        Goal selectedGoal = (Goal) objectiveSpinner.getSelectedItem();
        Activity selectedActivity = (Activity) activitySpinner.getSelectedItem();
        String birthDateStr = birthDateInput.getText().toString();
        String heightStr = heightInput.getText().toString();
        String weightStr = weightInput.getText().toString();

        if (weightStr.trim().equals("") || heightStr.trim().equals("") || selectedGoal == Goal.NOT_DEFINED || selectedActivity == Activity.NOT_DEFINED) {
            CustomToast.showErrorToast(UpdateDetailsActivity.this, R.string.fill_all_fields, Toast.LENGTH_SHORT);
            return;
        }

        if (!dateIsValid(birthDateStr)) {
            CustomToast.showErrorToast(UpdateDetailsActivity.this, R.string.invalid_date, Toast.LENGTH_SHORT);
            return;
        }

        LocalDate birthDate = UserPreferences.convertStringToLocalDate(birthDateStr);

        int height;
        try {
            height = Integer.parseInt(heightStr);
        } catch (NumberFormatException numberFormatException) {
            Log.d("UpdateDetailsActivity", numberFormatException.toString());
            CustomToast.showErrorToast(UpdateDetailsActivity.this, R.string.invalid_height_format, Toast.LENGTH_SHORT);
            return;
        }

        int weight;
        try {
            weight = Integer.parseInt(weightStr);
        } catch (NumberFormatException numberFormatException) {
            Log.d("UpdateDetailsActivity", numberFormatException.toString());
            CustomToast.showErrorToast(UpdateDetailsActivity.this, R.string.invalid_weight_format, Toast.LENGTH_SHORT);
            return;
        }

        try {
            User user = new User(birthDate, height, selectedGoal, selectedActivity);
            UserPreferences.writeUserPreferences(UpdateDetailsActivity.this, user);
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.d("UpdateDetailsActivity", illegalArgumentException.toString());
            CustomToast.showErrorToast(UpdateDetailsActivity.this, R.string.invalid_details, Toast.LENGTH_SHORT);
        }

        DatabaseHelper.executeInBackground(() -> {
            Day today = DatabaseHelper.DayHelper.getToday();

            try{
                today.setWeight(weight);
                DatabaseHelper.DayHelper.updateDay(today);
            }catch(IllegalArgumentException illegalArgumentException){
                Log.d("UpdateDetailsActivity", illegalArgumentException.toString());
            }

            runOnUiThread(() -> finish());
        });
    }

    private void showDatePickerDialog() {
        int year, month, day;

        try {
            LocalDate userDate = LocalDate.parse(birthDateInput.getText().toString());
            year = userDate.getYear();
            month = userDate.getMonthValue();
            day = userDate.getDayOfMonth();
        } catch (DateTimeParseException dateTimeParseException) {
            LocalDate currentDate = LocalDate.now();
            year = currentDate.getYear();
            month = currentDate.getMonthValue();
            day = currentDate.getDayOfMonth();
        }

        month--; // Month in DatePickerDialog is 0-indexed

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                UpdateDetailsActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(year1, month1 + 1, dayOfMonth);

                    try {
                        String formattedDate = UserPreferences.convertLocalDateToString(selectedDate);
                        birthDateInput.setText(formattedDate);
                    } catch (Exception e) {
                        CustomToast.showErrorToast(UpdateDetailsActivity.this, R.string.invalid_date, Toast.LENGTH_SHORT);
                    }

                },
                year, month, day
        );

        // Show the date picker dialog
        datePickerDialog.show();
    }

    private void fillSpinners() {
        // Custom adapter for the Goal spinner
        ArrayAdapter<Goal> customObjectiveAdapter = new ArrayAdapter<Goal>(this, android.R.layout.simple_spinner_item, Goal.values()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setText(getItem(position).getText(getContext()));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setText(getItem(position).getText(getContext()));
                }
                return view;
            }
        };

        customObjectiveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        objectiveSpinner.setAdapter(customObjectiveAdapter);

        // Custom adapter for the Activity spinner
        ArrayAdapter<Activity> customActivityAdapter = new ArrayAdapter<Activity>(this, android.R.layout.simple_spinner_item, Activity.values()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setText(getItem(position).getText(getContext()));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setText(getItem(position).getText(getContext()));
                }
                return view;
            }
        };

        customActivityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(customActivityAdapter);
    }

    private void assignViews() {
        birthDateInput = findViewById(R.id.birthdate_input);
        heightInput = findViewById(R.id.height_input);
        objectiveSpinner = findViewById(R.id.spinner_objective);
        activitySpinner = findViewById(R.id.spinner_activity);
        weightInput = findViewById(R.id.weight_input);
    }

}