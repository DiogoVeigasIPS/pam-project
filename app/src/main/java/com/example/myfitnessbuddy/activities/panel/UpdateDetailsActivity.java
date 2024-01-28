package com.example.myfitnessbuddy.activities.panel;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.models.Activity;
import com.example.myfitnessbuddy.models.Goal;
import com.example.myfitnessbuddy.models.User;

import java.time.LocalDate;

public class UpdateDetailsActivity extends AppCompatActivity {
    EditText birthDateInput, heightInput, weightInput;
    Spinner objectiveSpinner, activitySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        ImageButton btBack = findViewById(R.id.bt_back);
        btBack.setOnClickListener(v -> finish());

        birthDateInput = findViewById(R.id.birthdate_input);
        heightInput = findViewById(R.id.height_input);
        objectiveSpinner = findViewById(R.id.spinner_objective);
        activitySpinner = findViewById(R.id.spinner_activity);

        fillSpinners();

        birthDateInput.setOnClickListener(v -> showDatePickerDialog());

        ((ImageButton)findViewById(R.id.bt_submit)).setOnClickListener(v -> updateUserPreferences());
    }

    private void updateUserPreferences(){
        Goal selectedGoal = (Goal) objectiveSpinner.getSelectedItem();
        Activity selectedActivity = (Activity) activitySpinner.getSelectedItem();
        String birthDateString = birthDateInput.getText().toString();
        String heightString = heightInput.getText().toString();

        if(birthDateString.equals("") || heightString.equals("") || selectedGoal == Goal.NOT_DEFINED || selectedActivity == Activity.NOT_DEFINED){
            Toast.makeText(UpdateDetailsActivity.this, R.string.invalid_details, Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate birthDate = UserPreferences.convertStringToLocalDate(birthDateString);

        int height;
        try{
            height = Integer.parseInt(heightString);
        }catch(NumberFormatException numberFormatException){
            Log.d("UpdateDetailsActivity", numberFormatException.toString());
            Toast.makeText(UpdateDetailsActivity.this, R.string.invalid_height_format, Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            User user = new User(birthDate, height, selectedGoal, selectedActivity);
            UserPreferences.writeUserPreferences(UpdateDetailsActivity.this, user);
            finish();
        }catch(IllegalArgumentException illegalArgumentException){
            Log.d("UpdateDetailsActivity", illegalArgumentException.toString());
            Toast.makeText(UpdateDetailsActivity.this, R.string.invalid_details, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue() - 1;  // Month in DatePickerDialog is 0-indexed
        int day = currentDate.getDayOfMonth();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                UpdateDetailsActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                        birthDateInput.setText(selectedDate.toString());
                    }
                },
                year, month, day
        );

        // Show the date picker dialog
        datePickerDialog.show();
    }

    private void fillSpinners(){
        ArrayAdapter<Goal> objectiveAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Goal.values());
        objectiveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        objectiveSpinner.setAdapter(objectiveAdapter);

        ArrayAdapter<Activity> activityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Activity.values());
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);
    }
}