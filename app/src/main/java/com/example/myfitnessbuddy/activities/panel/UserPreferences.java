package com.example.myfitnessbuddy.activities.panel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.myfitnessbuddy.database.models.enums.Activity;
import com.example.myfitnessbuddy.database.models.enums.Goal;
import com.example.myfitnessbuddy.database.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;

public class UserPreferences {
    private static final String HEIGHT = "HEIGHT";
    private static final String GOAL = "GOAL";
    private static final String ACTIVITY = "ACTIVITY";
    private static final String BIRTH_DATE = "BIRTH_DATE";
    private static final String CALORIE_GOAL = "BIRTH_DATE";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public static void writeUserPreferences(Context context, User user) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(HEIGHT, user.getHeight());
        editor.putString(GOAL, user.getGoal().name());
        editor.putString(ACTIVITY, user.getActivity().name());

        String birthDateString = convertLocalDateToString(user.getBirthDate());
        editor.putString(BIRTH_DATE, birthDateString);

        editor.apply();
    }

    public static User readUserPreferences(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        int height = sharedPref.getInt(HEIGHT, 0); // Default value 0, adjust as needed
        String goalString = sharedPref.getString(GOAL, "");
        String activityString = sharedPref.getString(ACTIVITY, "");
        String birthDateString = sharedPref.getString(BIRTH_DATE, "");

        if(height == 0 || goalString.equals("") || activityString.equals("") || birthDateString.equals("")){
            return null;
        }

        Goal goal = Goal.valueOf(goalString);
        Activity activity = Activity.valueOf(activityString);
        LocalDate birthDate = convertStringToLocalDate(birthDateString);

        return new User(birthDate, height, goal, activity);
    }

    public static String convertLocalDateToString(LocalDate date){
        return dateFormat.format(java.sql.Date.valueOf(date.toString()));
    }

    public static LocalDate convertStringToLocalDate(String dateString) {
        try {
            return dateFormat.parse(dateString).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addWeight(){
        // Todo
    }

    public void getWeight(){
        // Todo
    }
}
